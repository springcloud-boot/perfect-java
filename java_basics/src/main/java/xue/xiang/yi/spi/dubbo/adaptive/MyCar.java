package xue.xiang.yi.spi.dubbo.adaptive;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.extension.Adaptive;
import com.alibaba.dubbo.common.extension.SPI;

/**
 * @author : 薛向毅
 * @date : 13:11 2022/8/2
 */
@SPI
public interface MyCar {
    @Adaptive("carName")
    void run(URL url);
}
