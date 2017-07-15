package com.trs.gov.kpi.scheduler;

import com.trs.gov.kpi.constant.Constants;
import com.trs.gov.kpi.constant.Types;
import com.trs.gov.kpi.dao.CommonMapper;
import com.trs.gov.kpi.dao.WebPageMapper;
import com.trs.gov.kpi.entity.dao.DBUpdater;
import com.trs.gov.kpi.entity.dao.QueryFilter;
import com.trs.gov.kpi.entity.dao.Table;
import com.trs.gov.kpi.entity.msg.CheckEndMsg;
import com.trs.gov.kpi.entity.wangkang.SiteManagement;
import com.trs.gov.kpi.entity.wangkang.WkCheckTime;
import com.trs.gov.kpi.msgqueue.CommonMQ;
import com.trs.gov.kpi.service.LinkAvailabilityService;
import com.trs.gov.kpi.service.WebPageService;
import com.trs.gov.kpi.service.wangkang.WkIdService;
import com.trs.gov.kpi.utils.DBUtil;
import com.trs.gov.kpi.utils.PageSpider;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * Created by wangxuan on 2017/5/10.
 * 定时任务，抓取网站链接是否可用
 */
@Slf4j
@Component
@Scope("prototype")
public class LinkAnalysisScheduler implements SchedulerTask {
    @Resource
    private CommonMQ commonMQ;

    @Resource
    private CommonMapper commonMapper;

    @Resource
    private LinkAvailabilityService linkAvailabilityService;

    @Resource
    private WkIdService wkIdService;

    @Resource
    PageSpider pageSpider;

    @Resource
    WebPageService webPageService;

    @Resource
    WebPageMapper webPageMapper;

    @Setter
    @Getter
    private Integer siteId;

    @Setter
    @Getter
    private String baseUrl;

    @Setter
    @Getter
    private Boolean isTimeNode;

    @Getter
    @Setter
    private SiteManagement site;

    private int checkId;

    @Override
    public void run() {

        log.info("LinkAnalysisScheduler " + site.getSiteId() + " start...");
        try {
            checkId = wkIdService.getNewCheckId();

            insertCheckTime();

            pageSpider.setSite(site);
            pageSpider.setCheckId(checkId);

            List<Pair<String, String>> unavailableUrlAndParentUrls = pageSpider.fetchAllPages(5, site.getSiteIndexUrl());

            CheckEndMsg endMsg = new CheckEndMsg();
            endMsg.setCheckId(checkId);
            endMsg.setSiteId(site.getSiteId());
            commonMQ.publishMsg(endMsg);

            updateCheckEndTime();

        } catch (Exception e) {
            log.error("check link:{}, siteId:{} availability error!", baseUrl, siteId, e);
        } finally {
            log.info("LinkAnalysisScheduler " + siteId + " end...");
        }
    }

    private void updateCheckEndTime() {
        DBUpdater updater = new DBUpdater(Table.WK_CHECK_TIME.getTableName());
        updater.addField("endTime", new Date());
        updater.addField("checkStatus", WkCheckTime.CHECK_END);
        QueryFilter filter = new QueryFilter(Table.WK_CHECK_TIME);
        filter.addCond(Constants.DB_FIELD_CHECK_ID, checkId);
        filter.addCond(Constants.DB_FIELD_SITE_ID, site.getSiteId());
        commonMapper.update(updater, filter);
    }

    private void insertCheckTime() {
        WkCheckTime wkCheckTime = new WkCheckTime();
        wkCheckTime.setBeginTime(new Date());
        wkCheckTime.setSiteId(site.getSiteId());
        wkCheckTime.setCheckId(checkId);
        wkCheckTime.setEndTime(new Date());
        commonMapper.insert(DBUtil.toRow(wkCheckTime));
    }

    @Override
    public Boolean getIsTimeNode() {
        return null;
    }

    @Override
    public void setIsTimeNode(Boolean isTimeNode) {

    }

    private String[] imageSuffixs = new String[]{"bmp", "jpg", "jpeg", "png", "gif"};

    private String[] fileSuffixs = new String[]{"zip", "doc", "xls", "xlsx", "docx", "rar"};

    private Types.LinkAvailableIssueType getTypeByLink(String url) {

        String suffix = url.substring(url.lastIndexOf('.') + 1);
        for (String imageSuffix : imageSuffixs) {

            if (StringUtils.equalsIgnoreCase(suffix, imageSuffix)) {

                return Types.LinkAvailableIssueType.INVALID_IMAGE;
            }
        }

        for (String fileSuffix : fileSuffixs) {

            if (StringUtils.equalsIgnoreCase(suffix, fileSuffix)) {

                return Types.LinkAvailableIssueType.INVALID_FILE;
            }
        }

        return Types.LinkAvailableIssueType.INVALID_LINK;
    }
}
