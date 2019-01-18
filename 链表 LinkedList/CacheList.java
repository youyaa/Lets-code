package LinkedList;

/**
 * @Author: listeningrain
 * @Date: 2019-01-16 20:28
 * @Description: 单链表实现LRU缓存淘汰算法
 */
public class CacheList {
    //头节点
    private SingleList head;
    //尾节点
    private SingleList tail;
    //当前缓存的长度
    private Integer currentListSize;
    //缓存的最大长度
    private Integer MAX_SIZE;

    //初始化
    public CacheList() {
        //初始化头尾节点为null
        head = tail = new SingleList(null, null);
        currentListSize = 0;
        MAX_SIZE = 10;
    }

    //初始化时设置缓存长度
    public CacheList(Integer MAX_SIZE) {
        head = tail = new SingleList(null, null);
        currentListSize = 0;
        this.MAX_SIZE = MAX_SIZE;
    }

    //放入缓存中
    public Boolean put(Integer key, Integer value) {
        //缓存已满，删除最后一个节点
        if (currentListSize == MAX_SIZE) {
            System.out.println("缓存满咯，删除一个");
            //找到尾节点的前一个节点
            SingleList next = head;
            while (null != next) {
                if (next.next == tail) {
                    //删除最后一个节点
                    System.out.println("删除的节点key=" + tail.key + " value=" + tail.value);
                    next.next = null;
                    tail = next;
                    break;
                }
                next = next.next;
            }
            currentListSize--;
        }

        SingleList singleList = new SingleList(key, value);
        SingleList next = head.next;
        head.next = singleList;
        singleList.next = next;
        if (head == tail) {
            tail = singleList;
        }

        currentListSize++;

        return true;
    }

    //取缓存值
    public Integer get(Integer key) {
        boolean flag = false;
        Integer result = null;
        SingleList next = head;
        if (null != head) {
            while (null != next) {
                if (key.equals(next.key)) {
                    flag = true;
                    result = next.value;
                }
                next = next.next;
            }
        }

        if (true == flag) {
            //缓存中存在，先删除原位置的节点，再插入到第一个位置
            SingleList next3 = head;
            while (null != next3) {
                if (key.equals(next3.next.key)) {
                    //删除
                    SingleList old = next3.next;
                    next3.next = old.next;
                    if (next3.next == tail) {
                        tail = next3;
                    }
                    //插入第一位
                    SingleList next2 = head.next;
                    head.next = old;
                    old.next = next2;
                    break;
                }
                next3 = next3.next;
            }
        }
        return result;
    }


    //打印链表
    public void printList() {
        if (null == head) {
            return;
        }
        SingleList singleList = head;
        while (singleList != null) {
            System.out.print("key:" + singleList.key + " value:" + singleList.value);
            if (null != singleList.next) {
                System.out.print(" --> ");
            }
            singleList = singleList.next;
        }
    }


    public static void main(String[] args) {
        CacheList cacheList = new CacheList(2);
        cacheList.put(12, 12);
        cacheList.put(13, 14);
        cacheList.put(43, 56);

        System.out.println("------------取值前----------");
        cacheList.printList();

        System.out.println();
        System.out.println("------------取值中-----------");
        Integer integer = cacheList.get(15);
        System.out.println(integer);

        System.out.println("缓存中不存在，放入缓存");
        cacheList.put(15, 56);

        System.out.println("------------取值后----------");
        cacheList.printList();
        System.out.println();

        System.out.println("--------再次放-------");
        cacheList.put(100,1000);
        cacheList.printList();

        System.out.println("--------再次放-------");
        cacheList.put(111,1111);
        cacheList.printList();

    }

}


/**
 * 单链表的定义
 */
class SingleList {
    public Integer key;
    public Integer value;
    //下一个节点的指针
    public SingleList next = null;

    public SingleList() {
    }

    public SingleList(Integer key, Integer value) {
        this.key = key;
        this.value = value;
    }
}
