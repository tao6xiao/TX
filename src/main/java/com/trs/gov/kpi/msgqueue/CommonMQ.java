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

    private Object msgWaiter = new Object();

    public void publishMsg(IMQMsg msg) {
        msgQueue.add(msg);
        synchronized (msgWaiter) {
            msgWaiter.notifyAll();
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
                List<IMQMsg> msgList = new ArrayList<>();
                if (!msgQueue.isEmpty()) {
                    synchronized (msgQueue) {
                        while (!msgQueue.isEmpty()) {
                            msgList.add(msgQueue.poll());
                        }
                        msgQueue.clear();
                    }
                } else {
                    synchronized (msgWaiter) {
                        if (msgQueue.isEmpty()) {
                            try {
                                msgWaiter.wait();
                            } catch (InterruptedException e) {
                                log.error("", e);
                            }
                        } else {
                            while (!msgQueue.isEmpty()) {
                                msgList.add(msgQueue.poll());
                            }
                            msgQueue.clear();
                        }
                    }
                }

                for (IMQMsg msg : msgList) {
                    for (MQListener listener : listeners) {
                        if (msg.getType().equals(listener.getType())) {
                            listener.onMessage(msg);
                        } else if (msg.getType().endsWith(CheckEndMsg.MSG_TYPE)) {
                            listener.onMessage(msg);
                        }
                    }
                }
            } catch (Throwable e) {
                log.error("", e);
            }
        }
    }
}
