package org.oka.effectivejava.sort;

import static org.oka.effectivejava.RandomGenerator.generateUnsortedArray;

/**
 * Runs benchmarking test (5) times to check and averages the required time on a Sorter feature.
 */
public class SortBenchmarker {
    private static final int TIMES = 5;

    double sort(final Sort sorter, final int arraySize) {
        long ping = System.currentTimeMillis();
        for (int j = 0; j < TIMES; j++) {
            int[] array = generateUnsortedArray(arraySize);
            sorter.sort(array, array.length);
        }
        long pong = System.currentTimeMillis();

        return  (double) (pong - ping) / TIMES;
    }
}
