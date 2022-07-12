package xue.xiang.yi;

import xue.xiang.yi.annotation.ComponentScan;
import xue.xiang.yi.context.ApplicationContext;
import xue.xiang.yi.controller.TestController;

import java.net.URISyntaxException;

/**
 * @author : 薛向毅
 * @date : 14:28 2022/7/12
 */
@ComponentScan(scanPackages = "xue.xiang.yi")
public class Main {
    public static void main(String[] args) throws URISyntaxException {
        ApplicationContext applicationContext = new ApplicationContext(Main.class);
        TestController test = (TestController) applicationContext.getBean("TestController");
        test.sout("哈哈哈");

    }
}
