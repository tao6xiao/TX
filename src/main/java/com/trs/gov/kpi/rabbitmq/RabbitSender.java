package com.trs.gov.kpi.rabbitmq;

import com.trs.gov.kpi.constant.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by he.lang on 2017/6/22.
 */
@Service
@Component
@Slf4j
public class RabbitSender {
    @Autowired
    private Barista source;

    @InboundChannelAdapter(Constants.OUTPUT_CHANNEL)
    public String sendMessage(){
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            log.info("发送");
            source.logOutPut().send(MessageBuilder.withPayload(sdf.format(new Date())).build());
        }catch (Exception e){
            log.error("发送失败", e);
        }
        return null;
    }

}
