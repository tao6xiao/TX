package com.trs.gov.kpi.processor;

import com.trs.gov.kpi.msgqueue.MQListener;
import org.springframework.stereotype.Component;

/**
 * Created by linwei on 2017/7/12.
 */
@Component
public class InvalidLinkProcessor implements MQListener {

    @Override
    public void onMessage(Object msg) {

    }
}
