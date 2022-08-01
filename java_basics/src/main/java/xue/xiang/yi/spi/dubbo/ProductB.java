package xue.xiang.yi.spi.dubbo;

/**
 * @author : 薛向毅
 * @date : 15:02 2022/7/29
 */
public class ProductB implements Product{
    @Override
    public void product() {
        System.out.println("我是产品B");
    }
}
