package com.trs.gov.kpi.processor;

import com.trs.gov.kpi.entity.msg.CheckEndMsg;
import com.trs.gov.kpi.entity.msg.IMQMsg;
import com.trs.gov.kpi.entity.msg.PageInfoMsg;
import com.trs.gov.kpi.entity.wangkang.WkAvgSpeed;
import com.trs.gov.kpi.entity.wangkang.WkEveryLink;
import com.trs.gov.kpi.entity.wangkang.WkUpdateContent;
import com.trs.gov.kpi.msgqueue.MQListener;
import com.trs.gov.kpi.service.wangkang.WkAvgSpeedUpdateContentService;
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
    WkEveryLinkService wkEveryLinkService;

    @Resource
    WkAvgSpeedUpdateContentService wkAvgSpeedUpdateContentService;

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
            Integer siteId = checkEndMsg.getSiteId();
            Integer checkId = checkEndMsg.getCheckId();

            Integer avgSpeed =  wkEveryLinkService.selectOnceCheckAvgSpeed(siteId, checkId);
            WkAvgSpeed wkAvgSpeed = new WkAvgSpeed();
            wkAvgSpeed.setCheckId(checkEndMsg.getCheckId());
            wkAvgSpeed .setSiteId(checkEndMsg.getSiteId());
            wkAvgSpeed.setAvgSpeed(avgSpeed);
            wkAvgSpeed.setCheckTime(new Date());
            wkAvgSpeedUpdateContentService.insertOnceCheckWkAvgSpeed(wkAvgSpeed);

            int count = 0;
            boolean checkUpdate;
            int lastTimeCheckId = checkId - 1;
            List<WkEveryLink> wkEveryLinkLastTimeList = wkEveryLinkService.getLastTimeWkEveryLinkList(siteId, lastTimeCheckId);
            List<WkEveryLink> wkEveryLinkThisTimeList = wkEveryLinkService.getThisTimeWkEveryLinkList(siteId, checkId);
            for (WkEveryLink wkThisTime : wkEveryLinkThisTimeList){
                checkUpdate = true;
                for (WkEveryLink wkLastTime:wkEveryLinkLastTimeList) {
                    if(wkThisTime.getMd5().equals(wkLastTime.getMd5())){
                        checkUpdate = false;
                        break;
                    }
                }
                if(checkUpdate){
                    count ++;
                }
            }
            WkUpdateContent wkUpdateContent = new WkUpdateContent();
            wkUpdateContent.setSiteId(checkEndMsg.getSiteId());
            wkUpdateContent.setCheckId(checkEndMsg.getCheckId());
            wkUpdateContent.setCheckTime(new Date());
            wkUpdateContent.setUpdateContent(count);
            wkAvgSpeedUpdateContentService.insertOnceCheckWkUpdateContent(wkUpdateContent);

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
