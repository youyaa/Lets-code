package Sort;

/**
 * @Author: listeningrain
 * @Date: 2019-01-23 08:49
 * @Description: 优化后的冒泡排序
 */
public class BubbleSort {

    public static Boolean bubbleSort(Integer[] nums){
        if(null == nums || 1> nums.length){
            return false;
        }

        for(int i=0; i<nums.length-1; i++){
            Boolean flag = false;
            for(int j=0; j<nums.length-1-i; j++){
                if(nums[j]>nums[j+1]){
                    Integer temp = nums[j];
                    nums[j] = nums[j+1];
                    nums[j+1] = temp;
                    flag = true;
                }
            }

            if(!flag){
                System.out.println("本次循环没有交换元素，直接结束");
                break;
            }
            int times = i+1;
            System.out.println("循环次数："+times);
        }
        return true;
    }

    public static void main(String[] args){
        Integer[] sort = {34,2,35,345,64,7};
        Boolean aBoolean = BubbleSort.bubbleSort(sort);

        System.out.println("排序的结果是"+aBoolean);

        for(int i=0; i<sort.length; i++){
            System.out.print(sort[i]+" ");
        }
    }
}
