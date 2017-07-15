package com.trs.gov.kpi.processor;

import com.trs.gov.kpi.entity.msg.CheckEndMsg;
import com.trs.gov.kpi.entity.msg.IMQMsg;
import com.trs.gov.kpi.entity.msg.PageInfoMsg;
import com.trs.gov.kpi.entity.wangkang.WkEveryLink;
import com.trs.gov.kpi.entity.wangkang.WkAllStats;
import com.trs.gov.kpi.msgqueue.MQListener;
import com.trs.gov.kpi.service.wangkang.WkAllStatsService;
import com.trs.gov.kpi.service.wangkang.WkEveryLinkService;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Created by linwei on 2017/7/12.
 */
@Component
public class SpeedAndUpdateProcessor implements MQListener {

    @Resource
    private WkEveryLinkService wkEveryLinkService;

    @Resource
    private WkAllStatsService wkAllStatsService;

    private final String name = "SpeedAndUpdateProcessor";

    @Override
    public String getType() {
        return PageInfoMsg.MSG_TYPE;
    }

    @Override
    public void onMessage(IMQMsg msg) {

        if (msg.getType().equals(CheckEndMsg.MSG_TYPE)) {
            // 检查结束
            CheckEndMsg checkEndMsg = (CheckEndMsg)msg;
            Integer avgSpeed =  wkEveryLinkService.selectOnceCheckAvgSpeed(checkEndMsg.getSiteId(), checkEndMsg.getCheckId());
            Integer count = getUpdateCount(checkEndMsg.getSiteId(), checkEndMsg.getCheckId());
            WkAllStats wkAllStats = new WkAllStats();
            wkAllStats.setSiteId(checkEndMsg.getSiteId());
            wkAllStats.setCheckId(checkEndMsg.getCheckId());
            wkAllStats.setUpdateContent(count);
            wkAllStats.setAvgSpeed(avgSpeed);
            wkAllStatsService.insertOrUpdateUpdateContentAndSpeed(wkAllStats);

        } else {
            PageInfoMsg speedMsg = (PageInfoMsg)msg;
            WkEveryLink wkEveryLink = new WkEveryLink();
            wkEveryLink.setCheckId(speedMsg.getCheckId());
            wkEveryLink.setCheckTime(new Date());
            wkEveryLink.setSiteId(speedMsg.getSiteId());
            wkEveryLink.setUrl(speedMsg.getUrl());
            wkEveryLink.setAccessSpeed(speedMsg.getSpeed());
            wkEveryLink.setMd5(DigestUtils.md5Hex(speedMsg.getContent()));
            wkEveryLinkService.insertWkEveryLinkAccessSpeed(wkEveryLink);
        }
    }

    private Integer getUpdateCount(Integer siteId, Integer checkId) {
        int count = 0;
        boolean checkUpdate;
        Integer lastTimeCheckId = wkAllStatsService.getLastCheckId(siteId, checkId);
        if (lastTimeCheckId == null) {
            // 第一次检查
            count = wkEveryLinkService.count(siteId, checkId);
        } else {
            List<WkEveryLink> lastTimeList = wkEveryLinkService.getList(siteId, lastTimeCheckId);
            List<WkEveryLink> thisTimeList = wkEveryLinkService.getList(siteId, checkId);
            for (WkEveryLink wkThisTime : thisTimeList){
                checkUpdate = true;
                for (WkEveryLink wkLastTime:lastTimeList) {
                    if(wkThisTime.getMd5().equals(wkLastTime.getMd5())){
                        checkUpdate = false;
                        break;
                    }
                }
                if(checkUpdate){
                    count ++;
                }
            }
        }

        return count;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SpeedAndUpdateProcessor that = (SpeedAndUpdateProcessor) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
