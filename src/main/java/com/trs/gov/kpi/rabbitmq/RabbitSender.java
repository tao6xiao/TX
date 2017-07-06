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
            String json = "{  \n" +
                    "    \"TYPE\": 11,\n" +
                    "    \"DATA\":{\n" +
                    "         \n" +
                    "        \"CRTIME\":\"2017-05-10 14:33:31\",\n" +
                    "        \"DOCPUBTIME\":\"\",\n" +
                    "        \"CHANNELID\":\"97\",\n" +
                    "        \"DOCSTATUS\":\"10\",\n" +
                    "        \"DOCAUTHOR\":\"聂明磊\",\n" +
                    "        \"DOCRELTIME\":\"\",\n" +
                    "        \"DOCSOURCENAME\":\"教育部\",\n" +
                    "        \"DOCID\":\"1738\",\n" +
                    "        \"DOCTITLE\":\"关于“特岗教师”解除“特岗”合同的咨询\",\n" +
                    "        \"DOCPUBURL\":\"\",\n" +
                    "        \"DOCLINK\":\"\",\n" +
                    "        \"CHANNELNAME\":\"互动\",\n" +
                    "        \"DOCTYPE\":\"20\",\n" +
                    "        \"DOCTYPE_DISP\":\"文字\",\n" +
                    "        \"SUBDOCTITLE\":\"\",\n" +
                    "        \"DOCKEYWORDS\":\"\",\n" +
                    "        \"RECID\":\"2300\",\n" +
                    "        \"DOCCHANNEL\":\"97\",\n" +
                    "        \"METADATAID\":\"1738\",\n" +
                    "        \"WCMMETATABLEGOVDOCVIEWID\":\"39\",\n" +
                    "        \"DOCABSTRACT\":\"\",\n" +
                    "        \"CRUSER\":\"admin\",\n" +
                    "        \"SITEID\":\"1\",\n" +
                    "        \"CRDEPTID\":\"1\" \n" +
                    "    }    \n" +
                    "}";
            log.info("发送");
            source.logOutPut().send(MessageBuilder.withPayload(json).build());
        }catch (Exception e){
            log.error("发送失败", e);
        }
        return null;
    }

}
