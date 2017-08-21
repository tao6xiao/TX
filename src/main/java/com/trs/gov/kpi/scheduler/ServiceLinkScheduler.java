package com.trs.gov.kpi.scheduler;

import com.trs.gov.kpi.constant.EnumCheckJobType;
import com.trs.gov.kpi.constant.IssueTableField;
import com.trs.gov.kpi.constant.SchedulerType;
import com.trs.gov.kpi.constant.Types;
import com.trs.gov.kpi.dao.CommonMapper;
import com.trs.gov.kpi.dao.IssueMapper;
import com.trs.gov.kpi.entity.Issue;
import com.trs.gov.kpi.entity.dao.DBUpdater;
import com.trs.gov.kpi.entity.dao.QueryFilter;
import com.trs.gov.kpi.entity.dao.Table;
import com.trs.gov.kpi.entity.exception.BizException;
import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.gov.kpi.entity.outerapi.Site;
import com.trs.gov.kpi.entity.outerapi.sp.ServiceGuide;
import com.trs.gov.kpi.service.outer.SGService;
import com.trs.gov.kpi.service.outer.SiteApiService;
import com.trs.gov.kpi.utils.DBUtil;
import com.trs.gov.kpi.utils.ServiceLinkSpiderUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;

/**
 * 服务链接是否可用
 * Created by he.lang on 2017/7/12.
 */
@Slf4j
@Component
@Scope("prototype")
public class ServiceLinkScheduler implements SchedulerTask {

    @Setter
    @Getter
    private Integer siteId;

    @Setter
    @Getter
    private Boolean isTimeNode;

    @Setter
    @Getter
    private String baseUrl;

    @Resource
    IssueMapper issueMapper;

    @Resource
    ServiceLinkSpiderUtil spider;

    @Resource
    SGService sgService;

    @Resource
    private CommonMapper commonMapper;

    @Resource
    private SiteApiService siteApiService;

    //错误信息计数
    @Getter
    Integer monitorResult = 0;

    //站点监测状态（0：自动监测；1：手动监测）
    @Setter
    @Getter
    private Integer monitorType;

    @Getter
    private EnumCheckJobType checkJobType = EnumCheckJobType.SERVICE_LINK;

    @Override
    public void run() throws RemoteException, BizException {
        Site checkSite = siteApiService.getSiteById(siteId, "");
        if (checkSite == null) {
            String errorInfo = "任务调度[" + getName() + "]，站点[" + siteId + "]不存在";
            log.error(errorInfo);
            throw new BizException(errorInfo);
        }
        for (ServiceGuide guide : sgService.getAllService(siteId).getData()) {
            if (spider.linkCheck(guide.getItemLink()) == Types.ServiceLinkIssueType.INVALID_LINK) {
                QueryFilter queryFilter = new QueryFilter(Table.ISSUE);
                queryFilter.addCond(IssueTableField.SITE_ID, siteId);
                queryFilter.addCond(IssueTableField.TYPE_ID, Types.IssueType.SERVICE_LINK_AVAILABLE);
                queryFilter.addCond(IssueTableField.DETAIL, guide.getItemLink());
                int issueCount = issueMapper.count(queryFilter);
                if (issueCount < 1) {
                    Issue issue = new Issue();
                    issue.setSiteId(siteId);
                    issue.setSubTypeId(Types.ServiceLinkIssueType.INVALID_LINK.value);
                    issue.setTypeId(Types.IssueType.SERVICE_LINK_AVAILABLE.value);
                    issue.setDetail(guide.getItemLink());
                    issue.setCustomer1(guide.getItemLink());
                    Date nowTime = new Date();
                    issue.setIssueTime(nowTime);
                    issue.setCheckTime(nowTime);
                    issueMapper.insert(DBUtil.toRow(issue));
                } else {
                    DBUpdater updater = new DBUpdater(Table.ISSUE.getTableName());
                    updater.addField(IssueTableField.CHECK_TIME, new Date());
                    commonMapper.update(updater, queryFilter);
                }
                monitorResult++;
            }
        }
    }

    @Override
    public String getName() {
        return SchedulerType.SERVICE_LINK_SCHEDULER.toString();
    }

}

