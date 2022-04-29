package org.oka.effectivejava.cache.java;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class LFUCacheServiceTest {

    @Test
    public void shouldReturnNull_WhenTheItemDoesNotExist() {
        // Given
        LFUCacheService<String, String> cacheService = new LFUCacheService<>();

        // When
        String cachedValue = cacheService.get("random_key");

        // Then
        assertThat(cachedValue).isNull();
    }

    @Test
    public void shouldIncreaseAndReturnMissesByTwo_WhenTheItemDoesNotExist() {
        // Given
        LFUCacheService<String, String> cacheService = new LFUCacheService<>();

        // When
        cacheService.get("random_key");
        cacheService.get("random_key2");

        // Then
        assertThat(cacheService.getMisses()).isEqualTo(2);
    }

    @Test
    public void shouldReturnCachedValue() {
        // Given
        LFUCacheService<String, String> cacheService = new LFUCacheService<>();
        cacheService.put("key", "value");

        // When
        String value = cacheService.get("key");

        // Then
        assertThat(value).isEqualTo("value");
    }

    @Test
    public void shouldIncreaseHits() {
        // Given
        LFUCacheService<String, String> cacheService = new LFUCacheService<>();
        cacheService.put("key", "value");

        // When
        cacheService.get("key");

        // Then
        assertThat(cacheService.getHits()).isEqualTo(1);
    }

    @Test
    public void shouldIncreaseCachedItemCounter_WhenCallingGet() {
        // Given
        LFUCacheService<String, String> cacheService = new LFUCacheService<>();
        cacheService.put("key", "value");

        // When
        cacheService.get("key");

        // Then
        assertThat(cacheService.getCachedItemStats()).containsEntry("key", 2);
    }

    @Test
    public void shouldReturnImmutableCachedItemStats() {
        // Given
        LFUCacheService<String, String> cacheService = new LFUCacheService<>();
        cacheService.put("key", "value");

        // When
        Map<String, Integer> cachedItemStats = cacheService.getCachedItemStats();

        // Then
        assertThrows(UnsupportedOperationException.class, cachedItemStats::clear);
    }

    @Test
    public void shouldReturnImmutableCachedItemFreq() {
        // Given
        LFUCacheService<String, String> cacheService = new LFUCacheService<>();
        cacheService.put("key", "value");

        // When
        Map<Integer, List<String>> map = cacheService.getCachedItemFreq();

        // Then
        assertThrows(UnsupportedOperationException.class, map::clear);
    }

    @Test
    public void shouldReturnImmutableCachedItemStorage() {
        // Given
        LFUCacheService<String, String> cacheService = new LFUCacheService<>();
        cacheService.put("key", "value");

        // When
        Map<String, String> map = cacheService.getCachedItemStorage();

        // Then
        assertThrows(UnsupportedOperationException.class, map::clear);
    }

    @Test
    public void shouldRemoveCachedItemFromFrequency_WhenCallingGet() {
        // Given
        LFUCacheService<String, String> cacheService = new LFUCacheService<>();
        cacheService.put("key", "value");

        // When
        cacheService.get("key");

        // Then
        assertThat(cacheService.getCachedItemFreq()).doesNotContainKey(1);
    }

    @Test
    public void shouldRemoveCachedItemFromFrequency_DuringEviction() {
        // Given
        LFUCacheService<String, String> cacheService = new LFUCacheService<>();
        cacheService.put("key", "value");

        // When
        for (int i = 0; i < 100_000; i++) {
            cacheService.put("key" + i, "value");
            cacheService.get("key" + i);
        }

        // Then
        assertThat(cacheService.getCachedItemFreq()).doesNotContainKey(1);
    }

    @Test
    public void shouldNotRemoveCachedItemFromFrequency_IfOtherEntriesHaveSameFrequency() {
        // Given
        LFUCacheService<String, String> cacheService = new LFUCacheService<>();
        cacheService.put("key", "value");
        cacheService.put("key2", "value");

        // When
        cacheService.get("key");

        // Then
        assertThat(cacheService.getCachedItemFreq()).containsEntry(1, List.of("key2"));
        assertThat(cacheService.getCachedItemFreq()).containsEntry(2, List.of("key"));
    }

    @Test
    public void shouldNotRemoveCachedItemFromFrequency_IfOtherEntriesHaveSameFrequencyDuringEviction() {
        // Given
        LFUCacheService<String, String> cacheService = new LFUCacheService<>();
        cacheService.put("key", "value");
        cacheService.put("keyy", "value");

        // When
        for (int i = 0; i < 99_999; i++) {
            cacheService.put("key" + i, "value");
            cacheService.get("key" + i);
        }

        // Then
        assertThat(cacheService.getCachedItemFreq()).containsKey(1);
    }

    @Test
    public void shouldIncreaseFrequencyOfCachedItem() {
        // Given
        LFUCacheService<String, String> cacheService = new LFUCacheService<>();
        cacheService.put("key", "value");

        // When
        cacheService.get("key");

        // Then
        assertThat(cacheService.getCachedItemFreq()).containsEntry(2, List.of("key"));
    }

    @Test
    public void shouldAddCacheItem() {
        // Given
        LFUCacheService<String, String> cacheService = new LFUCacheService<>();

        // When
        cacheService.put("key", "value");

        // Then
        assertThat(cacheService.getCachedItemStorage()).containsEntry("key", "value");
    }

    @Test
    public void shouldAddCacheTwoItem() {
        // Given
        LFUCacheService<String, String> cacheService = new LFUCacheService<>();

        // When
        cacheService.put("key", "value");
        cacheService.put("key2", "value2");

        // Then
        assertThat(cacheService.getCachedItemStorage())
                .containsEntry("key", "value")
                .containsEntry("key2", "value2");
    }

    @Test
    public void shouldReturnEmptyCachedItemStats() {
        // Given
        LFUCacheService<String, String> cacheService = new LFUCacheService<>();

        // When

        // Then
        assertThat(cacheService.getCachedItemStats()).isEmpty();
    }

    @Test
    public void shouldIncreaseCachedItemCounter() {
        // Given
        LFUCacheService<String, String> cacheService = new LFUCacheService<>();

        // When
        cacheService.put("key", "value");

        // Then
        assertThat(cacheService.getCachedItemStats()).containsEntry("key", 1);
    }

    @Test
    public void shouldReturnStatsForTwoKeys() {
        // Given
        LFUCacheService<String, String> cacheService = new LFUCacheService<>();

        // When
        cacheService.put("key", "value");
        cacheService.put("key2", "value2");

        // Then
        assertThat(cacheService.getCachedItemStats())
                .containsEntry("key", 1)
                .containsEntry("key2", 1);
    }

    @Test
    public void shouldReplaceTheValue_WhenKeyExists() {
        // Given
        LFUCacheService<String, String> cacheService = new LFUCacheService<>();

        // When
        cacheService.put("key", "value");
        cacheService.put("key", "value2");

        // Then
        assertThat(cacheService.getCachedItemStorage()).containsEntry("key", "value2");
        assertThat(cacheService.getCachedItemStorage()).doesNotContainEntry("key", "value");
    }

    @Test
    public void shouldIncreaseTheCounter_WhenKeyExists() {
        // Given
        LFUCacheService<String, String> cacheService = new LFUCacheService<>();

        // When
        cacheService.put("key", "value");
        cacheService.put("key", "value2");

        // Then
        assertThat(cacheService.getCachedItemStats()).containsEntry("key", 2);
    }

    @Test
    public void shouldIncreaseTheFrequencyCounter_WhenKeyExists() {
        // Given
        LFUCacheService<String, String> cacheService = new LFUCacheService<>();

        // When
        cacheService.put("key", "value");
        cacheService.put("key", "value2");

        // Then
        assertThat(cacheService.getCachedItemFreq()).containsEntry(2, List.of("key"));
    }

    @Test
    public void shouldRemoveItemFromFrequency_WhenKeyExists() {
        // Given
        LFUCacheService<String, String> cacheService = new LFUCacheService<>();

        // When
        cacheService.put("key", "value");
        cacheService.put("key", "value2");

        // Then
        assertThat(cacheService.getCachedItemFreq()).doesNotContainKey(1);
    }

    @Test
    public void shouldNotRemoveItemFromFrequency_IfListDoesNotGetEmpty() {
        // Given
        LFUCacheService<String, String> cacheService = new LFUCacheService<>();

        // When
        cacheService.put("key0", "value0");
        cacheService.put("key", "value");
        cacheService.put("key", "value2");

        // Then
        assertThat(cacheService.getCachedItemFreq()).containsKey(1);
    }

    @Test
    public void shouldEvictFirstCachedElement() {
        // Given
        LFUCacheService<String, String> cacheService = new LFUCacheService<>();
        cacheService.put("first_key", "value");
        for (int i = 0; i < 99_999; i++) {
            cacheService.put(randomAlphabetic(15), "value");
        }

        // When
        cacheService.put("last", "value");

        // Then
        assertThat(cacheService.get("first_key")).isNull();
    }

    @Test
    public void shouldRemoveFrequencyListItem_WhenEvictionLeavesFrequencyEmpty() {
        // Given
        LFUCacheService<String, String> cacheService = new LFUCacheService<>();
        cacheService.put("first_key", "value");
        for (int i = 0; i < 99_999; i++) {
            String randomKey = randomAlphabetic(15);
            cacheService.put(randomKey, "value");
            cacheService.get(randomKey);
        }

        // When
        cacheService.put("last", "value");
        cacheService.get("last");

        // Then
        assertThat(cacheService.getCachedItemFreq().get(1)).isNull();
    }

    @Test
    public void shouldIncreaseTheEvictionsNumber() {
        // Given
        LFUCacheService<String, String> cacheService = new LFUCacheService<>();
        for (int i = 0; i < 100_005; i++) {
            String randomKey = "key" + i;
            cacheService.put(randomKey, "value");
        }

        // When
        Integer evictions = cacheService.getEvictions();

        // Then
        assertThat(evictions).isEqualTo(5);
    }

    @Test
    public void shouldNotRemoveFirstCachedElement_WhenNotInTheLimit() {
        // Given
        LFUCacheService<String, String> cacheService = new LFUCacheService<>();
        cacheService.put("first_key", "value");
        for (int i = 0; i < 99_998; i++) {
            cacheService.put(randomAlphabetic(15), "value");
        }

        // When
        cacheService.put("last", "value");

        // Then
        assertThat(cacheService.get("first_key")).isEqualTo("value");
    }

    @Test
    public void shouldReturnAvgInsertionTime() {
        // Given
        LFUCacheService<String, String> cacheService = new LFUCacheService<>();

        // When
        for (int i = 0; i < 200_000; i++) {
            cacheService.put(randomAlphabetic(15), "value");
        }

        // Then
        assertThat(cacheService.getAvgInsertionTime()).isGreaterThan(0.0d).isLessThan(1d);
    }

    @Test
    public void shouldReturnInsertionTime() {
        // Given
        LFUCacheService<String, String> cacheService = new LFUCacheService<>();

        // When
        for (int i = 0; i < 200_000; i++) {
            cacheService.put(randomAlphabetic(15), "value");
        }

        // Then
        assertThat(cacheService.getInsertionTime()).isGreaterThan(0L);
    }

    @Test
    public void shouldAddCacheItemInMultithreadedEnvironment() throws InterruptedException {
        // Given
        LFUCacheService<String, String> cacheService = new LFUCacheService<>();
        ExecutorService executor = Executors.newFixedThreadPool(50);
        List<Callable<Void>> tasks = new ArrayList<>();
        for (int i = 0; i < 100_000; i++) {
            int counter = i;
            tasks.add(() -> {
                cacheService.put("key" + counter, "value");
                return null;
            });
        }

        // When
        executor.invokeAll(tasks);

        // Then
        Map<String, String> storage = cacheService.getCachedItemStorage();
        for (int i = 0; i < 100_000; i++) {
            assertThat(storage).containsEntry("key" + i, "value");
        }
        executor.shutdown();
    }

    @Test
    public void shouldAddCacheItemStatsInMultithreadedEnvironment() throws InterruptedException {
        // Given
        LFUCacheService<String, String> cacheService = new LFUCacheService<>();
        ExecutorService executor = Executors.newFixedThreadPool(50);
        List<Callable<Void>> tasks = new ArrayList<>();
        for (int i = 0; i < 100_000; i++) {
            int counter = i;
            tasks.add(() -> {
                cacheService.put("key" + counter, "value");
                return null;
            });
        }

        // When
        executor.invokeAll(tasks);

        // Then
        Map<String, Integer> stats = cacheService.getCachedItemStats();
        for (int i = 0; i < 100_000; i++) {
            assertThat(stats).containsEntry("key" + i, 1);
        }
        executor.shutdown();
    }

    @Test
    public void shouldAddCacheItemStatsInRepeatedMultithreadedEnvironment() throws InterruptedException {
        // Given
        LFUCacheService<String, String> cacheService = new LFUCacheService<>();
        ExecutorService executor = Executors.newFixedThreadPool(50);
        List<Callable<Void>> tasks = new ArrayList<>();
        for (int i = 0; i < 100_000; i++) {
            tasks.add(() -> {
                cacheService.put("key", "value");
                return null;
            });
        }

        // When
        executor.invokeAll(tasks);

        // Then
        Map<String, Integer> stats = cacheService.getCachedItemStats();
        assertThat(stats).containsEntry("key", 100_000);
        executor.shutdown();
    }
}
