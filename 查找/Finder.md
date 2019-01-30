## 查找算法

### 二分查找
二分查找是效率非常高的算法，其时间复杂度为O(LogN)。

### 二分查找的算法实现
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