package com.trs.gov.kpi.processor;

import com.trs.gov.kpi.constant.Constants;
import com.trs.gov.kpi.constant.Types;
import com.trs.gov.kpi.dao.CommonMapper;
import com.trs.gov.kpi.entity.dao.DBUpdater;
import com.trs.gov.kpi.entity.dao.QueryFilter;
import com.trs.gov.kpi.entity.dao.Table;
import com.trs.gov.kpi.entity.msg.CalcScoreMsg;
import com.trs.gov.kpi.entity.msg.CheckEndMsg;
import com.trs.gov.kpi.entity.msg.IMQMsg;
import com.trs.gov.kpi.entity.wangkang.WkCheckTime;
import com.trs.gov.kpi.msgqueue.CommonMQ;
import com.trs.gov.kpi.msgqueue.MQListener;
import com.trs.gov.kpi.service.wangkang.WkScoreService;
import com.trs.gov.kpi.service.wangkang.WkSiteManagementService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by linwei on 2017/7/15.
 */
@Component
public class CalcScoreProcessor implements MQListener {

    @Resource
    private WkScoreService wkScoreService;

    @Resource
    private WkSiteManagementService wkSiteManagementService;

    @Resource
    private CommonMapper commonMapper;

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
                updateCheckEndTime(calcScoreMsg.getSiteId(), calcScoreMsg.getCheckId());
                wkSiteManagementService.changeSiteStatus(calcScoreMsg.getSiteId(), Types.WkCheckStatus.DONE_CHECK);
                scoreMsgCountMap.remove(key);
            } else {
                scoreMsgCountMap.put(key, count);
            }
        } else {
            scoreMsgCountMap.put(key, 1);
        }
    }

    private void updateCheckEndTime(Integer siteId, Integer checkId) {
        DBUpdater updater = new DBUpdater(Table.WK_CHECK_TIME.getTableName());
        updater.addField("endTime", new Date());
        updater.addField("checkStatus", WkCheckTime.CHECK_END);
        QueryFilter filter = new QueryFilter(Table.WK_CHECK_TIME);
        filter.addCond(Constants.DB_FIELD_CHECK_ID, checkId);
        filter.addCond(Constants.DB_FIELD_SITE_ID, siteId);
        commonMapper.update(updater, filter);
    }
}
