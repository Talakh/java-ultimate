package org.course.sort;

import java.util.ArrayList;
import java.util.List;

public class MergeSort {
    public static void main(String[] args) {
        System.out.println(mergeSort(new ArrayList<>(List.of(3, 8, 5, 7, 6, 10, 5, 4, 3, 2, 0, 1))));
    }

    public static <T extends Comparable<? super T>> List<T> mergeSort(List<T> list) {
        if (list.size() > 1) {
            List<T> left = mergeSort(new ArrayList<>(list.subList(0, list.size() / 2)));
            List<T> right = mergeSort(new ArrayList<>(list.subList(list.size() / 2, list.size())));

            merge(list, left, right);
        }
        return list;
    }

    private static <T extends Comparable<? super T>> void merge(List<T> list, List<T> left, List<T> right) {
        int leftIndex = 0;
        int rightIndex = 0;
        while (leftIndex < left.size() && rightIndex < right.size()) {
            if (left.get(leftIndex).compareTo(right.get(rightIndex)) <= 0) {
                list.set(leftIndex + rightIndex, left.get(leftIndex++));
            } else {
                list.set(leftIndex + rightIndex, right.get(rightIndex++));
            }
        }

        while (leftIndex < left.size()) {
            list.set(leftIndex + rightIndex, left.get(leftIndex++));
        }

        while (rightIndex < right.size()) {
            list.set(leftIndex + rightIndex, right.get(rightIndex++));
        }
    }
}
