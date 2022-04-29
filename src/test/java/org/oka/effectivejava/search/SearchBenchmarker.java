package org.oka.effectivejava.search;

import static org.oka.effectivejava.RandomGenerator.generateSortedArray;

/**
 * Runs benchmarking test (5) times to check and averages the required time on a Search feature.
 */
public class SearchBenchmarker {
    private static final int TIMES = 5;

    double search(final Search search, final int arraySize) {
        int[] array = generateSortedArray(arraySize);

        long totalTime = 0;
        for (int i = 0; i < arraySize; i++) {
            long ping = System.currentTimeMillis();
            for (int j = 0; j < TIMES; j++) {
                search.search(array, i);
            }
            long pong = System.currentTimeMillis();
            totalTime = totalTime + (pong - ping);
        }
        return (double) totalTime / TIMES;
    }
}
