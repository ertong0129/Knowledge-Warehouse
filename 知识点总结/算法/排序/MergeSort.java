package algorithm;

/**
 * 归并排序
 */
public class MergeSort {

    /**
     * 排序入口
     * @param array
     */
    public static void sort(int[] array) {
        sort(array, 0, array.length-1);
    }

    public static void sort(int[] array, int begin, int end) {
        if (begin == end) {
            return;
        }
        int center = (begin+end) / 2;
        sort(array, begin, center);
        sort(array, center+1, end);
        int[] tmpArray = new int[end-begin+1];
        int beginIndex = begin, centerIndex = center+1, tmpIndex = 0;
        //归并排序，对数组分为两部分，不断取最小值塞入临时数组
        while(beginIndex <= center && centerIndex <= end) {
            if (array[beginIndex] < array[centerIndex]) {
                tmpArray[tmpIndex++] = array[beginIndex++];
            } else {
                tmpArray[tmpIndex++] = array[centerIndex++];
            }
        }
        //数组的一部分已被填完，将另一部分将剩下的值填入临时数组
        for (;beginIndex <= center;) {
            tmpArray[tmpIndex++] = array[beginIndex++];
        }
        for (;centerIndex <= end;) {
            tmpArray[tmpIndex++] = array[centerIndex++];
        }
        //将临时数组回填到原数组
        for (int i = 0; i < tmpArray.length; i++) {
            array[i+begin] = tmpArray[i];
        }
    }

}
