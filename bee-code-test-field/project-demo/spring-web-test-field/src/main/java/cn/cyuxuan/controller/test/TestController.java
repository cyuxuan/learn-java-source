package cn.cyuxuan.controller.test;

import cn.cyuxuan.service.test.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {
    @Autowired
    private TestService testService;

    @GetMapping("/teststr")
    public String testStr() {
        try{
            Thread.sleep(500);
        } catch (Exception e) {
            e.printStackTrace();
        }
        testService.testMore("123");
        char[] chars = new char[104857600];
        return testService.testService();
    }

    @GetMapping("/teststr2")
    public String testStr2() {
        try{
            Thread.sleep(500);
        } catch (Exception e) {
            e.printStackTrace();
        }
        testService.testMore("123");
        char[] chars = new char[104857600];
        return testService.testService();
    }
}
