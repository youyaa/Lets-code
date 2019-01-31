package Finder;

/**
 * @Author: listeningrain
 * @Date: 2019-01-31 15:24
 * @Description: 利用二分查找算法在循环有序的不重复数组中查找指定值，类似在4，5，6，1，2，3 中查找指定值
 */
public class CircleSortedBinarySearch {

    public  int circleBinarySearch(int[] a, int value){
        int low = 0, high=0;
        for(int i=0;i<a.length-1;i++){
            //找到有序数组的下标
            if(a[i]<a[i+1]){
                high=i+1;
            }else{
                //有序数组到顶，二分查找
                int i1 = binarySearch(low, high, a, value);
                if(-1 != i1){
                    return i1;
                }else{
                    low = high+1;
                }
            }
            //high已经到最后把一个位置
            if(a.length-1 == high){
                return binarySearch(low, high, a, value);
            }
        }
        return -1;
    }

    public int binarySearch(int low, int high, int[] a, int value){
        for(;low<=high;){
            int middle = low+((high-low)>>1);
            if(a[middle] == value){
                return middle;
            }
            if(a[middle] > value){
                high = middle -1;
            }else{
                low = middle +1;
            }
        }
        return -1;
    }

    public static void main(String[] args){
        int[] sort = {4,5,6,1,2,3,56,67,899};
        int i = new CircleSortedBinarySearch().circleBinarySearch(sort, 6756);

        System.out.println("二分查找出来的下标为："+i);
    }
}
