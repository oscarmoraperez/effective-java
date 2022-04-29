package org.oka.effectivejava.sort;

public final class InsertionSort implements Sort {
    @Override
    public void sort(final int[] array, final int size) {
        for (int i = 1; i < size; i++) {

            int j = i - 1;
            int current = array[i];

            while (j >= 0 && array[j] > current) {
                array[j + 1] = array[j];
                j--;
            }
            array[j + 1] = current;
        }
    }
}
