# redis中跳表实现.6.3.7版本

## 1丶找到server.h,查看结构定义

```c++
/* ZSETs use a specialized version of Skiplists */

//跳表中的每个节点
typedef struct zskiplistNode {
    sds ele; //实际元素,这个可以看我之前的sds分析
    double score; //分值,用于排序
    struct zskiplistNode *backward; //向后一个节点的指针
    struct zskiplistLevel {
        struct zskiplistNode *forward; //向前节点的指针
        unsigned long span; //跨度.  有可能跨越多个区间
    } level[];//层级
} zskiplistNode; 

//list结构体
typedef struct zskiplist {
    //指向首尾指针
    struct zskiplistNode *header, *tail;
    //长度
    unsigned long length; 
    //级别,
    int level;
} zskiplist;

```

## 2丶刚创建时的状态,见 t_zet.c

```c++
/* Create a new skiplist. */
zskiplist *zslCreate(void) {
    int j;
    //创建指向 zskiplist的指针.
    zskiplist *zsl;
    //分配一个节点的空间
    zsl = zmalloc(sizeof(*zsl));
    //初始化级别为1
    zsl->level = 1;
    //长度为0
    zsl->length = 0;
    //创建头部节点  #define ZSKIPLIST_MAXLEVEL 32 
    /* Should be enough for 2^64 elements,这里下面会介绍,为什么是2的64次方的元素. ; */
    zsl->header = zslCreateNode(ZSKIPLIST_MAXLEVEL,0,NULL);
    //初始化头节点的所有前置节点为空,跨度为0.
    for (j = 0; j < ZSKIPLIST_MAXLEVEL; j++) {
        zsl->header->level[j].forward = NULL;
        zsl->header->level[j].span = 0;
    }
    zsl->header->backward = NULL;
    zsl->tail = NULL;
    return zsl;
}

zskiplistNode *zslCreateNode(int level, double score, sds ele) {
    zskiplistNode *zn =
        zmalloc(sizeof(*zn)+level*sizeof(struct zskiplistLevel));
    zn->score = score;
    zn->ele = ele;
    return zn;
}
```



刚初始化时的状态为图如下

![image-20220608135243312](C:\Users\G006631\AppData\Roaming\Typora\typora-user-images\image-20220608135243312.png)

## 3丶插入一个元素

```C++
zskiplistNode *zslInsert(zskiplist *zsl, double score, sds ele) {
    zskiplistNode *update[ZSKIPLIST_MAXLEVEL], *x;
    //跨度. 新插入元素,每层的跨越间隔
    unsigned int rank[ZSKIPLIST_MAXLEVEL];
    int i, level;

    serverAssert(!isnan(score));
    x = zsl->header;
    
    /**这是为了找到新元素在每一层的位置前一个节点.
    *  后续在每层插入的时候,只需要如此;   新节点的backward = update[i];  新节点的forward = update[i].forward
    **/
    for (i = zsl->level-1; i >= 0; i--) {
        /* store rank that is crossed to reach the insert position  */
        rank[i] = i == (zsl->level-1) ? 0 : rank[i+1];
        while (x->level[i].forward &&
                (x->level[i].forward->score < score ||
                    (x->level[i].forward->score == score &&
                    sdscmp(x->level[i].forward->ele,ele) < 0)))
        {
            rank[i] += x->level[i].span;
            x = x->level[i].forward;
        }
        update[i] = x;
    }
    /* 这里便是为什么可以容纳2*64次方元素的原因. 因为第32层需要2*64次方元素才有概率放入.见下面分析 */
    level = zslRandomLevel();
    if (level > zsl->level) {
        for (i = zsl->level; i < level; i++) {
            rank[i] = 0;
            update[i] = zsl->header;
            update[i]->level[i].span = zsl->length;
        }
        zsl->level = level;
    }
    x = zslCreateNode(level,score,ele);
    //逐层交换元素
    for (i = 0; i < level; i++) {
        x->level[i].forward = update[i]->level[i].forward;
        update[i]->level[i].forward = x;

        /* update span covered by update[i] as x is inserted here */
        x->level[i].span = update[i]->level[i].span - (rank[0] - rank[i]);
        update[i]->level[i].span = (rank[0] - rank[i]) + 1;
    }

    /* increment span for untouched levels */
    for (i = level; i < zsl->level; i++) {
        update[i]->level[i].span++;
    }

    x->backward = (update[0] == zsl->header) ? NULL : update[0];
    if (x->level[0].forward)
        x->level[0].forward->backward = x;
    else
        zsl->tail = x;
    zsl->length++;
    return x;
}


#define ZSKIPLIST_P 0.25      /* Skiplist P = 1/4
	即第一层的概率为 3/4 = 0.75;       可以理解为  (1/4)^0*(3/4)
	第二层的概率为, 1 减去 第一层的概率 然后乘以0.75;   (1-0.75) * 0.75;  即:  (1/4)^1*(3/4)
	第三层的概率为: (1-0.75)[进入第二层的概率] * 1/4[进入第三层的概率]  *0.75 [在第三次的概率]   即: (1/4)^2*(3/4)
	第四层的概率为: (1-0.75)[进入第二层的概率] * 1/4[进入第三层的概率]  *  1/4[进入第四层的概率] *0.75 [在第四层的概率]
	顾32层:  (1/4)&^31    *  (1/2)*(1/2)*3  =  (1/2)64 * 3
*/
int zslRandomLevel(void) {
    int level = 1;
    while ((random()&0xFFFF) < (ZSKIPLIST_P * 0xFFFF))
        level += 1;
    return (level<ZSKIPLIST_MAXLEVEL) ? level : ZSKIPLIST_MAXLEVEL;
}
```

![image-20220608170011435](C:\Users\G006631\AppData\Roaming\Typora\typora-user-images\image-20220608170011435.png)

```tex
 	主要是学习一种设计的思路,跳表可以理解为N叉数,插入即先找到要插入元素在每层的位置,然后替换.
 插入/删除/改动/查询 基本相似不再过多分析.  思考为何大佬们能设计出这种数据结构.为什么有使用跳表而不用Btrees呢
 
 作者原话:
There are a few reasons:

1) They are not very memory intensive. It's up to you basically. Changing parameters about the probability of a node to have a given number of levels will make then less memory intensive than btrees.

2) A sorted set is often target of many ZRANGE or ZREVRANGE operations, that is, traversing the skip list as a linked list. With this operation the cache locality of skip lists is at least as good as with other kind of balanced trees.

3) They are simpler to implement, debug, and so forth. For instance thanks to the skip list simplicity I received a patch (already in Redis master) with augmented skip lists implementing ZRANK in O(log(N)). It required little changes to the code.

出处:https://news.ycombinator.com/item?id=1171423
```

