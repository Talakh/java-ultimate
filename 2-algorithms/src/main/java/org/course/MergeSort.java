package org.course;

import java.util.Arrays;

import static java.util.Arrays.copyOfRange;

public class MergeSort {
    public static void main(String[] args) {
        int[] arr = new int[]{2, 7, 3, 8, 21, 11, 10, 9, 5, 4, 1};
        System.out.println(Arrays.toString(mergeSort(arr)));
    }

    public static int[] mergeSort(int[] arr) {
        if (arr.length == 1) {
            return arr;
        }

        int[] left = mergeSort(copyOfRange(arr, 0, arr.length / 2));
        int[] right = mergeSort(copyOfRange(arr, arr.length / 2, arr.length));

        return merge(arr, left, right);
    }

    private static int[] merge(int[] src, int[] left, int[] right) {
        int leftIndex = 0;
        int rightIndex = 0;
        int scrIndex = 0;

        while (leftIndex < left.length || rightIndex < right.length) {
            if (leftIndex == left.length) {
                src[scrIndex] = right[rightIndex++];
            } else if (rightIndex == right.length) {
                src[scrIndex] = left[leftIndex++];
            } else if (right[rightIndex] < left[leftIndex]) {
                src[scrIndex] = right[rightIndex++];
            } else {
                src[scrIndex] = left[leftIndex++];
            }
            scrIndex++;
        }

        return src;
    }
}
