package algorithm;

/**
 * 交换排序
 */
public class ChangeSort {

    /**
     * 排序入口
     * @param array
     */
    public static void sort(int[] array) {
        for (int i = 0; i < array.length-1; i++) {
            for (int j = 0; j < array.length-1-i; j++) {
                if (array[j] > array[j+1]) {
                    exchange(array, j, j+1);
                }
            }
        }
    }

    public static void exchange(int[] array, int a, int b) {
        int tmp = array[a];
        array[a] = array[b];
        array[b] = tmp;
    }
}
