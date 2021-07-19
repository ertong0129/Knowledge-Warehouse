package test;

import com.alibaba.fastjson.JSON;

import java.util.Arrays;
import java.util.Random;

/**
 * 二分算法
 */
public class BinaryFind {

    public static void main(String[] args) {
        int key = 555;
        int[] array = new int[1000];
        Random random = new Random();
        for (int i = 0; i < array.length; i++) {
            array[i] = random.nextInt(1000);
        }
        Arrays.sort(array);

        System.out.println(JSON.toJSONString(array));

        int begin = 0, end = array.length, center = 0;
        while (begin != end) {
            center = (begin+end)/2;
            if (array[center] > key) {
                end = center;
            } else if (array[center] < key) {
                begin = center + 1;
            } else {
                System.out.println(center);
                break;
            }
        }
    }
}
