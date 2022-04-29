package org.oka.effectivejava.cache.java;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static java.util.Collections.unmodifiableMap;
import static java.util.stream.Collectors.toMap;

/**
 * Manages a generic a cache service of key (K) / value (V).
 *
 * @param <K> Key
 * @param <V> Value
 */
@Slf4j
public final class LFUCacheService<K, V> {
    /**
     * Max size of the cache storage.
     */
    private static final int MAX_SIZE = 100_000;
    /**
     * Stores the key/value.
     */
    private final HashMap<K, V> cachedItemStore = new HashMap<>(MAX_SIZE);
    /**
     * Store the number occurrences of each K key.
     */
    private final HashMap<K, Integer> cachedItemStats = new HashMap<>(MAX_SIZE);
    /**
     * Stores the frequency vs the K entities.
     */
    private final TreeMap<Integer, LinkedList<K>> cachedItemFreq = new TreeMap<>();
    /**
     * Holds the number of misses when using the cache service.
     */
    @Getter
    private Integer misses = 0;
    /**
     * Holds the number of hits.
     */
    @Getter
    private Integer hits = 0;
    /**
     * Holds the number of evictions.
     */
    @Getter
    private Integer evictions = 0;
    /**
     * Holds the total time used to insert elements.
     */
    @Getter
    private long insertionTime = 0L;
    /**
     * Holds the number of attempted insertions.
     */
    @Getter
    private long insertions = 0L;

    /**
     * Returns the cached S value for the key K.
     *
     * @param t Key to use in the lookup.
     * @return S cached value. Null if it not exists.
     */
    public synchronized V get(final K t) {
        if (!cachedItemStore.containsKey(t)) {
            misses = misses + 1;
            return null;
        }
        hits = hits + 1;
        Integer counter = cachedItemStats.get(t);
        cachedItemFreq.get(counter).remove(t);
        if (cachedItemFreq.get(counter).isEmpty()) {
            cachedItemFreq.remove(counter);
        }
        cachedItemFreq.computeIfAbsent(counter + 1, f -> new LinkedList<>()).add(t);
        cachedItemStats.put(t, counter + 1);
        return cachedItemStore.get(t);
    }

    /**
     * Add a new key/value to the cache service.
     *
     * @param k K key.
     * @param v V Value.
     */
    public synchronized void put(final K k, final V v) {
        final long timeBefore = System.currentTimeMillis();
        if (!cachedItemStore.containsKey(k)) {
            if (cachedItemStore.size() >= MAX_SIZE) {
                Integer lowestCount = cachedItemFreq.firstKey();
                K removedKey = cachedItemFreq.get(lowestCount).remove(0);
                if (cachedItemFreq.get(lowestCount).isEmpty()) {
                    cachedItemFreq.remove(lowestCount);
                }
                cachedItemStats.remove(removedKey);
                cachedItemStore.remove(removedKey);
                evictions++;
                log.info("Cached Item: " + removedKey + " evicted!");
            }
            cachedItemStore.put(k, v);
            cachedItemStats.put(k, 1);
            cachedItemFreq.computeIfAbsent(1, f -> new LinkedList<>()).add(k);
        } else {
            int count = cachedItemStats.get(k);
            cachedItemFreq.get(count).remove(k);
            if (cachedItemFreq.get(count).isEmpty()) {
                cachedItemFreq.remove(count);
            }
            cachedItemStats.put(k, count + 1);
            cachedItemStore.put(k, v);
            cachedItemFreq.computeIfAbsent(count + 1, f -> new LinkedList<>()).add(k);
        }
        final long timeAfter = System.currentTimeMillis();
        this.insertionTime = this.insertionTime + (timeAfter - timeBefore);
        this.insertions++;
    }

    /**
     * Exposes current Cached Items storage.
     *
     * @return Map<K, CacheItem < V>>
     */
    public Map<K, V> getCachedItemStorage() {
        Map<K, V> mapToReturn = this.cachedItemStore
                .entrySet()
                .stream()
                .collect(toMap(Map.Entry::getKey, p -> p.getValue()));
        return unmodifiableMap(mapToReturn);
    }

    /**
     * Exposes current Cached Items usage statistics.
     *
     * @return Map<K, CacheItem < V>>
     */
    public Map<K, Integer> getCachedItemStats() {
        Map<K, Integer> mapToReturn = this.cachedItemStats
                .entrySet()
                .stream()
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue));
        return unmodifiableMap(mapToReturn);
    }

    /**
     * Exposes the frequencies of the cached items.
     *
     * @return Map<Integer, LinkedList < K>>
     */
    public Map<Integer, List<K>> getCachedItemFreq() {
        Map<Integer, List<K>> mapToReturn = this.cachedItemFreq
                .entrySet()
                .stream()
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue));
        return unmodifiableMap(mapToReturn);
    }

    /**
     * Returns the average time for insertions.
     *
     * @return avg insertion time (ms)
     */
    public double getAvgInsertionTime() {
        return (double) this.insertionTime / this.insertions;
    }
}
