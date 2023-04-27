package xue.xiang.yi.sword.offer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 给你一个整数数组 nums ，判断是否存在三元组 [nums[i], nums[j], nums[k]] 满足 i != j、i != k 且 j != k ，同时还满足 nums[i] + nums[j] + nums[k] == 0 。请
 *
 * 你返回所有和为 0 且不重复的三元组。
 *
 * 注意：答案中不可以包含重复的三元组。
 *
 输入：nums = [-1,0,1,2,-1,-4]
 输出：[[-1,-1,2],[-1,0,1]]
 解释：
 nums[0] + nums[1] + nums[2] = (-1) + 0 + 1 = 0 。
 nums[1] + nums[2] + nums[4] = 0 + 1 + (-1) = 0 。
 nums[0] + nums[3] + nums[4] = (-1) + 2 + (-1) = 0 。
 不同的三元组是 [-1,0,1] 和 [-1,-1,2] 。
 注意，输出的顺序和三元组的顺序并不重要。

 */
public class 三个数字之和 {

    public static void main(String[] args) {
        三个数字之和 三个数字之和 = new 三个数字之和();
        三个数字之和.threeSum(new int[]{1,2,-2,-1});
    }
    public List<List<Integer>> threeSum(int[] nums) {
        //当前数组的长度为空，或者长度小于3时，直接退出
        if(nums == null || nums.length <3){
            return null;
        }
        List<List<Integer>> result = new ArrayList<>();
        Arrays.sort(nums);

        //3个指针.  左指针为主. 找到中间指针和右指针有满足的
        for (int left = 0; left < nums.length; left++) {
            if (nums[left] > 0) {
                break;
            }
            //去重.  左指针和上个元素一样. 就算有结果也重复
            if (left > 0 && nums[left] == nums[left -1]) {
                continue;
            }
            //第二个开始找
            int middle = left++;
            //最右边指针下标
            int right = nums.length - 1;
            while (middle < right) {
                int itemSum = nums[left] + nums[middle] + nums[right];
                if (itemSum == 0) {
                    result.add(Arrays.asList(nums[left], nums[middle], nums[right]));

                    //中间指针取的数的值与前一个数相同; 前俩数字,后指针更一样了
                    while(middle < right && nums[middle] == nums[middle+1]) {
                        middle++;
                    }
                    //去重. 前一个,后一个数字一样.中间还算个啥
                    while(middle < right && nums[right] == nums[right-1]){
                        right--;
                    }
                    middle++;
                    right--;
                } else if (itemSum < 0) {
                    middle++;
                } else {
                    right--;
                }
            }

        }
        return result;
    }
}
