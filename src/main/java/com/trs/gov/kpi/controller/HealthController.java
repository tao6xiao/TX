package com.trs.gov.kpi.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by linwei on 2017/6/28.
 */
@RestController
public class HealthController {

    @RequestMapping(value = "/gov/kpi/health", method = RequestMethod.GET)
    public Map<String, Object> checkHealth() {
        Map<String, Object> health = new HashMap<>();

        Map<String, Object> status = new HashMap<>();
        status.put("code", "UP");
        status.put("description", "KPI service is okay!");

        health.put("status", status);
        health.put("details", new Object());

        return health;
    }
}
