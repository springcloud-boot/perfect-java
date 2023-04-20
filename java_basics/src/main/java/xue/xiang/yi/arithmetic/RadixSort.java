package xue.xiang.yi.arithmetic;

import java.util.Arrays;

public class RadixSort {

    public static void main(String[] args) {
        // 待排序的手机号数组
        String[] nums = {"13812345678", "13512345678", "13712345678", "13912345678", "13612345678", "18012345678"};
        // 执行基数排序
        radixSort(nums);
        // 输出排序结果
        System.out.println(Arrays.toString(nums));
    }

    /**
     * 基数排序，将手机号数组从小到大排序
     *
     * @param nums 待排序的手机号数组
     */
    public static void radixSort(String[] nums) {
        // 获取手机号码的最大长度
        int maxLength = getMaxLenth(nums);
        // 创建10个桶，分别存放0-9的数字
        String[][] bucket = new String[10][nums.length];
        // 记录每个桶存放的元素个数
        int[] count = new int[10];

        // 从个位到最高位依次进行排序
        for (int i = 0; i < maxLength; i++) {
            // 将手机号码的每一位取出来放到相应的桶中
            for (String num : nums) {
                // 获取当前手机号码第i+1位上的数字
                int digit = getDigit(num, i);
                // 将手机号码放到对应的桶中
                bucket[digit][count[digit]] = num;
                // 桶中元素个数加1
                count[digit]++;
            }
            // 将桶中的元素依次取出放回原数组中
            int index = 0;
            for (int j = 0; j < count.length; j++) {
                if (count[j] != 0) { // 如果桶中有元素
                    for (int k = 0; k < count[j]; k++) {
                        nums[index] = bucket[j][k];
                        bucket[j][k] = null;
                        index++;
                    }
                    // 清空桶中元素个数
                    count[j] = 0;
                }
            }
        }
    }

    /**
     * 获取手机号码的最大长度
     *
     * @param nums 待排序的手机号数组
     * @return 手机号码的最大长度
     */
    public static int getMaxLenth(String[] nums) {
        int maxLength = 0;
        for (String num : nums) {
            maxLength = Math.max(maxLength, num.length());
        }
        return maxLength;
    }

    /**
     * 获取手机号码第i+1位上的数字
     *
     * @param num 手机号码
     * @param i   位数，从右往左，从0开始计数
     * @return 手机号码第i+1位上的数字
     */
    public static int getDigit(String num, int i) {
        if (num.length() < i + 1) { // 如果位数大于手机号码长度，则返回0
            return 0;
        }
        // 将字符转为数字
        char c = num.charAt(num.length() - i - 1);
        return c - '0';
    }
}
