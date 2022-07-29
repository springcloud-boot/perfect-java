package xue.xiang.yi.boot.initializer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import xue.xiang.yi.boot.aop.Student;

/**
 * @author : 薛向毅
 * @date : 9:45 2022/7/29
 *
 * 这个类只是为了打断点. 看出beanFactory何时注入属性
 */
@Component
public class Teacher implements CommandLineRunner {
    @Autowired
    private Student student;

    private void test() {
        student.say();
    }


    @Override
    public void run(String... args) throws Exception {
        student.say();
    }
}
