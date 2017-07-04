package com.trs.gov.kpi.rabbitmq;

import com.trs.gov.kpi.constant.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.InboundChannelAdapter;
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

//    @InboundChannelAdapter(value = Constants.OUTPUT_CHANNEL, autoStartup = "false")
    public String sendMessage(){
        try {
            String json = "{\n" +
                    "    \"msgType\":\"editcenter_department_new_doc_byday\",\n" +
                    "    \"data\":{\n" +
                    "        \"User\":12,\n" +
                    "        \"Department\":12,\n" +
                    "        \"siteId\":11,\n" +
                    "        \"CRDay\":\"2017-06-03\",\n" +
                    "    }\n" +
                    "}";
            log.info("发送");
            source.logOutPut().send(MessageBuilder.withPayload(json).build());
        }catch (Exception e){
            log.error("发送失败", e);
        }
        return null;
    }

}
