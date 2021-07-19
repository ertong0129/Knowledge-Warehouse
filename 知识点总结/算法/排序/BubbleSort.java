package algorithm;

public class BubbleSort {

    /**
     * 冒泡排序入口
     * @param array
     */
    public static void sort(int[] array) {
        sort(array, 0, array.length-1);
    }

    /**
     * 冒泡排序
     * @param array
     * @param begin
     * @param end
     */
    public static void sort(int[] array, int begin, int end) {
        for (int i = begin+1; i <= end; i++) {
            int originValue = array[i];
            int j = i-1;
            for (; j >= begin; j--) {
                if (array[j] > originValue) {
                    //当前位置值比原值大，当前值后移一位
                    array[j+1] = array[j];
                } else {
                    //当前位置值不比原值大，退出循环
                    break;
                }
            }
            //将最后一个比原值大的位置，设为原值
            array[j+1] = originValue;
        }
    }
}
