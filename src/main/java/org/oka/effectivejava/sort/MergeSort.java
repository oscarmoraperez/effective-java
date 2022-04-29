package org.oka.effectivejava.sort;

import static java.util.Arrays.copyOfRange;

/**
 * Merge sort implementation.
 */
public final class MergeSort implements Sort {
    /**
     * {@inheritDoc}
     */
    @Override
    public void sort(final int[] array, final int size) {
        if (size < 2) {
            return;
        }
        int middlePoint = size / 2;
        int[] firstHalf = copyOfRange(array, 0, middlePoint);
        int[] secondHalf = copyOfRange(array, middlePoint, array.length);

        sort(firstHalf, middlePoint);
        sort(secondHalf, size - middlePoint);

        merge(array, firstHalf, secondHalf, middlePoint, size - middlePoint);
    }

    private void merge(final int[] array, final int[] firstHalf, final int[] secondHalf, final int leftSize, final int rightSize) {
        int i = 0;
        int j = 0;
        int k = 0;
        while (i < leftSize && j < rightSize) {
            if (firstHalf[i] < secondHalf[j]) {
                array[k++] = firstHalf[i++];
            } else {
                array[k++] = secondHalf[j++];
            }
        }
        while (i < leftSize) {
            array[k++] = firstHalf[i++];
        }
        while (j < rightSize) {
            array[k++] = secondHalf[j++];
        }
    }
}
