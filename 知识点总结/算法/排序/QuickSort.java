package algorithm;

public class QuickSort {

    /**
     * 排序入口
     * @param array
     */
    public static void sort(int[] array) {
        sort(array, 0, array.length-1);
    }

    /**
     * 排序
     * @param array
     * @param begin
     * @param end
     */
    public static void sort(int[] array, int begin, int end) {
        if (end-begin >= 3) {
            //分组数量大于n用快排
            int flag = caculate(array, begin, end);
            int i = 0, j = end-1;
            for(;;) {
               while(array[++i] < flag);
               while(array[--j] > flag);
               if (i >= j) {
                   //已经交换完比flag小的和比flag大的数的位置了
                   break;
               } else {
                   //将比flag小的值和比flag大的值交换位置
                   exchange(array, i, j);
               }
            }
            //将flag与从左往右数第一个比它大的数交换位置
            exchange(array, i, end-1);
            //对左右分别排序
            sort(array, begin, i-1);
            sort(array, i+1, end);
        } else {
            //小于n用冒泡
            bubbleSort(array, begin, end);
        }
    }

    /**
     * 交换数组中两数位置
     * @param array
     * @param a
     * @param b
     */
    public static void exchange(int[] array, int a, int b) {
        int tmp = array[a];
        array[a] = array[b];
        array[b] = tmp;
    }

    /**
     * 对头、中、尾三个位置的数进行排序，然后交换中、尾-1两个位置的值，然后返回尾-1位置数的值
     * @param array
     * @param begin
     * @param end
     */
    public static int caculate(int[] array, int begin, int end) {
        int center = (begin+end) / 2;
        if (array[begin] > array[center]) {
            exchange(array, begin, center);
        }
        if (array[center] > array[end]) {
            exchange(array, center, end);
        }
        if (array[begin] > array[center]) {
            exchange(array, begin, center);
        }
        exchange(array, center, end-1);
        return array[end-1];
    }

    /**
     * 冒泡排序
     * @param array
     * @param begin
     * @param end 此处end是最后一个值的下标
     */
    public static void bubbleSort(int[] array, int begin, int end) {
        for (int i = begin+1; i <= end; i++) {
            int originValue = array[i];
            int j = i-1;
            for (; j >= begin; j--) {
                if (array[j] > originValue) {
                    array[j+1] = array[j];
                } else {
                    break;
                }
            }
            array[j+1] = originValue;
        }
    }
}
