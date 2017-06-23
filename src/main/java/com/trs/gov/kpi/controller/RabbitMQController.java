package com.trs.gov.kpi.controller;

import com.trs.gov.kpi.rabbitmq.RabbitSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by he.lang on 2017/6/22.
 */
@Slf4j
@RestController
public class RabbitMQController {

//    @Autowired
//    RabbitSender sender;
//
//    @RequestMapping(value = "/rabbitmq", method = RequestMethod.POST)
//    public String send(){
//        sender.sendMessage();
//        return null;
//    }
}
