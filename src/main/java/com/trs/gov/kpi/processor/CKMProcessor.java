package com.trs.gov.kpi.processor;

import com.trs.gov.kpi.constant.Types;
import com.trs.gov.kpi.dao.CommonMapper;
import com.trs.gov.kpi.entity.msg.CheckEndMsg;
import com.trs.gov.kpi.entity.msg.IMQMsg;
import com.trs.gov.kpi.entity.msg.PageInfoMsg;
import com.trs.gov.kpi.entity.wangkang.WkAllStats;
import com.trs.gov.kpi.entity.wangkang.WkIssue;
import com.trs.gov.kpi.entity.wangkang.WkIssueCount;
import com.trs.gov.kpi.msgqueue.MQListener;
import com.trs.gov.kpi.service.wangkang.WkAllStatsService;
import com.trs.gov.kpi.service.wangkang.WkIssueService;
import com.trs.gov.kpi.utils.DBUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import sun.rmi.runtime.Log;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

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

            synchronized (threadPoolLocker) {
                fixedThreadPool.shutdown();

                while (true){
                    try {
                        final boolean isTerminate = fixedThreadPool.awaitTermination(1, TimeUnit.SECONDS);
                        if (isTerminate) {
                            break;
                        }
                    } catch (InterruptedException e) {
                        log.error("", e);
                        break;
                    }
                }

                fixedThreadPool = Executors.newFixedThreadPool(10);
            }

            CheckEndMsg checkEndMsg = (CheckEndMsg)msg;
            WkAllStats wkAllStats = new WkAllStats();
            wkAllStats.setSiteId(checkEndMsg.getSiteId());
            wkAllStats.setCheckId(checkEndMsg.getCheckId());
            wkAllStats.setErrorInfo(wkIssueService.getErrorWordsCount(checkEndMsg.getSiteId(), checkEndMsg.getCheckId()));
            wkAllStatsService.insertOrUpdateErrorWords(wkAllStats);

            // 比较并记录入库
            compareLastCheckAndInsert(checkEndMsg.getSiteId(), checkEndMsg.getCheckId());

        } else {
            // 监听待检测的内容消息
            CKMProcessWorker worker = appContext.getBean(CKMProcessWorker.class);
            worker.setContent((PageInfoMsg)msg);

            // 把检测内容分配给检测线程
            synchronized (threadPoolLocker) {
                fixedThreadPool.execute(worker);
            }
        }
    }

    /**
     * 同上一次监测结果进行比较，统计出已解决问题个数和现有问题个数，并入统计库
     *
     * @param siteId
     * @param checkId
     */
    private void compareLastCheckAndInsert(Integer siteId, Integer checkId) {

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
