package org.oka.effectivejava.sort;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.oka.effectivejava.RandomGenerator.generateUnsortedArray;

public class MergeSortTest {
    @Test
    public void shouldSort() {
        // Given
        Sort sorter = new MergeSort();
        int[] array = generateUnsortedArray(5000);

        // When
        sorter.sort(array, array.length);

        // Then
        assertThat(array).isSorted();
    }
}
