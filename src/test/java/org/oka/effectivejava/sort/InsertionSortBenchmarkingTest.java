package org.oka.effectivejava.sort;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class InsertionSortBenchmarkingTest {
    SortBenchmarker sortBenchmarker = new SortBenchmarker();

    @ParameterizedTest
    @ValueSource(ints = {10_000, 20_000, 30_000, 40_000, 50_000, 60_000, 70_000, 80_000, 90_000, 100_000})
    public void shouldSort(final int size) {
        // Given
        Sort sorter = new InsertionSort();

        // When
        double totalTime = sortBenchmarker.sort(sorter, size);

        // Then
        System.out.println("InsertionSort execution time (" + size + "): " + (totalTime) + " ms.");
    }
}
