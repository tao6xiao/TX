package com.trs.gov.kpi.scheduler;

import com.trs.gov.kpi.constant.Types;
import com.trs.gov.kpi.dao.CommonMapper;
import com.trs.gov.kpi.dao.WebPageMapper;
import com.trs.gov.kpi.entity.msg.CheckEndMsg;
import com.trs.gov.kpi.entity.wangkang.SiteManagement;
import com.trs.gov.kpi.entity.wangkang.WkCheckTime;
import com.trs.gov.kpi.entity.wangkang.WkLinkType;
import com.trs.gov.kpi.msgqueue.CommonMQ;
import com.trs.gov.kpi.service.WebPageService;
import com.trs.gov.kpi.service.wangkang.WkIdService;
import com.trs.gov.kpi.service.wangkang.WkSiteManagementService;
import com.trs.gov.kpi.utils.DBUtil;
import com.trs.gov.kpi.utils.PageSpider;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Map;

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
    private WkIdService wkIdService;

    @Resource
    private WkSiteManagementService wkSiteManagementService;

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

            wkSiteManagementService.changeSiteStatus(site.getSiteId(), Types.WkCheckStatus.CONDUCT_CHECK);

            pageSpider.setSite(site);
            pageSpider.setCheckId(checkId);

            pageSpider.fetchAllPages(5, site.getSiteIndexUrl());

            final Map<Types.WkLinkIssueType, Integer> linkCountMap = pageSpider.getLinkCountMap();
            WkLinkType wkLinkType = new WkLinkType();
            wkLinkType.setSiteId(site.getSiteId());
            wkLinkType.setCheckId(checkId);
            wkLinkType.setCheckTime(new Date());
            wkLinkType.setEnclosuLink(linkCountMap.get(Types.WkLinkIssueType.ENCLOSURE_DISCONNECT));
            wkLinkType.setImageLink(linkCountMap.get(Types.WkLinkIssueType.IMAGE_DISCONNECT));
            wkLinkType.setVideoLink(linkCountMap.get(Types.WkLinkIssueType.VIDEO_DISCONNECT));
            wkLinkType.setWebLink(linkCountMap.get(Types.WkLinkIssueType.LINK_DISCONNECT));
            wkLinkType.setOthersLink(linkCountMap.get(Types.WkLinkIssueType.OTHERS_DISCONNECT));
            wkLinkType.calcTotal();

            commonMapper.insert(DBUtil.toRow(wkLinkType));

            CheckEndMsg endMsg = new CheckEndMsg();
            endMsg.setCheckId(checkId);
            endMsg.setSiteId(site.getSiteId());
            commonMQ.publishMsg(endMsg);
            log.info("LinkAnalysisScheduler " + siteId + " send end msg");
        } catch (Exception e) {
            log.error("check link:{}, siteId:{} availability error!", baseUrl, siteId, e);
        } finally {
            log.info("LinkAnalysisScheduler " + siteId + " end...");
        }
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
        return true;
    }

    @Override
    public void setIsTimeNode(Boolean isTimeNode) {

    }

    private String[] imageSuffixs = new String[]{"bmp", "jpg", "jpeg", "png", "gif"};

    private String[] fileSuffixs = new String[]{"zip", "doc", "xls", "xlsx", "docx", "rar", "pdf"};

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
