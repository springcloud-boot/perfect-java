package xue.xiang.yi.boot;

import org.springframework.beans.BeansException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import xue.xiang.yi.boot.aop.Person;

@SpringBootApplication
@EnableAspectJAutoProxy
public class BootApplication implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    public static void main(String[] args) {
        SpringApplication.run(BootApplication.class, args);
        Person bean2 = (Person)applicationContext.getBean("student");
        bean2.say();

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
