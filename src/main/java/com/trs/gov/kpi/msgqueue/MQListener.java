package com.trs.gov.kpi.msgqueue;

import com.trs.gov.kpi.entity.msg.IMQMsg;

import java.text.ParseException;

/**
 * Created by li.hao on 2017/7/11.
 */
public interface MQListener  {

    String getType();

    void onMessage(IMQMsg msg) throws ParseException;

}
