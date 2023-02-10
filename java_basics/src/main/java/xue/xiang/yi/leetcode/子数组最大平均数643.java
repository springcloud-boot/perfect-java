package xue.xiang.yi.leetcode;

/**
 * @author : 薛向毅
 * @date : 13:30 2023/1/31
 */
public class 子数组最大平均数643 {
    public static void main(String[] args) {
        int[] ints = {-1};
        double maxAverage = findMaxAverage(ints, 1);
        System.out.println(maxAverage);
    }

    public static double findMaxAverage(int[] nums, int k) {
        int[] preSum = new int[nums.length+1];
        for (int i = 0; i < nums.length; i++) {
            preSum[i+1] = preSum[i] + nums[i];
        }

         double res=Integer.MIN_VALUE;
        //找出前缀和最大的区间; preSum[i+k] - preSum[i] 最大
        for (int i = 0; i < preSum.length - k ; i++) {
            int tempPreSum = preSum[i+k] - preSum[i];
            if (tempPreSum > res) {
                res = tempPreSum;
            }
        }

        return res/k;

    }
}
