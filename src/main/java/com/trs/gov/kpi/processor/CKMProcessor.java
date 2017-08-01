package com.trs.gov.kpi.processor;

import com.trs.gov.kpi.constant.Types;
import com.trs.gov.kpi.dao.CommonMapper;
import com.trs.gov.kpi.entity.msg.CalcScoreMsg;
import com.trs.gov.kpi.entity.msg.CheckEndMsg;
import com.trs.gov.kpi.entity.msg.IMQMsg;
import com.trs.gov.kpi.entity.msg.PageInfoMsg;
import com.trs.gov.kpi.entity.wangkang.WkAllStats;
import com.trs.gov.kpi.entity.wangkang.WkIssue;
import com.trs.gov.kpi.entity.wangkang.WkIssueCount;
import com.trs.gov.kpi.entity.wangkang.WkScore;
import com.trs.gov.kpi.msgqueue.CommonMQ;
import com.trs.gov.kpi.msgqueue.MQListener;
import com.trs.gov.kpi.service.wangkang.WkAllStatsService;
import com.trs.gov.kpi.service.wangkang.WkIssueService;
import com.trs.gov.kpi.service.wangkang.WkScoreService;
import com.trs.gov.kpi.utils.DBUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by li.hao on 2017/7/11.
 */
@Component
@Slf4j
public class CKMProcessor implements MQListener {

    @Resource
    ApplicationContext appContext;

    @Resource
    private WkIssueService wkIssueService;

    @Resource
    private CommonMapper commonMapper;

    @Resource
    private WkScoreService wkScoreService;

    @Resource
    private CommonMQ commonMQ;

    @Resource
    private WkAllStatsService wkAllStatsService;

    private final String name = "CKMProcessor";

    private ExecutorService fixedThreadPool = Executors.newFixedThreadPool(10);

    // 线程池的锁
    private Object threadPoolLocker = new Object();

    @Override
    public String getType() {
        return PageInfoMsg.MSG_TYPE;
    }

    @Override
    public void onMessage(IMQMsg msg) {
        if (msg.getType().equals(CheckEndMsg.MSG_TYPE)) {

            CheckEndMsg checkEndMsg = (CheckEndMsg)msg;
            log.info("CKMProcessor receive end msg");
            try {
                WkAllStats wkAllStats = new WkAllStats();
                wkAllStats.setSiteId(checkEndMsg.getSiteId());
                wkAllStats.setCheckId(checkEndMsg.getCheckId());
                wkAllStats.setErrorInfo(wkIssueService.getErrorWordsCount(checkEndMsg.getSiteId(), checkEndMsg.getCheckId()));
                wkAllStatsService.insertOrUpdateErrorWords(wkAllStats);

                // 比较并记录入库
                compareLastCheckAndInsert(checkEndMsg.getSiteId(), checkEndMsg.getCheckId());

                final int errorWordsCount = wkIssueService.getErrorWordsCount(checkEndMsg.getSiteId(), checkEndMsg.getCheckId());
                calcScoreAndInsert(checkEndMsg.getSiteId(), checkEndMsg.getCheckId(), errorWordsCount);
            } catch (Exception e) {
                log.error("", e);
            }

            CalcScoreMsg calcSpeedScoreMsg = new CalcScoreMsg();
            calcSpeedScoreMsg.setCheckId(checkEndMsg.getCheckId());
            calcSpeedScoreMsg.setSiteId(checkEndMsg.getSiteId());
            calcSpeedScoreMsg.setScoreType("errorWords");
            commonMQ.publishMsg(calcSpeedScoreMsg);
            log.info("CKMProcessor send errorWords calc score msg");
        } else {
            // 监听待检测的内容消息
            CKMProcessWorker worker = appContext.getBean(CKMProcessWorker.class);
            worker.setContent((PageInfoMsg)msg);
            worker.run();
        }
    }

    /**
     * 同上一次监测结果进行比较，统计出已解决问题个数和现有问题个数，并入统计库
     *
     * @param siteId
     * @param checkId
     */
    private void compareLastCheckAndInsert(Integer siteId, Integer checkId) {
        //上一次完成检查的数据的checkId
        Integer lastCheckid= wkAllStatsService.getLastCheckId(siteId, checkId);
        if (lastCheckid == null) {
            // 第一次检查
            final int errorWordsCount = wkIssueService.getErrorWordsCount(siteId, checkId);
            WkIssueCount issueCount = new WkIssueCount();
            issueCount.setCheckId(checkId);
            issueCount.setCheckTime(new Date());
            issueCount.setIsResolved(0);
            issueCount.setUnResolved(errorWordsCount);
            issueCount.setSiteId(siteId);
            issueCount.setTypeId(Types.WkSiteCheckType.CONTENT_ERROR.value);
            commonMapper.insert(DBUtil.toRow(issueCount));
        } else {
            final List<WkIssue> curInvalidLinkList = wkIssueService.getErrorWordsList(siteId, checkId);
            final List<WkIssue> lastInvalidLinkList = wkIssueService.getErrorWordsList(siteId, lastCheckid);

            int resolvedCount = 0;
            boolean isResolved;
            for (WkIssue lastLink : lastInvalidLinkList) {
                isResolved = true;
                for (int index = 0; index < curInvalidLinkList.size(); index++) {
                    if (lastLink.getUrl().equals(curInvalidLinkList.get(index).getUrl())
                            && lastLink.getSubTypeId().equals(curInvalidLinkList.get(index).getSubTypeId())
                            && lastLink.getDetailInfo().equals(curInvalidLinkList.get(index).getDetailInfo())) {
                        isResolved = false;
                        break;
                    }
                }
                if (isResolved) {
                    resolvedCount++;
                }
            }

            final int errorWordsCount = wkIssueService.getErrorWordsCount(siteId, checkId);
            WkIssueCount issueCount = new WkIssueCount();
            issueCount.setCheckId(checkId);
            issueCount.setCheckTime(new Date());
            issueCount.setIsResolved(resolvedCount);
            issueCount.setUnResolved(errorWordsCount);
            issueCount.setSiteId(siteId);
            issueCount.setTypeId(Types.WkSiteCheckType.CONTENT_ERROR.value);
            commonMapper.insert(DBUtil.toRow(issueCount));
        }
    }

    private void calcScoreAndInsert(Integer siteId, Integer checkId, double errorCount) {
        /**
         *  SC 内容得分(权值20%)	 	= R123x20%+R4x40%+R5x40%
         *	R1、2、3：党和领导人名字、党和领导人称谓、党和领导人排序
         *	R4：敏感词
         *	R5：错别字
         *  各项得分计算公式：100(1-ln(R/100+1)) 其中R为次数除以总数后的百分比
         */

        double errorCountScore = 100;
        if(errorCount > 0){
            double allErrorEount = errorCount;

            double typosCount = wkIssueService.getTyposCount(siteId, checkId);
            double sensitiveWordsCount = wkIssueService.getSensitiveWordsCount(siteId, checkId);
            double politicsCount = wkIssueService.getPoliticsCount(siteId, checkId);

            double typosR123 = Math.log((typosCount/allErrorEount)*100 /100 + 1);
            double typosScore = 100 * (1 - typosR123);

            double sensitiveWordsR4 = Math.log((sensitiveWordsCount/allErrorEount) *100 /100 + 1);
            double sensitiveWordsScore = 100 * (1 - sensitiveWordsR4);

            double politicsR5 = Math.log((politicsCount/allErrorEount)*100 /100 + 1);
            double politicsScore = 100 * (1 - politicsR5);

            //对计算结果小数点后一位做四舍五入处理
            double errorCountScoreL = typosScore * 0.2 + sensitiveWordsScore * 0.4 + politicsScore * 0.4;
            DecimalFormat df = new DecimalFormat("#.0");
            String errorCountScoreD = df.format(errorCountScoreL);
            errorCountScore = Double.valueOf(errorCountScoreD);
            if (errorCountScore < 0) {
                errorCountScore = 0;
            }
        }

        WkScore score = new WkScore();
        score.setSiteId(siteId);
        score.setCheckId(checkId);
        score.setCheckTime(new Date());
        score.setContentError(errorCountScore);

        wkScoreService.insertOrUpdateErrorWords(score);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CKMProcessor that = (CKMProcessor) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
