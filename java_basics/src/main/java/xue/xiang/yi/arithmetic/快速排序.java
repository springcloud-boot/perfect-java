package xue.xiang.yi.arithmetic;


import java.util.Random;

/**
 * @author : 薛向毅
 * @date : 16:25 2023/2/10
 */
public class 快速排序 {
    public static void main(String[] args) {
        快速排序 quickSort = new 快速排序();
        int[] needSortArr = new int[1000000];
        for (int i = 0; i < 1000000; i++) {
            needSortArr[i] = new Random().nextInt(1000000);
        }
        long l = System.currentTimeMillis();
        quickSort.sort(needSortArr, 0, needSortArr.length-1);
        System.out.println(System.currentTimeMillis() -l);
//        System.out.println(JSON.toJSONS+tring(needSortArr));
    }

    private void sort(int[] needSortArr, int left, int right) {
       while (left < right) {
           //先找到拆分点
           int pivot  = partition(needSortArr, left, right);

           //右边更短
           if (pivot - left > right - pivot) {
               sort(needSortArr, pivot+1, right);
               //剩余代拍区间为  [left, pivot - 1]
               right = pivot-1;
           } else {
               sort(needSortArr, left , pivot-1);
               //剩余代拍区间为  [pivot + 1, right]
               left  = pivot+1;
           }
       }
    }

    private int partition(int[] nums, int left, int right) {
        // 以 nums[left] 作为基准数
        int i = left, j = right;
        while (i < j) {
            while (i < j && nums[j] >= nums[left]) {
                j--;          // 从右向左找首个小于基准数的元素
            }
            while (i < j && nums[i] <= nums[left]) {
                i++;          // 从左向右找首个大于基准数的元素
            }
            swap(nums, i, j); // 交换这两个元素
        }
        swap(nums, i, left);  // 将基准数交换至两子数组的分界线
        return i;             // 返回基准数的索引
    }
    void swap(int[] nums, int i, int j) {
        int tmp = nums[i];
        nums[i] = nums[j];
        nums[j] = tmp;
    }
}
