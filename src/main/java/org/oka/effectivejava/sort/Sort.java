package org.oka.effectivejava.sort;

/**
 * Generic interface to define the sort operation.
 */
public interface Sort {
    /**
     * Sorts the input array.
     *
     * @param array Array where to perform the search.
     * @param size  Size of the array.
     */
    void sort(int[] array, int size);
}
