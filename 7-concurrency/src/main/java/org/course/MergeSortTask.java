package org.course;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RecursiveTask;

public class MergeSortTask<T extends Comparable<? super T>> extends RecursiveTask<List<T>> {
    private final List<T> list;

    public MergeSortTask(List<T> list) {
        this.list = list;
    }

    @Override
    protected List<T> compute() {
        if (list.size() > 1) {
            var left = new ArrayList<>(list.subList(0, list.size() / 2));
            var right = new ArrayList<>(list.subList(list.size() / 2, list.size()));

            var leftTask = new MergeSortTask<>(left);
            var rightTask = new MergeSortTask<>(right);

            leftTask.fork();

            merge(leftTask.join(), rightTask.compute());
        }
        return list;
    }

    private void merge(List<T> left, List<T> right) {
        int leftIndex = 0;
        int rightIndex = 0;

        while (left.size() > leftIndex && right.size() > rightIndex) {
            if (left.get(leftIndex).compareTo(right.get(rightIndex)) < 0) {
                list.set(leftIndex + rightIndex, left.get(leftIndex++));
            } else {
                list.set(rightIndex + leftIndex, right.get(rightIndex++));
            }
        }

        while (left.size() > leftIndex) {
            list.set(leftIndex + rightIndex, left.get(leftIndex++));
        }

        while (right.size() > rightIndex) {
            list.set(leftIndex + rightIndex, right.get(rightIndex++));
        }
    }
}
