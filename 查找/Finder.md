## 查找算法

### 二分查找
二分查找是效率非常高的算法，其时间复杂度为O(LogN)。
二分查找是基于有序数组的一种高效的查找算法。
其有多种变体：

1. 最基本的在数组中查找有无指定值，要求数组无重复元素。

-----------------二分查找的变体---------------

1. 数组中有重复元素，查找第一个等于指定值的下标。
2. 数组中有重复元素，查找最后一个等于指定值的下标。
3. 数组中有重复元素，查找第一个大于等于指定值的下标。
4. 数组中有重复元素，查找最后一个小于等于指定值的下标。


### 二分查找的算法实现
具体的代码参考BinarySearch.java

```
public static int binarySearch(int[] a, int n, int value){
        if(n == 0 || 0>= a.length){
            return -1;
        }

        int low=0;
        int high=n-1;

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
        }
        //不存在返回-1
        return -1;
    }
```
注意点：
1. 循环的条件：high >=low
2. low和high的更新middle -/+ 1
3. middle = (high+low)/2的写法是有问题的，因为如果low和high比较大的话，可能会溢出。
   改进的写法是low+(high-low)/2。
   
   进一步改进的写法是：因为计算机处理位运算比处理除法运算要快得多，故该语句可以将除以2的操作改进成：
   low+((high-low)>>1)
   
   
### 二分查找的递归算法实现
```
// 二分查找的递归实现
public int bsearch(int[] a, int n, int val) {
  return bsearchInternally(a, 0, n - 1, val);
}

private int bsearchInternally(int[] a, int low, int high, int value) {
  if (low > high) return -1;

  int mid =  low + ((high - low) >> 1);
  if (a[mid] == value) {
    return mid;
  } else if (a[mid] < value) {
    return bsearchInternally(a, mid+1, high, value);
  } else {
    return bsearchInternally(a, low, mid-1, value);
  }
}
```

## 思考题：
利用二分查找算法在循环有序的不重复数组中查找指定值，类似在4，5，6，1，2，3 中查找指定值

实现：

参考CircleSortedBinarySearch.java