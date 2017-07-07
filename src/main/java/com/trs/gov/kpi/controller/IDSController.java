package com.trs.gov.kpi.controller;

import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by he.lang on 2017/6/29.
 */
@RestController
public class IDSController {

    @RequestMapping(value = "/gov/kpi", method = RequestMethod.GET)
    public String checkHealth11() {

        return "";
    }

    @RequestMapping(value = "/gov/kpi/index", method = RequestMethod.GET)
    public String checkHealth12(HttpServletRequest request) {
        request.getSession(true).setAttribute("rand", "1234");
        return "123";
    }

    @RequestMapping(value = "/gov/kpi/login", method = RequestMethod.GET)
    public String doLogin() {

        return "login";
    }
}
