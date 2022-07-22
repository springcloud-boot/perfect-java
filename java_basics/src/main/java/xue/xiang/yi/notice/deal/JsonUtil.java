package xue.xiang.yi.notice.deal;

import com.alibaba.fastjson.JSON;

/**
 * @author : 薛向毅
 * @date : 17:20 2022/7/22
 */
public class JsonUtil {
    public static Object encode(Object[] args) {
        return JSON.toJSONString(args);
    }
}
