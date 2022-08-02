package xue.xiang.yi.spi.dubbo;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.extension.ExtensionLoader;
import com.alibaba.dubbo.rpc.*;
import xue.xiang.yi.spi.dubbo.adaptive.MyCar;

/**
 * @author : 薛向毅
 * @date : 15:03 2022/7/29
 */
public class DubboSpi {
    public static void main(String[] args) {
        //基础的spi
        ExtensionLoader<Product> extensionLoader = ExtensionLoader.getExtensionLoader(Product.class);
        Product a = extensionLoader.getExtension("A");
        a.product();

        //自适应扩展
        MyCar adaptiveExtension = ExtensionLoader.getExtensionLoader(MyCar.class).getAdaptiveExtension();
        adaptiveExtension.run(URL.valueOf("http://localhost:8080/a?carName=CCAGCar"));


    }
}
