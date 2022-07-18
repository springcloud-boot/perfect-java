# 通过ReentrantLock学习AQS

## 1丶上类图

![AbstractQueuedSynchronizer](C:/Users/G006631/Pictures/AbstractQueuedSynchronizer.png)

由图可见,ReentrantLock 使用内部抽象类 Sync 来操作 AQS的相关方法

## 2丶简单的使用案例

```java
public class ReentrantLockDemo {
    private static ReentrantLock reentrantLock = new ReentrantLock();
    private static Integer test = 0;

    public static void main(String[] args) {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(100, 100, 10, TimeUnit.SECONDS, new SynchronousQueue<>(), new ThreadPoolExecutor.CallerRunsPolicy());
        threadPoolExecutor.prestartAllCoreThreads();
        for (int i = 0; i < 1000; i++) {
            threadPoolExecutor.execute(() -> {
                reentrantLock.lock();
                try {
                    test = test + 1;
                    Thread.sleep(3);
                } catch (Exception e) {
                    System.out.println("报错了");
                } finally {
                    reentrantLock.unlock();
                    ;
                }
            });
        }

        while (threadPoolExecutor.getTaskCount() != threadPoolExecutor.getCompletedTaskCount()) {
            System.out.println(threadPoolExecutor.getTaskCount() + "--" + threadPoolExecutor.getCompletedTaskCount());
        }

        System.out.println(test);  //这里必然是1000
    }
}
```

## 3丶基础属性介绍

```java
public abstract class AbstractQueuedSynchronizer
    extends AbstractOwnableSynchronizer
    implements java.io.Serializable {
    
	/**
     * 锁的状态.
     */
    private volatile int state;

	//队列的头结点
	private transient volatile Node head;
/**
     * 队列尾部节点
     */
    private transient volatile Node tail;
    
    /**
    * csa的使用工具
    */
    private static final Unsafe unsafe = Unsafe.getUnsafe();

}


static final class Node {
        /** 共享模式,此次为使用 */
        static final Node SHARED = new Node();
        /** 标识节点为:独占模式 */
        static final Node EXCLUSIVE = null;

        /** 节点的等待状态 已取消 */
        static final int CANCELLED =  1;
        /** 等待信号源 */
        static final int SIGNAL    = -1;
        /** 等待条件,此次未使用 */
        static final int CONDITION = -2;
        /**
         * 未使用
         */
        static final int PROPAGATE = -3;

        /**
         * 状态如上
         */
        volatile int waitStatus;

        
        volatile Node prev;


        volatile Node next;

        /**
         * 节点所属线程
         */
        volatile Thread thread;

        /**
         * 节点的持有模式. 共享? 独占?
         */
        Node nextWaiter;

        /**
         * 是否共享模式节点
         */
        final boolean isShared() {
            return nextWaiter == SHARED;
        }

        /**
         * 返回前置节点
         */
        final Node predecessor() throws NullPointerException {
            Node p = prev;
            if (p == null)
                throw new NullPointerException();
            else
                return p;
        }

        Node() {    // Used to establish initial head or SHARED marker
        }

        Node(Thread thread, Node mode) {     // Used by addWaiter
            this.nextWaiter = mode;
            this.thread = thread;
        }

        Node(Thread thread, int waitStatus) { // Used by Condition
            this.waitStatus = waitStatus;
            this.thread = thread;
        }
    }
```

## 4丶由创建为入口,观察如何实现 加锁

```java
/**
 * 默认创建的为 非公平锁. 内部类Sync的2中实现之一
 */
public ReentrantLock() {
    sync = new NonfairSync();
}
```

```java
static final class NonfairSync extends Sync {
    private static final long serialVersionUID = 7316153563782823691L;

    /**
     * 执行获取锁的方法时
     */
    final void lock() {
        //使用AQS中的方法,基于CSA进行设置值, unsafe.compareAndSwapInt(this, stateOffset, expect, update);
        if (compareAndSetState(0, 1))
        	//设置成功了 将当前线程设置为 占用锁的线程
            setExclusiveOwnerThread(Thread.currentThread());
        else
            acquire(1);
    }

    protected final boolean tryAcquire(int acquires) {
        return nonfairTryAcquire(acquires);
    }
}
```

我们假设,当前锁已经被占用, 则需要调用 acquire(1) 方法.



```java
//在AQS如下实现
public final void acquire(int arg) {
	//这里用了模板设计模式,调用了子类的方法.tryAcquire(1)
    if (!tryAcquire(arg) &&
        acquireQueued(addWaiter(Node.EXCLUSIVE), arg))
        selfInterrupt();
}
```



```java
//这里调用了 Sync 类中的 nonfairTryAcquire(); 
//通过源码对比,公平锁的 tryAcquire 在自己内部实现,为何非公平锁的写在父类呢?
static final class NonfairSync extends Sync {
    protected final boolean tryAcquire(int acquires) {
        return nonfairTryAcquire(acquires);
    }
}
```



```java
//非公平的模式去获取锁
abstract static class Sync extends AbstractQueuedSynchronizer {
   
    final boolean nonfairTryAcquire(int acquires) {
        final Thread current = Thread.currentThread();
        //当前标识为0,则锁没有被占用
        int c = getState();
        if (c == 0) {
            if (compareAndSetState(0, acquires)) {
                setExclusiveOwnerThread(current);
                return true;
            }
        }
        //如果当前线程已经持有锁, 则将标识为+1 ; 因为进到这个分支,必然是单线程执行,故setState直接赋值即可.
        else if (current == getExclusiveOwnerThread()) {
            int nextc = c + acquires;
            if (nextc < 0) // overflow
                throw new Error("Maximum lock count exceeded");
            setState(nextc);
            return true;
        }
        return false;
    }
    
}
```



```java
//我们假设 tryAcquire(arg) 获取锁失败. 则继续执行 acquireQueued(addWaiter(Node.EXCLUSIVE), arg)方法.
//在AQS如下实现
public final void acquire(int arg) {
    if (!tryAcquire(arg) &&
        acquireQueued(addWaiter(Node.EXCLUSIVE), arg))
        selfInterrupt();
}
```



```java
//在AQS如下实现 ;
/**
* 添加一个等待的节点模式为 EXCLUSIVE .标识为独占模式. 
* 另一个初始化状态为  SHARED  . 则为共享模式; 有: Semaphore/ReentrantReadWriteLock 等使用 暂时不涉及
*/
private Node addWaiter(Node mode) {
    Node node = new Node(Thread.currentThread(), mode);
    // 能进入这个方法,则必然有锁竞争.拿到等待队列的尾节点
    Node pred = tail;
    if (pred != null) {
    	//若尾节点不为空,则将新节点的上个节点设置为 尾节点.
        node.prev = pred;
        //若并发低的情况下这里设置成功,直接返回当前节点即可. 不需要使用enq(mode) 进行自旋设置耗费资源
        if (compareAndSetTail(pred, node)) {
            pred.next = node;
            return node;
        }
    }
    enq(node);
    return node;
}

//这个方法进行第一次锁竞争的初始化,与高并发下正确的将新node放入尾部节点
private Node enq(final Node node) {
        for (;;) {
            Node t = tail;
            //首次竞争,初始化头尾节点. 统一都为new Node(); 未指定下一个节点的占用模式. 
            if (t == null) { // Must initialize
                if (compareAndSetHead(new Node()))
                    tail = head;
            } else {
            	//队列入队
                node.prev = t;
                if (compareAndSetTail(t, node)) {
                    t.next = node;
                    return t;
                }
            }
        }
    }
```



```java
//在AQS中实现. 尾部节点的入队
final boolean acquireQueued(final Node node, int arg) {
    boolean failed = true;
    try {
        boolean interrupted = false;
        for (;;) {
        	//拿到节点的前置节点.
            final Node p = node.predecessor();
            // 若当前节点前面是头部节点, 则直接尝试获取锁.因为前面没有等待锁的节点.
            if (p == head && tryAcquire(arg)) {
            	//若拿到锁了.则将当前节点设置为头部
                setHead(node);
                p.next = null; // help GC   释放当前节点,因为已经赋值给头结点 如下图简单示例
                failed = false;
                return interrupted;
            }
            //头结点下个节点获取锁失败|其它后置节点. 通过源码分析, shouldParkAfterFailedAcquire首次必然返回false
            //则上个节点是头结点则会尝试获取锁2次.其它节点则不会
            if (shouldParkAfterFailedAcquire(p, node) &&
            	//这个方法是暂停线程. 底层不探究,就是释放线程资源.等收到通知才执行; 
                parkAndCheckInterrupt())
                interrupted = true;
        }
    } finally {
        if (failed)
            cancelAcquire(node);
    }
}
```

![image-20220714165403447](C:/Users/G006631/Pictures/image-20220714165403447.png)

```java
//在AQS中
private static boolean shouldParkAfterFailedAcquire(Node pred, Node node) {
    int ws = pred.waitStatus;
    if (ws == Node.SIGNAL)
        /*
         * 已经设置为等待信号源了 则返回可以设置
         */
        return true;
    if (ws > 0) {
        /*
         * 上个节点已经为已取消. 
         */
        do {
        	//pre节点已经取消了
            node.prev = pred = pred.prev; //这里为 当前节点上个节点 = pre节点的上个节点. pre= pre的上个节点
        } while (pred.waitStatus > 0);
        pred.next = node;
    } else {
        /*
         * 任何节点首次执行到这里的时候,waitStatus必然没有赋值,int类型默认为0;则走else分值.
         * 设置这个节点为等待信号源标识
         */
        compareAndSetWaitStatus(pred, ws, Node.SIGNAL);
    }
    return false;
}
```

## 5丶解锁的源码分析

```java
// reentrantLock 中的sync类 继承自 AbstractQueuedSynchronizer. 
public void unlock() {
    sync.release(1);
}
```

```java
//AbstractQueuedSynchronizer
public final boolean release(int arg) {
    //使用模板设计模式.  调用子类实现的方法
    if (tryRelease(arg)) {
        //若释放锁成功,拿到头结点
        Node h = head;
        //通过枷锁的源码分析,只要有并发,且有线程存在等待.  waitStatus必然不能为0,故肯定进去这个方法
        if (h != null && h.waitStatus != 0)
            //通知头结点的下个节点,去继续执行,去获取锁.  因为阻塞的节点线程是头结点的下个线程
            // 1丶初始化时,头结点为 new node();  抢占锁为他的子节点阻塞
			// 2丶非初始化时, 头结点设置为抢占锁的节点.(执行程序,并不会阻塞.)  
            //头结点可以理解为(初始化时不是),拿到锁的节点
            unparkSuccessor(h);
        return true;
    }
    return false;
}

private void unparkSuccessor(Node node) {
        /*
         * 清楚头结点标识.  为0时, 下个节点可以尝试获取锁,2次, 否则,则1次就进入等待. 这里就算更新失败也是可以的,
       	 *          只是会提前进入阻塞
         */
        int ws = node.waitStatus;
        if (ws < 0)
            compareAndSetWaitStatus(node, ws, 0);

        /*
          * 拿到下个节点.
         */
        Node s = node.next;
        if (s == null || s.waitStatus > 0) {
            s = null;
            for (Node t = tail; t != null && t != node; t = t.prev)
                if (t.waitStatus <= 0)
                    s = t;
        }
        if (s != null)
            LockSupport.unpark(s.thread);
    }
```



```java
//尝试释放锁. 必须是枷锁的线程释放
protected final boolean tryRelease(int releases) {
    int c = getState() - releases;
    //如果释放锁,和占有锁的线程不一致, 直接报错
    if (Thread.currentThread() != getExclusiveOwnerThread())
        throw new IllegalMonitorStateException();
    boolean free = false;
    //释放成功
    if (c == 0) {
        free = true;
        setExclusiveOwnerThread(null);
    }
    setState(c);
    return free;
}
```

## 6丶至此. 非公平锁分析完毕. 公平锁大部分同非公平锁代码,不做分析.



为何非公平锁的获取锁方法写在sync中,搜索源码可以看到

java.util.concurrent.locks.ReentrantLock#tryLock() 

在这里实现的复用, 因为大部分程序都是非公平模式,故放在父类,进行代码复用



**注意点 tryLock() 就算是公平锁 也是按照非公平锁的方法进行获取锁的!**

