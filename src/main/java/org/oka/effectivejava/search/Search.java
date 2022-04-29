package org.oka.effectivejava.search;

/**
 * Generic interface to define the search operation.
 */
public interface Search {
    /**
     * Return the index of the 'query' paramenter in the array. -1 if it does not exist.
     *
     * @param array Array where to perform the search.
     * @param query Number to search in the array
     * @return index of the query parameter in the input array
     */
    int search(int[] array, int query);
}
