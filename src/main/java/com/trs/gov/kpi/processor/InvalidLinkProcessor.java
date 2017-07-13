package com.trs.gov.kpi.processor;

import com.trs.gov.kpi.entity.msg.IMQMsg;
import com.trs.gov.kpi.entity.msg.InvalidLinkMsg;
import com.trs.gov.kpi.msgqueue.MQListener;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * Created by linwei on 2017/7/12.
 */
@Component
public class InvalidLinkProcessor implements MQListener {

    private final String name = "InvalidLinkProcessor";

    @Override
    public String getType() {
        return InvalidLinkMsg.MSG_TYPE;
    }

    @Override
    public void onMessage(IMQMsg msg) {

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InvalidLinkProcessor that = (InvalidLinkProcessor) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
