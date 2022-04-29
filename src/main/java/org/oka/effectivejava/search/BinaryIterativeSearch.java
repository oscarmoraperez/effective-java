package org.oka.effectivejava.search;

/**
 * Binary iterative-based search implementation.
 */
public class BinaryIterativeSearch implements Search {
    /**
     * {@inheritDoc}
     */
    @Override
    public int search(final int[] array, final int query) {
        int leftEnd = 0;
        int rightEnd = array.length - 1;
        while (leftEnd <= rightEnd) {
            int middle = (leftEnd + rightEnd) / 2;
            if (array[middle] == query) {
                return middle;
            } else if (query < array[middle]) {
                rightEnd = middle - 1;
            } else {
                leftEnd = middle + 1;
            }
        }
        return -1;
    }
}
