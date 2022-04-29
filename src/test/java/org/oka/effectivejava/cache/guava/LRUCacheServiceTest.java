package org.oka.effectivejava.cache.guava;

import org.junit.jupiter.api.Test;
import org.oka.effectivejava.cache.guava.LRUCacheService;
import org.oka.effectivejava.cache.java.LFUCacheService;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.time.temporal.ChronoUnit.SECONDS;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.assertj.core.api.Assertions.assertThat;

public class LRUCacheServiceTest {

    @Test
    public void shouldReturnNull_WhenTheItemDoesNotExist() {
        // Given
        LRUCacheService<String, String> lruCacheService = LRUCacheService
                .<String, String>builder()
                .expireAfter(Duration.of(5, SECONDS))
                .build();
        lruCacheService.buildCache();

        // When

        // Then
        assertThat(lruCacheService.get("key")).isNull();
    }

    @Test
    public void shouldStoreTheCachedItem() {
        // Given
        LRUCacheService<String, String> lruCacheService = LRUCacheService
                .<String, String>builder()
                .expireAfter(Duration.of(5, SECONDS))
                .build();
        lruCacheService.buildCache();

        // When
        lruCacheService.put("key", "value");
        lruCacheService.put("key2", "value");

        // Then
        assertThat(lruCacheService.getCachedItemStorage()).containsEntry("key", "value");
        assertThat(lruCacheService.getCachedItemStorage()).containsEntry("key2", "value");
    }

    @Test
    public void shouldReturnTheCachedItem() {
        // Given
        LRUCacheService<String, String> lruCacheService = LRUCacheService
                .<String, String>builder()
                .expireAfter(Duration.of(5, SECONDS))
                .build();
        lruCacheService.buildCache();
        lruCacheService.put("key", "value");
        lruCacheService.put("key2", "value");

        // When
        String value = lruCacheService.get("key");
        String value2 = lruCacheService.get("key2");

        // Then
        assertThat(value).isEqualTo("value");
        assertThat(value2).isEqualTo("value");
    }

    @Test
    public void shouldEvictAfter5Seconds() throws InterruptedException {
        // Given
        LRUCacheService<String, String> lruCacheService = LRUCacheService
                .<String, String>builder()
                .expireAfter(Duration.of(5, SECONDS))
                .build();
        lruCacheService.buildCache();
        lruCacheService.put("key", "value");

        // When
        Thread.sleep(6000);

        // Then
        assertThat(lruCacheService.getCachedItemStorage().get("key")).isNull();
    }

    @Test
    public void shouldIncreaseAndReturnMissesByTwo_WhenTheItemDoesNotExist() {
        // Given
        LRUCacheService<String, String> lruCacheService = LRUCacheService
                .<String, String>builder()
                .expireAfter(Duration.of(5, SECONDS))
                .build();
        lruCacheService.buildCache();
        lruCacheService.put("key", "value");

        // When
        lruCacheService.get("random_key");
        lruCacheService.get("random_key2");

        // Then
        assertThat(lruCacheService.getMisses()).isEqualTo(2);
    }

    @Test
    public void shouldIncreaseHits() {
        // Given
        LRUCacheService<String, String> lruCacheService = LRUCacheService
                .<String, String>builder()
                .expireAfter(Duration.of(5, SECONDS))
                .build();
        lruCacheService.buildCache();
        lruCacheService.put("key", "value");

        // When
        lruCacheService.get("key");

        // Then
        assertThat(lruCacheService.getHits()).isEqualTo(1);
    }

    @Test
    public void shouldIncreaseMisses() {
        // Given
        LRUCacheService<String, String> cacheService = LRUCacheService
                .<String, String>builder()
                .expireAfter(Duration.of(5, SECONDS))
                .build();
        cacheService.buildCache();

        // When
        cacheService.get("random_key");
        cacheService.get("random_key2");

        // Then
        assertThat(cacheService.getMisses()).isEqualTo(2);
    }

    @Test
    public void shouldReturnAvgInsertionTime() {
        // Given
        LRUCacheService<String, String> cacheService = LRUCacheService
                .<String, String>builder()
                .expireAfter(Duration.of(5, SECONDS))
                .build();
        cacheService.buildCache();

        // When
        for (int i = 0; i < 200_000; i++) {
            cacheService.put(randomAlphabetic(15), "value");
        }

        // Then
        assertThat(cacheService.getAvgInsertionTime()).isGreaterThan(0L).isLessThan(1L);
    }
}
