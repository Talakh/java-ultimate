package org.course;

import java.util.Arrays;
import java.util.concurrent.ForkJoinPool;

public class DemoApp {
    public static void main(String[] args) {
        var list = Arrays.asList(1, 5, 9, 2, 7, 4, 0, 3, 6, 10);

        var sorted = ForkJoinPool.commonPool().invoke(new MergeSortTask<>(list));

        System.out.println(sorted);
    }
}
