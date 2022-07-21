package xue.xiang.yi.cache;


import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

/**
 * @author : 薛向毅
 * @date : 13:20 2022/7/21
 * 使用内部缓存的工具类
 */
public class FileCacheUtil {

    private final static String splitStr = "#";

    /**
     * 注意, 不可以包含特殊字符 #
     *
     * @param fileName
     * @param bufferStr
     * @return
     * @throws IOException
     */
    public synchronized static boolean write(String fileName, String bufferStr) throws IOException {
        System.out.println("我开始写了");
        File file = new File(fileName);
        if (!file.exists()) {
            file.createNewFile();
        }
        FileOutputStream fileOutputStream = new FileOutputStream(file, true);
        fileOutputStream.write((bufferStr + splitStr).getBytes());
        fileOutputStream.flush();
        fileOutputStream.close();
        System.out.println("我写完比人才能写 哈哈");
        return true;
    }

    /**
     * 注意,写是追加写,请注意逻辑处理成功,删除文件,重新开始写
     * @param fileName
     * @return
     * @throws IOException
     */
    public synchronized static String[] read(String fileName) throws IOException {
        if (!new File(fileName).exists()) {
            return null;
        }
        BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName));
        return bufferedReader.readLine().split(splitStr);
    }

    public synchronized boolean delete(String fileName) throws IOException {
        File file = new File(fileName);
        if (!file.exists()) {
            return true;
        }
        return file.delete();

    }

}
