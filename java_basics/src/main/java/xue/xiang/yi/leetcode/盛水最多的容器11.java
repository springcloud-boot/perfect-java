package xue.xiang.yi.leetcode;

import java.util.Arrays;

/**
 * @author : 薛向毅
 * @date : 14:53 2023/2/10
 */
public class 盛水最多的容器11 {

    public static void main(String[] args) {
        盛水最多的容器11 aa = new 盛水最多的容器11();
        int i = aa.maxArea(new int[]{1,8,6,2,5,4,8,3,7});
        System.out.println(i);
    }

    public int maxArea(int[] height) {
        int max = 0;
        int leftIndex = 0;
        int rightIndex = height.length-1;
        while (leftIndex < rightIndex) {
            //差距
            int distance = rightIndex - leftIndex;
            //差距高度
            int abs = Math.min(height[leftIndex] , height[rightIndex]);
            //成水量
            int capacity = distance * abs;

            max = Math.max(max, capacity);
            //左边高度小 往右找更高的
            if (height[leftIndex] <= height[rightIndex]) {
                leftIndex++;
            } else {
                rightIndex--;
            }
        }
        return max;
    }
}
