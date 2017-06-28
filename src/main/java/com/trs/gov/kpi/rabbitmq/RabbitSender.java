package com.trs.gov.kpi.rabbitmq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * Created by he.lang on 2017/6/22.
 */
@Service
@Component
@Slf4j
public class RabbitSender {
    @Autowired
    private Barista source;

    public String sendMessage(){
        try {
            Object o = new Object();
            source.logOutPut().send(MessageBuilder.withPayload(o).build());
            log.info("发送");
        }catch (Exception e){
            log.error("发送失败", e);
        }
        return null;
    }

}
