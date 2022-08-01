package xue.xiang.yi.spi.dubbo;

import com.alibaba.dubbo.common.extension.ExtensionLoader;

/**
 * @author : 薛向毅
 * @date : 15:03 2022/7/29
 */
public class DubboSpi {
    public static void main(String[] args) {
        ExtensionLoader<Product> extensionLoader = ExtensionLoader.getExtensionLoader(Product.class);
        Product a = extensionLoader.getExtension("A");
        a.product();

    }
}
