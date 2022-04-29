package org.oka.effectivejava.search;

/**
 * Binary recursive-based search implementation.
 */
public class BinaryRecursiveSearch implements Search {
    /**
     * {@inheritDoc}
     */
    @Override
    public int search(final int[] array, final int query) {
        return this.binarySearch(array, 0, array.length - 1, query);
    }

    private int binarySearch(final int[] array, final int leftEnd, final int rightEnd, final int query) {
        if (leftEnd > rightEnd) {
            return -1;
        }
        int middle = (leftEnd + rightEnd) / 2;
        if (array[middle] == query) {
            return middle;
        } else if (query < array[middle]) {
            return this.binarySearch(array, leftEnd, middle - 1, query);
        } else {
            return this.binarySearch(array, middle + 1, rightEnd, query);
        }
    }
}
