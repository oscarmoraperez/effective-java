package org.oka.effectivejava.search;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.oka.effectivejava.RandomGenerator.generateSortedArray;

public class BinaryRecursiveSearchBenchmarkingTest {
    private final SearchBenchmarker searchBenchmarker = new SearchBenchmarker();

    @ParameterizedTest
    @ValueSource(ints = {10_000, 30_000, 50_000, 70_000, 90_000, 110_000, 130_000, 150_000, 170_000, 190_000, 210_000})
    public void shouldProduceBenchmarking(final int size) {
        // Given
        Search linearSearch = new BinaryRecursiveSearch();

        // When
        double totalTime = searchBenchmarker.search(linearSearch, size);

        // Then
        System.out.println("BinaryRecursiveSearch execution time (" + size + "): " + (totalTime) + " ms.");
    }
}
