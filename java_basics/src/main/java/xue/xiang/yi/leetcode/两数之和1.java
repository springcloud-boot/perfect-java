package xue.xiang.yi.leetcode;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author : 薛向毅
 * @date : 9:46 2023/1/30
 */
public class 两数之和1 {
    public int[] twoSum(int[] nums, int target) {
        int[] result = new int[2];
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            int temp = target - nums[i];
            if (Objects.nonNull(map.get(temp))) {
                result[0] = map.get(temp);
                result[1] = i;
                return result;
            }
            map.put(nums[i], i);
        }
        return null;

    }
}
