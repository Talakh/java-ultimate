package org.course;

public class Main {
    public static void main(String[] args) {
        HeterogeneousMaxHolder maxHolder = new HeterogeneousMaxHolder();
        maxHolder.put(Integer.class, 1);
        maxHolder.put(Integer.class, 3);
        maxHolder.put(Integer.class, 2);
        maxHolder.put(String.class, "C");
        maxHolder.put(String.class, "B");
        maxHolder.put(String.class, "A");
        System.out.println(maxHolder.getMax(Integer.class));
        System.out.println(maxHolder.getMax(String.class));
    }
}