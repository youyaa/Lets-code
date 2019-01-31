package Finder;

/**
 * @Author: listeningrain
 * @Date: 2019-01-30 15:48
 * @Description: 二分查找变体一：数组中存在重复的元素，查找第一个等于给定值value的元素下标
 */
public class BinarySearch_findFirst {

    public static int bsearch(int[] a, int n, int value) {
        int low=0,high=n-1;
        while (low<=high){
            int middle = low + ((high-low)>>2);
            if(a[middle]>value){
                high = middle-1;
            }else if(a[middle]<value){
                low = middle+1;
            }else{
                /**
                 *    当a[middle]==value时，先判断middle是不是等于0，若是肯定是我们要找的。
                 *    若不是再判断其前一个元素是不是等于value，若不等于，则说明也是我们要找的。
                 *    若等于，则说明，a[middle]不是第一个的指定值，则肯定在low和middle之间，则再令high=middle-1。
                 */
                if(middle==0 || a[middle-1]!=value){
                    return middle;
                }else {
                    high=middle-1;
                }
            }
        }

        return -1;
    }



    public static void main(String[] args){
        int[] sort = {1,3,4,5,5,5,7};


        int i = bsearch(sort, sort.length, 5);

        System.out.println("二分查找返回的下标值："+i);
    }
}
