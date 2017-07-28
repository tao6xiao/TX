package com.trs.gov.kpi.msgqueue;

import com.trs.gov.kpi.entity.msg.CheckEndMsg;
import com.trs.gov.kpi.entity.msg.IMQMsg;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by li.hao on 2017/7/11.
 */
@Component
@Slf4j
public class CommonMQ extends Thread {

    private Set<MQListener> listeners = Collections.synchronizedSet(new HashSet<>());

    private ConcurrentLinkedQueue<IMQMsg> msgQueue = new ConcurrentLinkedQueue<>();

    public void publishMsg(IMQMsg msg) {
        synchronized (msgQueue) {
            msgQueue.add(msg);
            msgQueue.notifyAll();
        }
    }

    public void registerListener(MQListener listener) {
        listeners.add(listener);
    }

    @Override
    public void run() {
        super.run();
        while (true) {
            try {
                List<IMQMsg> msgList = getMsg();
                for (IMQMsg msg : msgList) {
                    for (MQListener listener : listeners) {
                        try {
                            if (msg.getType().equals(listener.getType())) {
                                listener.onMessage(msg);
                            } else if (msg.getType().endsWith(CheckEndMsg.MSG_TYPE)) {
                                listener.onMessage(msg);
                            }
                        } catch (Exception e) {
                            // 一个消息监听器失败，不能影响其他监听器处理
                            log.error("", e);
                        }
                    }
                }
            } catch (Exception e) {
                log.error("", e);
            }
        }
    }

    private List<IMQMsg> getMsg() {
        List<IMQMsg> msgList = new ArrayList<>();
        while (true) {
            synchronized (msgQueue) {
                if (!msgQueue.isEmpty()) {
                    while (!msgQueue.isEmpty()) {
                        msgList.add(msgQueue.poll());
                    }
                    msgQueue.clear();
                    return msgList;
                } else {
                    try {
                        msgQueue.wait();
                    } catch (InterruptedException e) {
                        log.error("", e);
                    }
                }
            }
        }
    }
}
