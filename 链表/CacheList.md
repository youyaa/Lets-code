## 使用单链表实现LRU缓存淘汰算法

## 思路
1. 当访问缓存时，如果在缓存中找到了，则将该节点从原位置删除，插入到第一个位置。

2. 若在缓存中没有找到对应的值，则一般情况下会从数据库中取值，然后放进缓存。
  
  这个时候则分为两种情况：
  
  若缓存已满，则删除掉链表的最后一个节点，将新值插入到链表第一个位置。
  
  若缓存未满，直接插入到链表的第一个位置。


## 实现
  请查看CacheList.java文件