package org.oka.effectivejava;

import java.util.Random;

/**
 * Generates test arrays.
 */
public class RandomGenerator {
    /**
     * Generates a sorted array.
     *
     * @param size Size of the desired array
     * @return array of int[]
     */
    public static int[] generateSortedArray(int size) {
        int[] result = new int[size];
        for (int i = 0; i < size; i++) {
            result[i] = i;
        }
        return result;
    }

    /**
     * Generates an unsorted array.
     *
     * @param size Size of the desired array
     * @return array of int[]
     */
    public static int[] generateUnsortedArray(int size) {
        int[] result = new int[size];
        Random random = new Random();
        for (int i = 0; i < size; i++) {
            result[i] = random.nextInt();
        }
        return result;
    }
}
