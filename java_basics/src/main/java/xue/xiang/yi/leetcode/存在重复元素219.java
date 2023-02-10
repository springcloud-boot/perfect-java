package xue.xiang.yi.leetcode;

import com.alibaba.fastjson.JSON;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author : 薛向毅
 * @date : 16:09 2023/2/1
 */
public class 存在重复元素219 {

    public static void main(String[] args) throws ParseException {
        ArrayList<Object> objects = new ArrayList<>();
        objects.add("1");
        objects.add("2");
        objects.add("3");
        List<Object> objects1 = objects.subList(0, objects.size() - 1);
        System.out.println(JSON.toJSONString(objects1));
    }

    /**
     * 给你一个整数数组 nums 和一个整数 k ，判断数组中是否存在两个 不同的索引 i 和 j ，满足 nums[i] == nums[j] 且 abs(i - j) <= k 。如果存在，返回 true ；否则，返回 false 。
     *
     * 来源：力扣（LeetCode）
     * 链接：https://leetcode.cn/problems/contains-duplicate-ii
     * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
     * @param nums
     * @param k
     * @return
     */
    public boolean containsNearbyDuplicate(int[] nums, int k) {
        //保证abs(i-j) <= k;则需要保证下标范围 (k+1)内. 则 (i-j) <= k
        //使用set集合,存储 (k+1)下标内的所有元素. 然后通过滑动来判断是否有满足
        HashSet<Integer> set = new HashSet<>();
        for (int i = 0; i < nums.length; i++) {
            //超出范围了 k=2;  i=2;  set中有3个元素(0,1,2)下标  仍然符合题意 当i=3时不符合
            if (i > k) {
                //滑出超出范围的元素 假设k=2, i=3. 0,1,2,3  移除0下标. i=3,k=2. 3-2-1
                set.remove(nums[i-k-1]);
            }


            //集合内有存在的元素.则返回
            if (set.contains(nums[i])) {
                return true;
            }
            //添加当前元素
            set.add(nums[i]);


        }
        return false;
    }

}
