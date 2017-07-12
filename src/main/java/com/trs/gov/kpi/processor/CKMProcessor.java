package com.trs.gov.kpi.processor;

import com.trs.gov.kpi.entity.msg.CheckContentMsg;
import com.trs.gov.kpi.msgqueue.MQListener;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by li.hao on 2017/7/11.
 */
@Component
public class CKMProcessor implements MQListener {

    @Resource
    ApplicationContext appContext;

    private ExecutorService fixedThreadPool = Executors.newFixedThreadPool(10);

    @Override
    public void onMessage(Object msg) {

        // 监听待检测的内容消息
        CKMProcessWorker worker = appContext.getBean(CKMProcessWorker.class);
        worker.setContent((CheckContentMsg)msg);

        // 把检测内容分配给检测线程
        fixedThreadPool.execute(worker);
    }
}
