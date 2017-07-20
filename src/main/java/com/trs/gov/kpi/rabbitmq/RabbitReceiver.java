package com.trs.gov.kpi.rabbitmq;

import com.trs.gov.kpi.constant.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.Message;

/**
 * Created by he.lang on 2017/6/22.
 */
@Slf4j
@EnableBinding(Barista.class)
public class RabbitReceiver {

    @StreamListener(Constants.INPUT_CHANNEL)
    public void receiver(Message<Object> message) {
        Object o = message.getPayload();
        log.info("接受对象:" + o + "\n");
    }
}

