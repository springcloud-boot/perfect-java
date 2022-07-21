package xue.xiang.yi.lock;

import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author : 薛向毅
 * @date : 15:00 2022/7/14
 */
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

        System.out.println(test);
        System.out.println(threadPoolExecutor.getTaskCount() + "--" + threadPoolExecutor.getCompletedTaskCount());

    }
}
