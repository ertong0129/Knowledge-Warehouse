package test;

import com.alibaba.fastjson.JSON;

/**
 * 堆排序
 */
public class HeapSort {

    public static void main(String[] args) {
        int[] array = {12,1,41,414,346536,0,3453,113,111,111,22,33,41,24,1,41,42,1,0};
        //创建大顶堆
        for (int i = (array.length-1)/2; i >= 0; i--) {
            pushDown(array, array.length, i);
        }
        //排序
        for (int i = 0; i < array.length-1; i++) {
            //置换第一个（最大）与倒序位置的值
            int tmp = array[array.length-1-i];
            array[array.length-1-i] = array[0];
            array[0] = tmp;
            //然后重新构建大顶堆
            pushDown(array, array.length-1-i, 0);
        }
        System.out.println(JSON.toJSONString(array));
    }

    public static void pushDown(int[] array, int end, int index) {
        int originValue = array[index];
        int next = getLeftChild(index);
        for (; next < end; index = next, next = getLeftChild(index)) {
            if (next + 1 < end && array[next] < array[next + 1]) {
                next = next + 1;
            }
            if (array[next] < originValue) {
                break;
            }
            array[index] = array[next];
        }
        array[index] = originValue;
    }

    public static int getLeftChild(int father) {
        return father*2+1;
    }
}
