package trs.com.cn.gov.kpi.controller;

import org.springframework.web.bind.annotation.*;
import trs.com.cn.gov.kpi.entity.User;
import trs.com.cn.gov.kpi.service.UserService;

import javax.annotation.Resource;

/**
 * Created by wangxuan on 2017/5/9.
 */
@RestController
public class UserController {

    @Resource
    UserService userService;

    @GetMapping("/user")
    public User getUserById(@RequestParam("id") Integer id) {

        return userService.getUserById(id);
    }

    @GetMapping("/exce")
    public String exceptionTest() throws Exception{

        throw new Exception();
    }
}
