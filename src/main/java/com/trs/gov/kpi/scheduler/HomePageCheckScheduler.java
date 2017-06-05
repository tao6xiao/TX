package com.trs.gov.kpi.scheduler;

import com.trs.gov.kpi.constant.Types;
import com.trs.gov.kpi.dao.IssueMapper;
import com.trs.gov.kpi.entity.Issue;
import com.trs.gov.kpi.utils.SpiderUtils;
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

    @Setter @Getter
    private String baseUrl;

    @Setter @Getter
    private Integer siteId;

    @Resource
    private IssueMapper issueMapper;

    @Override
    public void run() {

        log.info("HomePageCheckScheduler " + siteId + " start...");
        try {
            List<String> unavailableUrls = spider.homePageCheck(baseUrl);
            if(unavailableUrls.contains(baseUrl)) {
                Issue issue = new Issue();
                issue.setSiteId(siteId);
                issue.setSubTypeId(Types.LinkAvailableIssueType.INVALID_HOME_PAGE.value);
                issue.setTypeId(Types.IssueType.LINK_AVAILABLE_ISSUE.value);
                issue.setDetail(baseUrl);
                issue.setCustomer1(baseUrl);
                issue.setIssueTime(new Date());
                issueMapper.insert(issue);
            }
        } finally {
            log.info("HomePageCheckScheduler " + siteId + " end...");
        }

    }
}
