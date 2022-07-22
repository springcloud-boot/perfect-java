package xue.xiang.yi.notice;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @DATE: 2020/11/30
 * @AUTHOR: 薛向毅
 * desc:
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface DangerNotice {
    public int timeOutMillisecond() default 60000;
    public String methodType() default MethodTypeConstant.OWNER_ORDER;
}
