package xue.xiang.yi.arithmetic;

import com.alibaba.fastjson.JSON;

import java.util.Random;

public class 归并排序1手写联系1 {
    public static void main(String[] args) {
        归并排序1手写联系1 mersort = new 归并排序1手写联系1();
        int[] needSortArr = new int[200];
        for (int i = 0; i < 200; i++) {
            needSortArr[i] = new Random().nextInt(200);
        }
        long l = System.currentTimeMillis();
        mersort.mergeSort(needSortArr, 0 , needSortArr.length-1);
        System.out.println(System.currentTimeMillis() -l);
        System.out.println(JSON.toJSONString(needSortArr));
    }

    private void mergeSort(int[] needSortArr, Integer startIndex, Integer endIndex) {
       if (startIndex >= endIndex) {
           return;
       }

       int middleIndex = (startIndex + endIndex) / 2;

       mergeSort(needSortArr, startIndex, middleIndex);
       mergeSort(needSortArr, middleIndex + 1, endIndex);

       merge1(needSortArr, startIndex,middleIndex, endIndex);
        return ;
    }

    private void merge1(int[] needSortArr, Integer startIndex, int middleIndex, Integer endIndex) {
        int leftIndex = startIndex, rightIndex = middleIndex + 1, tempIndex = 0;
        //存储这次排序的数组
        int[] tmp = new int[endIndex - startIndex + 1];

        while (leftIndex <= middleIndex && rightIndex <= endIndex) {
            if (needSortArr[leftIndex] < needSortArr[rightIndex]) {
                tmp[tempIndex++] = needSortArr[leftIndex++];
            } else {
                tmp[tempIndex++] = needSortArr[rightIndex++];
            }
        }
        while (leftIndex <= middleIndex) {
            tmp[tempIndex++] = needSortArr[leftIndex++];
        }
        while (rightIndex <= endIndex) {
            tmp[tempIndex++] = needSortArr[rightIndex++];
        }

        for (int i = 0; i < tmp.length; i++) {
            needSortArr[startIndex++] = tmp[i];
        }

    }

    private void merge(int[] needSortArr, Integer startIndex, int middleIndex, Integer endIndex) {
        //分隔后,二个数组的起始位置, 临时数组的起始位置
        int startTemp = startIndex, endTemp = middleIndex+1, tempArrIndex=0;

        //搞一个临时数组,存一下这次的数组
        int[] tmp = new int[endIndex-startIndex + 1];
        while (startTemp <= middleIndex && endTemp <= endIndex) {
            //比大小啊,谁小放谁
            if (needSortArr[startTemp] < needSortArr[endTemp]) {
                tmp[tempArrIndex++] = needSortArr[startTemp++];
            } else {
                tmp[tempArrIndex++] = needSortArr[endTemp++];
            }
        }

        //左边有剩余?
        while (startTemp <= middleIndex) {
            tmp[tempArrIndex++] = needSortArr[startTemp++];
        }
        //右边有剩余?
        while (endTemp <= endIndex) {
            tmp[tempArrIndex++] = needSortArr[endTemp++];
        }

        //替换
        for (int i : tmp) {
            needSortArr[startIndex++]= i;
        }

    }
}
