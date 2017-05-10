package trs.com.cn.gov.kpi.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

/**
 * Created by wangxuan on 2017/5/9.
 */
@RestController
public class HelloController {

    @GetMapping("/")
    public Map<String, String> sayHello() {

        return Collections.singletonMap("say", "hello");
    }
}
