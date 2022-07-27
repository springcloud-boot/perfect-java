package xue.xiang.yi.boot.cyclic.dependency;

import org.springframework.stereotype.Component;

/**
 * @author : 薛向毅
 * @date : 14:48 2022/7/27
 */
public class A {
    private B b ;

    public B getB() {
        return b;
    }

    public void setB(B b) {
        this.b = b;
    }
}
