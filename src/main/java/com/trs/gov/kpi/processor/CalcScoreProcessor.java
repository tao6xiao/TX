package com.trs.gov.kpi.processor;

import com.trs.gov.kpi.entity.msg.CalcScoreMsg;
import com.trs.gov.kpi.entity.msg.CheckEndMsg;
import com.trs.gov.kpi.entity.msg.IMQMsg;
import com.trs.gov.kpi.msgqueue.MQListener;
import com.trs.gov.kpi.service.wangkang.WkScoreService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by linwei on 2017/7/15.
 */
@Component
public class CalcScoreProcessor implements MQListener {

    @Resource
    private WkScoreService wkScoreService;

    // 算分消息计数的map， key = siteId#checkId
    private Map<String, Integer> scoreMsgCountMap = new HashMap<>();


    @Override
    public String getType() {
        return CalcScoreMsg.MSG_TYPE;
    }

    @Override
    public void onMessage(IMQMsg msg) {

        if (msg.getType().equals(CheckEndMsg.MSG_TYPE)) {
            return;
        }

        CalcScoreMsg calcScoreMsg = (CalcScoreMsg)msg;
        String key = calcScoreMsg.getSiteId() + "#" + calcScoreMsg.getCheckId();
        Integer count = scoreMsgCountMap.get(key);
        if (count != null) {
            count++;
            if (count >= 4) {
                // 算总分
                wkScoreService.calcTotalScore(calcScoreMsg.getSiteId(), calcScoreMsg.getCheckId());
                scoreMsgCountMap.remove(key);
            } else {
                scoreMsgCountMap.put(key, count);
            }
        }
    }
}
