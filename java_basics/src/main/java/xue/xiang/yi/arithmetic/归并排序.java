package xue.xiang.yi.arithmetic;

import com.alibaba.fastjson.JSON;

import java.util.Arrays;

/**
 * @author : 薛向毅
 * @date : 16:05 2022/9/14
 */
public class 归并排序 {
    public static void main(String[] args) {
        归并排序 mersort = new 归并排序();
        int[] needSortArr = {51, 45325, 765, 213, 765, 89, 3, 34, 7567, 3, 65, 2312, 7678, 786, 213};
        int[] ints = mersort.mergeSort(needSortArr);
        System.out.println(JSON.toJSONString(ints));
    }

    //分而治之
    public int[] mergeSort(int[] arr) {

         merge_sort(arr, 0, arr.length - 1);
        return arr;
    }


    private void merge_sort(int[] arr, int startIndex, int endIndex) {
        //起始位置大于结束位置. 无法拆分了直接返回呗
        if (startIndex >= endIndex) {
            return ;
        }
        //拿到拆分后的中间位置
        int middleIndex = (startIndex + endIndex) / 2;
        //左边仍然能拆分
        merge_sort(arr, startIndex, middleIndex);
        //右边也能拆分.  左边包含了middleIndex了.这里不要用
        merge_sort(arr, middleIndex +1, endIndex);

        merge(arr, startIndex,middleIndex, endIndex);
        return ;
    }

    private void merge(int[] arr, int startIndex, int middleIndex, int endIndex) {
        //分隔后,二个数组的起始位置, 临时数组的起始位置
        int startTemp = startIndex, endTemp = middleIndex+1, tempArrIndex=0;

        //搞一个临时数组,存一下这次的数组
        int[] tmp = new int[endIndex-startIndex + 1];
        while (startTemp <= middleIndex && endTemp <= endIndex) {
            //比大小啊,谁小放谁
            if (arr[startTemp] < arr[endTemp]) {
                tmp[tempArrIndex++] = arr[startTemp++];
            } else {
                tmp[tempArrIndex++] = arr[endTemp++];
            }
        }


        //左边有剩余?
        while (startTemp <= middleIndex) {
            tmp[tempArrIndex++] = arr[startTemp++];
        }
        //右边有剩余?
        while (endTemp <= endIndex) {
            tmp[tempArrIndex++] = arr[endTemp++];
        }

        //替换
        for (int i : tmp) {
            arr[startIndex++]= i;
        }
    }
}
