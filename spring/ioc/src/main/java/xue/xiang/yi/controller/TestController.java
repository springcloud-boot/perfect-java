package xue.xiang.yi.controller;

import xue.xiang.yi.annotation.Autowired;
import xue.xiang.yi.annotation.Component;
import xue.xiang.yi.service.TestService;
import xue.xiang.yi.service.TestServiceB;

/**
 * @author : 薛向毅
 * @date : 15:24 2022/7/12
 */
@Component
public class TestController {

    @Autowired
    private TestService testService;

    private final TestServiceB testServiceB;

    public TestController(TestServiceB testServiceB) {
        this.testServiceB = testServiceB;
    }

    public void sout(String sout) {
        System.out.println("controller:"+ sout);
        System.out.println("service:");
        testService.sout(sout);
        testServiceB.soutb();
    }
}
