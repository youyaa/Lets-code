package Sort;

/**
 * @Author: listeningrain
 * @Date: 2019-01-29 16:36
 * @Description: 选择排序
 */
public class SelectionSort {
    public static Boolean selectionSort(int[] a, int n){
        if(n == 0 || 0 == a.length){
            return false;
        }

        for(int i=0; i<n; i++){
            int temp = a[i];
            int smallestIndex=-1;
            int small = a[i];
            //寻找最小的元素的位置
            for(int j=i; j<n; j++){
                if(a[j]<small){
                    small = a[j];
                    smallestIndex = j;
                }
            }

            if(-1 != smallestIndex){
                a[i] = a[smallestIndex];
                a[smallestIndex] = temp;
            }
        }
        return true;
    }

    public static void main(String[] args){
        int[] sort = {5,6,2,3,1,3463,34,342,79,4567,44};

        System.out.print("未排序：");
        for (int i=0; i<sort.length; i++){
            System.out.print(sort[i]+" ");
        }

        selectionSort(sort,sort.length);
        System.out.println();

        System.out.print("排序后：");
        for (int i=0; i<sort.length; i++){
            System.out.print(sort[i]+" ");
        }
    }
}
