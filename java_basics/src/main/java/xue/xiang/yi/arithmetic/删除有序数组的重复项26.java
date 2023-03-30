package xue.xiang.yi.arithmetic;

/**
 * @author : 薛向毅
 * @date : 13:49 2023/2/14
 */
public class 删除有序数组的重复项26 {
    public static void main(String[] args) {
        int i = removeDuplicates(new int[]{0,0,1,1,1,2,2,3,3,4});
        System.out.println(i);
    }
    public static int removeDuplicates(int[] nums) {
        if (nums == null || nums.length ==0) {
            return 0;
        }
        if(nums == null || nums.length == 0) {
            return 0;
        }
        int p = 0;
        int q = 1;
        while(q < nums.length){
            if(nums[p] != nums[q]){
                if(q - p > 1){
                    nums[p + 1] = nums[q];
                }
                p++;
            }
            q++;
        }
        return p + 1;

    }
}
