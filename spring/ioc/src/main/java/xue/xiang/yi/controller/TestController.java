package xue.xiang.yi.controller;

import xue.xiang.yi.annotation.Autowired;
import xue.xiang.yi.annotation.Component;
import xue.xiang.yi.service.TestService;

/**
 * @author : 薛向毅
 * @date : 15:24 2022/7/12
 */
@Component
public class TestController {

    @Autowired
    private TestService testService;

    public void sout(String sout) {
        System.out.println("controller:"+ sout);
        System.out.println("service:");
        testService.sout(sout);
    }
}
