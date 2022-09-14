package xue.xiang.yi.leetcode;

/**
 * @author : 薛向毅
 * @date : 9:31 2022/9/14
 */
public class 一维数组的动态和1480 {

    class Solution {
        public int[] runningSum(int[] nums) {
            if (null == nums) {
                return null;
            }
            if (nums.length == 1) {
                return nums;
            }
            int temp = 0;
            for (int i = 0; i < nums.length; i++) {
                temp += nums[i];
                nums[i] = temp;
            }
            return nums;
        }

        //官方
        public int[] authority(int[] nums) {
            int n = nums.length;
            for (int i = 1; i < n; i++) {
                nums[i] += nums[i - 1];
            }
            return nums;
        }


    }
}
