package Sort;

/**
 * @Author: listeningrain
 * @Date: 2019-01-29 15:48
 * @Description: 插入排序
 */
public class InsertSort {

    //接收两个参数：数组和数组的长度
    public static Boolean insertSort(int[] a, int n){
        if(n == 0 || 0 == a.length){
            return false;
        }

        //初始排好序区只有一个元素a[0]
        for(int i=1; i<n; i++){
            int temp = a[i];
            int j;
            //在排好序区+本次需要插入的元素之间进行比较交换
            for( j=i-1; j>=0; j--){
                if(a[j]>temp){
                    a[j+1]=a[j];
                }else{
                    break;
                }
            }
           a[j+1] = temp;
        }
        return true;
    }

    public static void main(String[] args){
        int[] sort = {5,6,2,3,1,3463,34,342,79};

        System.out.print("未排序：");
        for (int i=0; i<sort.length; i++){
            System.out.print(sort[i]+" ");
        }

        insertSort(sort,sort.length);
        System.out.println();

        System.out.print("排序后：");
        for (int i=0; i<sort.length; i++){
            System.out.print(sort[i]+" ");
        }
    }

}
