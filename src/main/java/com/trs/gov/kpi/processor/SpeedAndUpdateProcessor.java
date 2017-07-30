package com.trs.gov.kpi.processor;

import com.trs.gov.kpi.dao.WkAllStatsMapper;
import com.trs.gov.kpi.entity.msg.CalcScoreMsg;
import com.trs.gov.kpi.entity.msg.CheckEndMsg;
import com.trs.gov.kpi.entity.msg.IMQMsg;
import com.trs.gov.kpi.entity.msg.PageInfoMsg;
import com.trs.gov.kpi.entity.wangkang.WkAllStats;
import com.trs.gov.kpi.entity.wangkang.WkEveryLink;
import com.trs.gov.kpi.entity.wangkang.WkScore;
import com.trs.gov.kpi.msgqueue.CommonMQ;
import com.trs.gov.kpi.msgqueue.MQListener;
import com.trs.gov.kpi.service.wangkang.WkAllStatsService;
import com.trs.gov.kpi.service.wangkang.WkEveryLinkService;
import com.trs.gov.kpi.service.wangkang.WkScoreService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Created by linwei on 2017/7/12.
 */
@Slf4j
@Component
public class SpeedAndUpdateProcessor implements MQListener {

    @Resource
    private WkEveryLinkService wkEveryLinkService;

    @Resource
    private WkAllStatsService wkAllStatsService;

    @Resource
    private WkAllStatsMapper wkAllStatsMapper;

    @Resource
    private WkScoreService wkScoreService;

    @Resource
    private CommonMQ commonMQ;

    private final String name = "SpeedAndUpdateProcessor";

    @Override
    public String getType() {
        return PageInfoMsg.MSG_TYPE;
    }

    @Override
    public void onMessage(IMQMsg msg) throws ParseException {

        if (msg.getType().equals(CheckEndMsg.MSG_TYPE)) {
            // 检查结束
            CheckEndMsg checkEndMsg = (CheckEndMsg)msg;
            log.info("SpeedAndUpdateProcessor receive end msg");
            try {
                Integer avgSpeed =  wkEveryLinkService.selectOnceCheckAvgSpeed(checkEndMsg.getSiteId(), checkEndMsg.getCheckId());
                Integer count = getUpdateCount(checkEndMsg.getSiteId(), checkEndMsg.getCheckId());
                WkAllStats wkAllStats = new WkAllStats();
                wkAllStats.setSiteId(checkEndMsg.getSiteId());
                wkAllStats.setCheckId(checkEndMsg.getCheckId());
                wkAllStats.setUpdateContent(count);
                wkAllStats.setAvgSpeed(avgSpeed);
                wkAllStatsService.insertOrUpdateUpdateContentAndSpeed(wkAllStats);
                calcScoreAndInsert(checkEndMsg.getSiteId(), checkEndMsg.getCheckId(), avgSpeed);
            } catch (Exception e) {
                log.error("", e);
            }

            CalcScoreMsg calcUpdateScoreMsg = new CalcScoreMsg();
            calcUpdateScoreMsg.setCheckId(checkEndMsg.getCheckId());
            calcUpdateScoreMsg.setSiteId(checkEndMsg.getSiteId());
            calcUpdateScoreMsg.setScoreType("updateContent");
            commonMQ.publishMsg(calcUpdateScoreMsg);
            log.info("SpeedAndUpdateProcessor send  updateContent calc score msg");

            CalcScoreMsg calcSpeedScoreMsg = new CalcScoreMsg();
            calcSpeedScoreMsg.setCheckId(checkEndMsg.getCheckId());
            calcSpeedScoreMsg.setSiteId(checkEndMsg.getSiteId());
            calcSpeedScoreMsg.setScoreType("avgSpeed");
            commonMQ.publishMsg(calcSpeedScoreMsg);
            log.info("SpeedAndUpdateProcessor send  avgSpeed calc score msg");
        } else {
            PageInfoMsg speedMsg = (PageInfoMsg)msg;
            WkEveryLink wkEveryLink = new WkEveryLink();
            wkEveryLink.setCheckId(speedMsg.getCheckId());
            wkEveryLink.setCheckTime(new Date());
            wkEveryLink.setSiteId(speedMsg.getSiteId());
            wkEveryLink.setUrl(speedMsg.getUrl());
            wkEveryLink.setAccessSpeed(speedMsg.getSpeed());
            wkEveryLink.setMd5(DigestUtils.md5Hex(speedMsg.getContent().intern()));
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

    private void calcScoreAndInsert(Integer siteId, Integer checkId, long avgSpeed){

        /**
         * T1(性能检测得分)= 100(1-ln(T/10+1) ,T为页面抓取平均耗时是200毫秒的倍数
         */
        double avg = avgSpeed;
        double avgLog = Math.log((avg / 200)/10 + 1);
        double avgScoreD = 100 * (1 - avgLog);
        //对计算结果小数点后一位做四舍五入处理
        DecimalFormat df = new DecimalFormat("#.0");
        String avgScoreL = df.format(avgScoreD);
        double avgSpeedScore = Double.valueOf(avgScoreL);
        if (avgSpeedScore < 0) {
            avgSpeedScore = 0;
        }
        /**
         * SD更新得分(权值20%)	 	=D1x50%+D2x50%
         * 	D1(网站最近更新时间T天)			=	100 (1- ln(T/10+1))
         * 	D2(网站最近一周更新文章数N) 	=	100 ln(N/100+1)		N<250
         */
        double thisTime = new Date().getTime();
        double lastTime = 0.0f;
        Date lastDate = wkAllStatsMapper.getLastTimeUpdateContent(siteId);
        if (lastDate != null) {
            lastTime = lastDate.getTime();
        }
        double days = ((thisTime - lastTime)/(1000 * 60 * 60 * 24));

        double updateContentD1 = Math.log(days / 10 + 1);
        double updateD1 = 100 * (1 - updateContentD1);
        if(updateD1 < 0){
            updateD1 = 0;
        }

        Calendar c = Calendar.getInstance();
        //过去七天
        Date endTime = new Date();
        c.add(Calendar.DATE, - 7);
        Date beginTime = c.getTime();
        Integer oneWeekUpdateCount = wkAllStatsMapper.getOneWeekUpdateCount(beginTime, endTime, siteId);
        if (250 <= oneWeekUpdateCount ){
            oneWeekUpdateCount = 249;
        }
        double oneWeekUpdateCountD = oneWeekUpdateCount;
        double updateContentD2 = Math.log(oneWeekUpdateCountD / 100 + 1);
        double updateD2 = 100 * updateContentD2;
        if(updateD2 >= 100){
            updateD2 = 100;
        }

        //对计算结果做四舍五入处理
        double updateCountScoreL = updateD1 * 0.5 + updateD2 * 0.5;
        String updateCountScoreD = df.format(updateCountScoreL);
        double updateCountScore = Double.valueOf(updateCountScoreD);
        if (updateCountScore < 0) {
            updateCountScore = 0;
        }

        WkScore score = new WkScore();
        score.setSiteId(siteId);
        score.setCheckId(checkId);
        score.setCheckTime(new Date());
        score.setOverSpeed(avgSpeedScore);
        score.setUpdateContent(updateCountScore);

        wkScoreService.insertOrUpdateUpdateContentAndSpeed(score);
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
