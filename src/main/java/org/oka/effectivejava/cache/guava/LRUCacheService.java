package org.oka.effectivejava.cache.guava;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.RemovalListener;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.util.Map;

import static java.util.Collections.unmodifiableMap;

@Builder
@Slf4j
public class LRUCacheService<K, V> {
    /**
     * Max size of the cache storage.
     */
    private static final int MAX_SIZE = 100_000;
    /**
     * Cache object.
     */
    private Cache<K, V> cache;
    /**
     * Expiration after Duration.
     */
    private final Duration expireAfter;
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
     * Builds and returns the cache object based the configurations.
     */
    public void buildCache() {
        cache = CacheBuilder
                .newBuilder()
                .recordStats()
                .maximumSize(MAX_SIZE)
                .expireAfterAccess(expireAfter)
                .removalListener((RemovalListener<K, V>) notification -> {
                    if (notification.wasEvicted()) {
                        log.info("Cached Item: " + notification.getKey() + " evicted!. Reason: " + notification.getCause());
                    }
                })
                .build();
    }

    /**
     * Returns the cached S value for the key K.
     *
     * @param k Key to use in the lookup.
     * @return S cached value. Null if it not exists.
     */
    public V get(final K k) {
        return this.cache.getIfPresent(k);
    }

    /**
     * Add a new key/value to the cache service.
     *
     * @param k K key.
     * @param v V Value.
     */
    public void put(final K k, final V v) {
        final long timeBefore = System.currentTimeMillis();
        this.cache.put(k, v);
        final long timeAfter = System.currentTimeMillis();
        this.insertionTime = this.insertionTime + (timeAfter - timeBefore);
        this.insertions++;
    }

    /**
     * Exposes current Cached Items storage.
     *
     * @return Map<K, V>
     */
    public Map<K, V> getCachedItemStorage() {
        Map<K, V> mapToReturn = this.cache.asMap();
        return unmodifiableMap(mapToReturn);
    }

    /**
     * Exposes the number of hits.
     *
     * @return hits
     */
    public long getHits() {
        return cache.stats().hitCount();
    }

    /**
     * Exposes the number of misses.
     *
     * @return hits
     */
    public long getMisses() {
        return cache.stats().missCount();
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
