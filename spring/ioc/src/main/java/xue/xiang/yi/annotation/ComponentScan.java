package xue.xiang.yi.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author : 薛向毅
 * @date : 13:55 2022/7/12
 *
 * 自定义扫描注解
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ComponentScan {

   String scanPackages() default "";

}
