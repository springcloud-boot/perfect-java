package xue.xiang.yi.cache;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author : 薛向毅
 * @date : 14:27 2022/7/21
 */
public class MyCacheTest {
    public static void main(String[] args) throws IOException {
        FileCacheUtil.write("ca.txt", "aaa");
        String[] read = FileCacheUtil.read("ca.txt");
        for (int i = 0; i < read.length; i++) {
            System.out.println(read[i]);
        }
    }
}
