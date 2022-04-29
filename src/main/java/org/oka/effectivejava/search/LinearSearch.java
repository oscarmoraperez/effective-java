package org.oka.effectivejava.search;

/**
 * Linear search implementation.
 */
public class LinearSearch implements Search {
    /**
     * {@inheritDoc}
     */
    @Override
    public int search(final int[] array, final int query) {
        for (int i = 0; i < array.length; i++) {
            if (array[i] == query) {
                return i;
            }
        }
        return -1;
    }
}
