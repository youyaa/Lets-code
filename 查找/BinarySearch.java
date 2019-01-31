package Finder;

/**
 * @Author: listeningrain
 * @Date: 2019-01-30 15:48
 * @Description: 二分查找(在没有重复元素的数组中查找指定元素)
 */
public class BinarySearch {

    public static int binarySearch(int[] a, int n, int value){
        if(n == 0 || 0>= a.length){
            return -1;
        }

        System.out.println("本次需要查找的数组的长度为："+n);

        int low=0;
        int high=n-1;
        int i=1;

        for(;high>=low;){
            int middle = (low+high)/2;
            System.out.println("第"+i+"次循环："+"本次判断的a["+middle+"]是"+a[middle]);
            if(a[middle]==value){
                return middle;
            }
            if(a[middle]>value){
                high= middle-1;
            }else {
                low = middle+1;
            }
            i++;
        }
        //不存在返回-1
        return -1;
    }

    public static void main(String[] args){
        int[] sort = new int[1000];
        for(int i=0,j=1;i<1000;i++,j++){
            sort[i]=j;
        }

        int i = binarySearch(sort, sort.length, 1000);

        System.out.println("二分查找返回的下标值："+i);
    }
}
