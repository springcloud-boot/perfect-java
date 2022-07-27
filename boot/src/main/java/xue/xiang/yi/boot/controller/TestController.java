package xue.xiang.yi.boot.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : 薛向毅
 * @date : 8:56 2022/7/27
 */
@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping("/hello")
    public String test(){
        return System.currentTimeMillis()+"";
    }


}
