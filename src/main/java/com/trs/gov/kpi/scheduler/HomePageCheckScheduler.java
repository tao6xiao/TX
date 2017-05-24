package com.trs.gov.kpi.scheduler;

import com.trs.gov.kpi.constant.IssueType;
import com.trs.gov.kpi.constant.LinkIssueType;
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
public class HomePageCheckScheduler extends AbstractScheduler {

    @Resource
    SpiderUtils spider;

    @Setter @Getter
    private String baseUrl;

    @Setter @Getter
    private Integer siteId;

    @Resource
    private IssueMapper issueMapper;

    @Getter
    private final Runnable task = new Runnable() {

        @Override
        public void run() {

            log.info("HomePageCheckScheduler " + String.valueOf(siteId) + " start...");
            try {
                List<String> unavailableUrls = spider.homePageCheck(baseUrl);
                if(unavailableUrls.contains(baseUrl)) {

                    Issue issue = new Issue();
                    issue.setSiteId(siteId);
                    issue.setSubTypeId(LinkIssueType.INVALID_HOME_PAGE.value);
                    issue.setTypeId(IssueType.AVAILABLE_ISSUE.getCode());
                    issue.setDetail(baseUrl);
                    issue.setCustomer1(baseUrl);
                    issue.setIssueTime(new Date());
                    issueMapper.insert(issue);
                }
            } finally {
                log.info("HomePageCheckScheduler " + String.valueOf(siteId) + " end...");
            }

        }
    };
}
