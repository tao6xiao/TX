package com.trs.gov.kpi.scheduler;

import com.trs.gov.kpi.constant.*;
import com.trs.gov.kpi.dao.CommonMapper;
import com.trs.gov.kpi.dao.IssueMapper;
import com.trs.gov.kpi.entity.Issue;
import com.trs.gov.kpi.entity.dao.DBUpdater;
import com.trs.gov.kpi.entity.dao.QueryFilter;
import com.trs.gov.kpi.entity.dao.Table;
import com.trs.gov.kpi.entity.exception.BizException;
import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.gov.kpi.service.MonitorRecordService;
import com.trs.gov.kpi.service.outer.SiteApiService;
import com.trs.gov.kpi.utils.DBUtil;
import com.trs.gov.kpi.utils.OuterApiServiceUtil;
import com.trs.gov.kpi.utils.SpiderUtils;
import com.trs.gov.kpi.utils.StringUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * Created by wangxuan on 2017/5/11.
 */
@Slf4j
@Component
@Scope("prototype")
public class HomePageCheckScheduler implements SchedulerTask {

    @Resource
    SpiderUtils spider;

    @Resource
    private SiteApiService siteApiService;

    @Setter
    @Getter
    private String baseUrl;

    @Setter
    @Getter
    private Boolean isTimeNode;

    @Setter
    @Getter
    private Integer siteId;

    @Resource
    private IssueMapper issueMapper;

    @Resource
    private MonitorRecordService monitorRecordService;

    @Resource
    private CommonMapper commonMapper;

    //错误信息计数
    @Getter
    Integer monitorResult = 0;

    //站点监测状态（0：自动监测；1：手动监测）
    @Setter
    @Getter
    private Integer monitorType = 0;

    @Getter
    private EnumCheckJobType checkJobType = EnumCheckJobType.CHECK_HOME_PAGE;

    @Override
    public void run() throws RemoteException, BizException {

        baseUrl = OuterApiServiceUtil.getUrl(siteApiService.getSiteById(siteId, null));
        if (StringUtil.isEmpty(baseUrl)) {
            return;
        }

        List<String> unavailableUrls = spider.homePageCheck(siteId, baseUrl);
        if (unavailableUrls.contains(baseUrl)) {
            monitorResult = 1;
            QueryFilter queryFilter = new QueryFilter(Table.ISSUE);
            queryFilter.addCond(IssueTableField.SITE_ID, siteId);
            queryFilter.addCond(IssueTableField.TYPE_ID, Types.IssueType.LINK_AVAILABLE_ISSUE.value);
            queryFilter.addCond(IssueTableField.SUBTYPE_ID, Types.LinkAvailableIssueType.INVALID_HOME_PAGE.value);
            queryFilter.addCond(IssueTableField.DETAIL, baseUrl);
            queryFilter.addCond(IssueTableField.IS_DEL, Status.Delete.UN_DELETE.value);
            queryFilter.addCond(IssueTableField.IS_RESOLVED, Status.Resolve.UN_RESOLVED.value);
            int count = issueMapper.count(queryFilter);
            if (count < 1) {
                Issue issue = new Issue();
                issue.setSiteId(siteId);
                issue.setSubTypeId(Types.LinkAvailableIssueType.INVALID_HOME_PAGE.value);
                issue.setTypeId(Types.IssueType.LINK_AVAILABLE_ISSUE.value);
                issue.setDetail(baseUrl);
                issue.setCustomer1(baseUrl);
                issue.setIssueTime(new Date());
                issueMapper.insert(DBUtil.toRow(issue));
            } else {
                DBUpdater updater = new DBUpdater(Table.ISSUE.getTableName());
                updater.addField(IssueTableField.CHECK_TIME, new Date());
                commonMapper.update(updater, queryFilter);
            }
        }
    }

    @Override
    public String getName() {
        return SchedulerType.HOMEPAGE_CHECK_SCHEDULER.toString();
    }

}
