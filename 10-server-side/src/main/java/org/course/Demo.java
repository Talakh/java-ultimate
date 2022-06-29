package org.course;

public class Demo {
    public static void main(String[] args) {
        merge(new int[]{4,5,6,0,0,0}, 3, new int[]{1,2,3}, 3);
    }

    public static void merge(int[] nums1, int m, int[] nums2, int n) {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m + i; j++) {
                if (nums1[j] > nums2[i]) {
                    System.arraycopy(nums1, j, nums1, j + 1, m + i - j);
                    nums1[j] = nums2[i];
                    break;
                }
            }
            if (nums1[m + i] == 0) {
                nums1[m + i] = nums2[i];
            }
        }
    }
}
