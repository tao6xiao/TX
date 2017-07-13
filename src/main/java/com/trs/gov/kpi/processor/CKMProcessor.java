package com.trs.gov.kpi.processor;

import com.trs.gov.kpi.entity.msg.CheckEndMsg;
import com.trs.gov.kpi.entity.msg.IMQMsg;
import com.trs.gov.kpi.entity.msg.PageInfoMsg;
import com.trs.gov.kpi.msgqueue.MQListener;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by li.hao on 2017/7/11.
 */
@Component
public class CKMProcessor implements MQListener {

    @Resource
    ApplicationContext appContext;

    private final String name = "CKMProcessor";

    private ExecutorService fixedThreadPool = Executors.newFixedThreadPool(10);

    @Override
    public String getType() {
        return PageInfoMsg.MSG_TYPE;
    }

    @Override
    public void onMessage(IMQMsg msg) {

        if (msg.getType().equals(CheckEndMsg.MSG_TYPE)) {

        } else {
            // 监听待检测的内容消息
            CKMProcessWorker worker = appContext.getBean(CKMProcessWorker.class);
            worker.setContent((PageInfoMsg)msg);

            // 把检测内容分配给检测线程
            fixedThreadPool.execute(worker);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CKMProcessor that = (CKMProcessor) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
