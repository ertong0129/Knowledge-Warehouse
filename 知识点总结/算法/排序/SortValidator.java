package test;

import com.alibaba.fastjson.JSON;

import java.util.Random;

/**
 * 算法是否正确 校验器
 */
public class SortValidator {

    public static void main(String[] args) {
        Random random = new Random();
        int[] array = new int[random.nextInt(1000)];
        for (int i = 0; i < array.length; i++) {
            array[i] = random.nextInt(10000);
        }

        System.out.println(JSON.toJSONString(array));

        for (int i = 1; i < array.length; i++) {
            if (array[i] < array[i-1]) {
                throw new RuntimeException("算法错误");
            }
        }
    }

}
