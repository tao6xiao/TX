package com.trs.gov.kpi.msgqueue;

/**
 * Created by li.hao on 2017/7/11.
 */
public interface MQListener {

    void onMessage(Object msg);

}
