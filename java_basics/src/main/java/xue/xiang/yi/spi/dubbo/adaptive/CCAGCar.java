package xue.xiang.yi.spi.dubbo.adaptive;

import com.alibaba.dubbo.common.URL;

/**
 * @author : 薛向毅
 * @date : 13:16 2022/8/2
 */
public class CCAGCar implements MyCar{
    @Override
    public void run(URL url) {
        System.out.println("长安车跑起来了");
    }
}
