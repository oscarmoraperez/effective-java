package org.oka.effectivejava.search;

import org.junit.jupiter.api.Test;
import org.oka.effectivejava.RandomGenerator;

import static org.assertj.core.api.Assertions.assertThat;

public class LinearSearchTest {

    Search linearSearch = new LinearSearch();

    @Test
    public void shouldReturnMinus1() {
        // Given
        int[] array = RandomGenerator.generateSortedArray(5000);

        // When
        int result = linearSearch.search(array, 10000);

        // Then
        assertThat(result).isEqualTo(-1);
    }

    @Test
    public void shouldReturnIndex() {
        for (int i = 0; i < 5000; i++) {
            // Given
            int[] array = RandomGenerator.generateSortedArray(5000);

            // When
            int result = linearSearch.search(array, i);

            // Then
            assertThat(result).isEqualTo(i);
        }
    }
}
