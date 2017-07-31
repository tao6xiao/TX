package com.trs.gov.kpi.msgqueue;

import com.trs.gov.kpi.entity.msg.CheckEndMsg;
import com.trs.gov.kpi.entity.msg.IMQMsg;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * Created by li.hao on 2017/7/11.
 */
@Component
@Slf4j
public class CommonMQ extends Thread {

    private Set<MQListener> listeners = Collections.synchronizedSet(new HashSet<>());

    private Map<Integer, ExecutorService> threadMap = new HashMap<>();

    private ConcurrentLinkedQueue<IMQMsg> msgQueue = new ConcurrentLinkedQueue<>();

    public CommonMQ() {
        for(int i = 0; i < 10; i++) {
            final int index = i;
            threadMap.put(i, Executors.newSingleThreadExecutor(new ThreadFactory() {
                @Override
                public Thread newThread(@NotNull Runnable r) {
                    return new Thread(r, "CommonMQ-Thread-" + index);
                }
            }));
        }
    }

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

                    if (msg.getType().equals(CheckEndMsg.MSG_TYPE)) {
                        log.info(" receive end msg. checkid[{}]", msg.getCheckId());
                    }

                    int no = msg.getCheckId() % threadMap.size();
                    threadMap.get(no).execute(new Runnable() {
                        @Override
                        public void run() {
                            for (MQListener listener : listeners) {
                                try {
                                    if (msg.getType().equals(listener.getType())) {
                                        listener.onMessage(msg);
                                    } else if (msg.getType().endsWith(CheckEndMsg.MSG_TYPE)) {
                                        listener.onMessage(msg);
                                    }
                                } catch (Throwable e) {
                                    // 一个消息监听器失败，不能影响其他监听器处理
                                    log.error("", e);
                                }
                            }
                        }
                    });
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
