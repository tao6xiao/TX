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
import com.trs.gov.kpi.msgqueue.MQListener;
import com.trs.gov.kpi.service.wangkang.WkAllStatsService;
import com.trs.gov.kpi.service.wangkang.WkScoreService;
import com.trs.gov.kpi.service.wangkang.WkSiteManagementService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by linwei on 2017/7/15.
 */
@Slf4j
@Component
public class CalcScoreProcessor implements MQListener {

    @Resource
    private WkScoreService wkScoreService;

    @Resource
    private WkSiteManagementService wkSiteManagementService;

    @Resource
    private CommonMapper commonMapper;

    @Resource
    private WkAllStatsService wkAllStatsService;

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
        log.info("receive calc msg " + key + ", type = " + calcScoreMsg.getScoreType());
        Integer count = scoreMsgCountMap.get(key);
        if (count != null) {
            count++;
            if (count >= 4) {
                // 算总分
                log.info("==begin calc total score of site[" + calcScoreMsg.getSiteId() + "]");
                try {
                    wkScoreService.calcTotalScore(calcScoreMsg.getSiteId(), calcScoreMsg.getCheckId());
                } catch (Exception e) {
                    log.error("", e);
                } finally {
                    log.info("==end calc total score of site[" + calcScoreMsg.getSiteId() + "]");
                }

                updateCheckEndTime(calcScoreMsg.getSiteId(), calcScoreMsg.getCheckId());

                // 修改站点检测状态
                log.info("==begin change site status of site[" + calcScoreMsg.getSiteId() + "]");
                try {
                    wkSiteManagementService.changeSiteStatus(calcScoreMsg.getSiteId(), Types.WkCheckStatus.DONE_CHECK);
                } catch (Exception e) {
                    log.error("", e);
                } finally {
                    log.info("==end change site status of site[" + calcScoreMsg.getSiteId() + "]");
                }

                log.info("==begin getLastTimeCheckBySiteIdAndCheckId of site[" + calcScoreMsg.getSiteId() + "]");
                try {
                    wkAllStatsService.getLastTimeCheckBySiteIdAndCheckId(calcScoreMsg.getSiteId(), calcScoreMsg.getCheckId());
                } catch (Exception e) {
                    log.error("", e);
                } finally {
                    log.info("==end getLastTimeCheckBySiteIdAndCheckId of site[" + calcScoreMsg.getSiteId() + "]");
                }
                scoreMsgCountMap.remove(key);
            } else {
                scoreMsgCountMap.put(key, count);
            }
        } else {
            scoreMsgCountMap.put(key, 1);
        }
    }

    private void updateCheckEndTime(Integer siteId, Integer checkId) {

        try {
            log.info("==begin update check end time of site[" + siteId + "]");
            DBUpdater updater = new DBUpdater(Table.WK_CHECK_TIME.getTableName());
            updater.addField("endTime", new Date());
            updater.addField("checkStatus", WkCheckTime.CHECK_END);
            QueryFilter filter = new QueryFilter(Table.WK_CHECK_TIME);
            filter.addCond(Constants.DB_FIELD_CHECK_ID, checkId);
            filter.addCond(Constants.DB_FIELD_SITE_ID, siteId);
            commonMapper.update(updater, filter);
        } catch (Exception e) {
            log.error("", e);
        } finally {
            log.info("==end update check end time of site[" + siteId + "]");
        }
    }
}
