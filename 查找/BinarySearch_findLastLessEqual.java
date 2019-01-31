package Finder;

/**
 * @Author: listeningrain
 * @Date: 2019-01-30 15:48
 * @Description: 二分查找变体四：数组中存在重复的元素，查找最后小于或等于给定值value的元素下标
 */
public class BinarySearch_findLastLessEqual {

    public static int bsearch(int[] a, int n, int value) {
        int low=0,high=n-1;
        while (low<=high){
            int middle = low + ((high-low)>>2);
            if(a[middle]<=value){
                if(middle==n-1 || a[middle+1]>value){
                    return middle;
                }else{
                    low = middle+1;
                }
            }else{
                high = middle-1;
            }
        }
        return -1;
    }



    public static void main(String[] args){
        int[] sort = {1,3,4,5,5,5,7};


        int i = bsearch(sort, sort.length, 55);

        System.out.println("二分查找返回的下标值："+i+"  值是："+sort[i]);
    }
}
