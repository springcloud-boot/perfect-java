# redis中的字典表分析

## 1丶源码解读

### 1丶dict结构体

```c++
//字典的结构
typedef struct dict {
    //字典类型
    dictType *type;
    //私有数据
    void *privdata;
    //2个字典hashtable.  一个使用, 一个rehash时使用; ****重点****
    dictht ht[2];
    //记录是否在进行rehash. -1是未进行
    long rehashidx; /* rehashing not in progress if rehashidx == -1 */
    //如果 >0说明 rehash被暂停.
    int16_t pauserehash; /* If >0 rehashing is paused (<0 indicates coding error) */
} dict;
```

### 2丶dictType

```c++
//字典类型, 一些函数操作,不是重点.
typedef struct dictType {
    uint64_t (*hashFunction)(const void *key);
    void *(*keyDup)(void *privdata, const void *key);
    void *(*valDup)(void *privdata, const void *obj);
    int (*keyCompare)(void *privdata, const void *key1, const void *key2);
    void (*keyDestructor)(void *privdata, void *key);
    void (*valDestructor)(void *privdata, void *obj);
    int (*expandAllowed)(size_t moreMem, double usedRatio);
} dictType;
```

### 3丶重点结构 dictht; 

```C++
//hash表
typedef struct dictht {
    //table数组.  数组中的每个元素都是指向 dictEntry 结构的指针， 每个 dictEntry 结构保存着一个键值对
    dictEntry **table;
    //数组的大小
    unsigned long size;
    //哈希表大小掩码，用于计算索引值 .  总是= size-1
    unsigned long sizemask;
    //
    unsigned long used;
} dictht;


typedef struct dictEntry {
    void *key;
    union {
        void *val;
        uint64_t u64;
        int64_t s64;
        double d;
    } v;
    struct dictEntry *next;
} dictEntry;
```