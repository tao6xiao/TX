package com.trs.gov.kpi.msgqueue;

import com.trs.gov.kpi.entity.outerapi.Site;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * Created by li.hao on 2017/7/11.
 */
@Component
@Scope("prototype")
public class CommonMQ extends Thread {

    private Set<MQListener> listeners = Collections.synchronizedSet(new HashSet<>());

    private ConcurrentLinkedQueue<Object> msgQueue = new ConcurrentLinkedQueue<>();

    private Object msgWaiter = new Object();

    public void publishMsg(Object msg) {
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

        synchronized (msgWaiter) {
            try {
                msgWaiter.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        List<Object> msgList = new ArrayList<>();
        synchronized (msgQueue) {
            while (!msgQueue.isEmpty()) {
                msgList.add(msgQueue.peek());
            }
            msgQueue.clear();
        }

        for (Object msg : msgList) {
            for (MQListener listener : listeners) {
                listener.onMessage(msg);
            }
        }
    }
}
