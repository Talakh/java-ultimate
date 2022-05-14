package org.course;

public class PrintFormattedTable {
    public static void main(String[] args) {
        String[] input = new String[]{"1", "2", "3", "x", "5", "6", "a", "porosiatko", "c", "10", "11", "12", "13", "14", "15", "16"};
        int[] size = new int[5];
        for (int i = 0; i < input.length; i++) {
            int index = i % 5;
            if (size[index] < input[i].length()) {
                size[index] = input[i].length();
            }
        }
        for (int i = 0; i < input.length; i++) {
            System.out.print(input[i] + " ".repeat(size[i % 5] - input[i].length() + 4));
            if ((i + 1) % 5 == 0) {
                System.out.println();
            }
        }
    }
}
