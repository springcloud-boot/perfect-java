# redis中的sds结构解析

## 0丶源码基于redis 6.2.7

## 1丶什么是sds

```tex
sds 即: simple dynamic string,简单动态字符串
```

## 2丶redis为什么使用sds,不适用c语言的字符串呢?

```tex
1丶sds可以在O(1)的时间范围中获取字符串长度,c语言需要遍历
2丶sds拥有自动扩容机制.
3丶sds拥有惰性空间释放机制,减少了内存分配次数.
4丶sds是二进制安全的.
```

## 3丶从源码探究

### 3.1 ,下载源码

[Download | Redis](https://redis.io/download/)

### 3.2. 查看源码

打开sds.h

```c++
/*
 *  sdshdr5从未被使用过,只是访问flag标识;
 *  flag 低三位 存储类型;  高5位 存储数据长度.  2^5=32.  因为buf最后以/0结尾. 故最大长度为31.
 * typedef unsigned char     uint8_t;
   typedef unsigned short    uint16_t;
   typedef unsigned int      uint32_t;
   typedef unsigned __int64     uint64_t;
 * */
struct __attribute__ ((__packed__)) sdshdr5 {
    unsigned char flags; /*flag 低三位 存储类型;  高5位 存储数据长度.  2^5=32.  因为buf最后以/0结尾. 故最大长度为31.*/
    char buf[];
};
struct __attribute__ ((__packed__)) sdshdr8 {
    uint8_t len; /* 已使用的数据长度 */
    uint8_t alloc; /* 总长度  */
    unsigned char flags; /* 3 lsb of type, 5 unused bits */
    char buf[];
};
struct __attribute__ ((__packed__)) sdshdr16 {
    uint16_t len; /* used */
    uint16_t alloc; /* excluding the header and null terminator */
    unsigned char flags; /* 3 lsb of type, 5 unused bits */
    char buf[];
};
struct __attribute__ ((__packed__)) sdshdr32 {
    uint32_t len; /* used */
    uint32_t alloc; /* excluding the header and null terminator */
    unsigned char flags; /* 3 lsb of type, 5 unused bits */
    char buf[];
};
struct __attribute__ ((__packed__)) sdshdr64 {
    uint64_t len; /* used */
    uint64_t alloc; /* excluding the header and null terminator */
    unsigned char flags; /* 3 lsb of type, 5 unused bits */
    char buf[];
};
```



短字符串,长度小于32时,使用的数据结构sdshdr5,结构如下.

![image-20220601133329707](C:\Users\G006631\AppData\Roaming\Typora\typora-user-images\image-20220601133329707.png)

相比redis3.0.0结构:

```
struct sdshdr {
    //记录buf数组中已使用字节的数量
    //等于SDS所保存字符串的长度
    unsigned int len;

    //记录buf数组中未使用字节的数量
    unsigned int free;

    //char数组，用于保存字符串
    char buf[];
};
```

![image-20220601133429559](C:\Users\G006631\AppData\Roaming\Typora\typora-user-images\image-20220601133429559.png)

### 3.3. sds的优化

​	因为历史版本的sds,无论字符串多长,它header里面的len,free字段,都需要占用8个字节的大小,这如果保存小字符串实在是太浪费了,

故在6.2.7中我们发现,他根据不同的字符串长度,使用不同的结构类型. 如果字符串长度在32位以内,则只多了1个字节的长度来存储字符串长度信息.

## 4丶优势解读

#### 4.1, 为什么是O(1)的时间复杂度获取字符串长度?

根据,6.2.7源码中看出,无论字符串长度多少,只是增加了一个字段.

```tex
unsigned char flags;
```

1位=8字节. flags字段,它使用第三位保存数据结构

如下图获取字符串长度的源码:

```c++
#define SDS_TYPE_5  0
#define SDS_TYPE_8  1
#define SDS_TYPE_16 2
#define SDS_TYPE_32 3
#define SDS_TYPE_64 4
#define SDS_TYPE_MASK 7
#define SDS_TYPE_BITS 3
//这里的sizeof 返回实际的类型
#define SDS_HDR_VAR(T,s) struct sdshdr##T *sh = (void*)((s)-(sizeof(struct sdshdr##T)));
#define SDS_HDR(T,s) ((struct sdshdr##T *)((s)-(sizeof(struct sdshdr##T))))
//falgs字段向右移动三位,就是字符串长度
#define SDS_TYPE_5_LEN(f) ((f)>>SDS_TYPE_BITS)

/**获取字符串长度*/
static inline size_t sdslen(const sds s) {
    //因为sds的指针指向buf数组,则buf数组往前一位,就是falgs字段.
    unsigned char flags = s[-1];
    //flags字段的低3位是数据结构,与7 &; 则得到实际的数据结构.    
    // 7   = 0 0 0 0 0 1  1  1
    //flags= X X X X X X  X  X 
    switch(flags&SDS_TYPE_MASK) {
        case SDS_TYPE_5:
            //因为高5位是字符串长度,拿到高5位的值即可.
            return SDS_TYPE_5_LEN(flags);
            
            //余下方式都一样,拿到实际对应的数据结构,见源码3.2. 然后拿出期中的len字段
        case SDS_TYPE_8:
            return SDS_HDR(8,s)->len;
        case SDS_TYPE_16:
            return SDS_HDR(16,s)->len;
        case SDS_TYPE_32:
            return SDS_HDR(32,s)->len;
        case SDS_TYPE_64:
            return SDS_HDR(64,s)->len;
    }
    return 0;
}
```

由上图可见,因为字符串长度是用属性存储的,故直接获取即可,不需要像c语言一样循环得出.

#### 4.2 sds是二进制安全的.

**	什么是二进制安全？**

**通俗地讲，C语言中，用'0'表示字符串的结束，如果字符串本身就有'0'字符，字符串就会被截断，即非二进制安全；若通过某种机制，保证读写字符串时不损害其内容，则是二进制安全。**

SDS使用len属性的值判断字符串是否结束，不会受'\0'的影响。

#### 4.3 sds的的自动扩容

首先看C语言中的字符串拼接

```c++
char * __cdecl strcat (char * dst, const char * src)
{
    char * cp = dst;
 
    while( *cp )
        cp++; /* 找到 dst 的结尾 */
 
    while( *cp++ = *src++ ) ; /* 无脑将 src 复制到 dst 中; 要是dst的长度不够呢?就会被覆盖!!!!!!!! */ 
 
    return( dst ); /* 返回 dst */
}    
```

在看redis中的扩容:

```c++
sds sdscatsds(sds s, const sds t) {
    return sdscatlen(s, t, sdslen(t));
}

/* s: 源字符串
 * t: 待拼接字符串
 * len: 待拼接字符串长度
 */
sds sdscatlen(sds s, const void *t, size_t len) {
    // 获取目前字符串的长度
    size_t curlen = sdslen(s);
  // SDS 自动扩容. 传入原字符串长度与拼接字符串长度
    s = sdsMakeRoomFor(s,len);
    if (s == NULL) return NULL;
    // 将目标字符串拷贝至源字符串末尾
    memcpy(s+curlen, t, len);
    // 更新 SDS 长度
    sdssetlen(s, curlen+len);
    // 追加结束符
    s[curlen+len] = '\0';
    return s;
}


/* s: 源字符串
 * addlen: 新增长度
 */
sds sdsMakeRoomFor(sds s, size_t addlen) {
    void *sh, *newsh;
    // sdsavail: s->alloc - s->len, 获取 SDS 的剩余长度; 
    //如果是SDS_TYPE_5 则直接返回0;
    size_t avail = sdsavail(s);
    size_t len, newlen, reqlen;
    // 根据 flags 获取 SDS 的类型 oldtype
    char type, oldtype = s[-1] & SDS_TYPE_MASK;
    int hdrlen;
    size_t usable;

    /* Return ASAP if there is enough space left. */
    // 剩余空间大于等于新增空间，无需扩容，直接返回源字符串
    if (avail >= addlen) return s;
    // 获取当前长度
    len = sdslen(s);
    // 
    sh = (char*)s-sdsHdrSize(oldtype);
    // 新长度
    reqlen = newlen = (len+addlen);
    // 断言新长度比原长度长，否则终止执行
    assert(newlen > len);   /* 防止数据溢出 */
    // SDS_MAX_PREALLOC = 1024*1024, 即1MB
    if (newlen < SDS_MAX_PREALLOC)
        // 新增后长度小于 1MB ，则按新长度的两倍扩容
        newlen *= 2;
    else
        // 新增后长度大于 1MB ，则按新长度加上 1MB 扩容
        newlen += SDS_MAX_PREALLOC;
    // 重新计算 SDS 的类型
    type = sdsReqType(newlen);

    /* Don't use type 5: the user is appending to the string and type 5 is
     * not able to remember empty space, so sdsMakeRoomFor() must be called
     * at every appending operation. */
    // 不使用 sdshdr5 . 因为sdshdr5是没有len属性与alloc属性 无法记住 空的 空间.
    if (type == SDS_TYPE_5) type = SDS_TYPE_8;
    // 获取新的 header 大小
    hdrlen = sdsHdrSize(type);
    assert(hdrlen + newlen + 1 > reqlen);  /* Catch size_t overflow */
    if (oldtype==type) {
        // 类型没变
        // 调用 s_realloc_usable 重新分配可用内存，返回新 SDS 的头部指针
        // usable 会被设置为当前分配的大小
        newsh = s_realloc_usable(sh, hdrlen+newlen+1, &usable);
        if (newsh == NULL) return NULL; // 分配失败直接返回NULL
        // 获取指向 buf 的指针
        s = (char*)newsh+hdrlen;
    } else {
        // 类型变化导致 header 的大小也变化，需要向前移动字符串，不能使用 realloc
        newsh = s_malloc_usable(hdrlen+newlen+1, &usable);
        if (newsh == NULL) return NULL;
        // 将原字符串copy至新空间中
        memcpy((char*)newsh+hdrlen, s, len+1);
        // 释放原字符串内存
        s_free(sh);
        s = (char*)newsh+hdrlen;
        // 更新 SDS 类型
        s[-1] = type;
        // 设置长度
        sdssetlen(s, len);
    }
    // 获取 buf 总长度(待定)
    usable = usable-hdrlen-1;
    if (usable > sdsTypeMaxSize(type))
        // 若可用空间大于当前类型支持的最大长度则截断
        usable = sdsTypeMaxSize(type);
    // 设置 buf 总长度
    sdssetalloc(s, usable);
    return s;
}


```

**自动扩容机制总结：**

**扩容阶段：**

- 若 SDS 中剩余空闲空间 avail 大于新增内容的长度 addlen，则无需扩容； 注意:若是sdshdr5 则直接范围0,必然扩容.

- 若 SDS 中剩余空闲空间 avail 小于或等于新增内容的长度 addlen：

- - 若新增后总长度 len+addlen < 1MB，则按新长度的两倍扩容；
  - 若新增后总长度 len+addlen > 1MB，则按新长度加上 1MB 扩容。

**内存分配阶段：**

- 根据扩容后的长度选择对应的 SDS 类型：

- - 若类型不变，则只需通过 `s_realloc_usable`扩大 buf 数组即可；
  - 若类型变化，则需要为整个 SDS 重新分配内存，并将原来的 SDS 内容拷贝至新位置。

#### 4.4 惰性空间释放机制,空间预分配策略

1丶空间预分配策略. 这样子扩容N次,SDS 将连续增长N次字符串所需的内存重分配次数从必定N次降低为最多N次。

​	见4.3的扩容逻辑,因为每次是*2或者增加1M大小,预留了空间,减少内存分配的次数.

2丶惰性空间释放机制

 空间预分配,是减少增加字符串扩容的次数,冻醒空间释放则相反,是优化了减少字符串空间分配的次数.

```c++
sds sdstrim(sds s, const char *cset) {
    char *start, *end, *sp, *ep;
    size_t len;

    sp = start = s;
    ep = end = s+sdslen(s)-1;
    // strchr()函数用于查找给定字符串中某一个特定字符
    while(sp <= end && strchr(cset, *sp)) sp++;
    while(ep > sp && strchr(cset, *ep)) ep--;
    len = (sp > ep) ? 0 : ((ep-sp)+1);
    if (s != sp) memmove(s, sp, len);
    s[len] = '\0';
    // 仅仅更新了len
    sdssetlen(s,len);
    return s;
}
```



其提供了真正释放空间的方法,来让其自行判断何时调用;
```c++
sds sdsRemoveFreeSpace(sds s) {
    void *sh, *newsh;
    //sds类型
    char type, oldtype = s[-1] & SDS_TYPE_MASK;
    // 获取 header 大小
    int hdrlen, oldhdrlen = sdsHdrSize(oldtype);
    //原字符串长度
    size_t len = sdslen(s);
    //原sds剩余空间
    size_t avail = sdsavail(s);
    //获取指向头部的指针; s的指针
    sh = (char*)s-oldhdrlen;

    /* Return ASAP if there is no space left. */
    if (avail == 0) return s;

    /* 获取实际需要的类型,与header大小 */
    type = sdsReqType(len);
    hdrlen = sdsHdrSize(type);

    /* If the type is the same, or at least a large enough type is still
     * required, we just realloc(), letting the allocator to do the copy
     * only if really needed. Otherwise if the change is huge, we manually
     * reallocate the string to use the different header type. */
    if (oldtype==type || type > SDS_TYPE_8) {
        newsh = s_realloc(sh, oldhdrlen+len+1);
        if (newsh == NULL) return NULL;
        s = (char*)newsh+oldhdrlen;
    } else {
        newsh = s_malloc(hdrlen+len+1);
        if (newsh == NULL) return NULL;
        memcpy((char*)newsh+hdrlen, s, len+1);
        s_free(sh);
        s = (char*)newsh+hdrlen;
        s[-1] = type;
        sdssetlen(s, len);
    }
    sdssetalloc(s, len);
    return s;
}
```

