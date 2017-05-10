package com.trs.gov.kpi.controller;

import com.trs.gov.kpi.entity.User;
import com.trs.gov.kpi.service.UserService;
import org.springframework.web.bind.annotation.*;

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
