package com.trs.gov.kpi.dao;

import com.trs.gov.kpi.config.TestConfigConst;
import com.trs.gov.kpi.constant.IssueTableField;
import com.trs.gov.kpi.constant.Types;
import com.trs.gov.kpi.entity.InfoUpdate;
import com.trs.gov.kpi.entity.Issue;
import com.trs.gov.kpi.entity.dao.QueryFilter;
import com.trs.gov.kpi.entity.dao.Table;
import com.trs.gov.kpi.utils.DBUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

import static junit.framework.TestCase.assertEquals;

/**
 * Created by linwei on 2017/5/25.
 */

@RunWith(SpringRunner.class)
@MybatisTest
@TestPropertySource(properties = {TestConfigConst.TEST_DB_URL_PROP, TestConfigConst.TEST_DB_USER_NAME_PROP, TestConfigConst.TEST_DB_PWD_PROP})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class IssueMapperTest {

    @Resource
    private IssueMapper issueMapper;


    private int testSiteId = 11111111;

    @Test
    @Rollback
    public void insert() throws Exception {
        String url = "http://www.baidu.com";
        Issue issue = new Issue();
        issue.setTypeId(Types.IssueType.INFO_UPDATE_ISSUE.value);
        issue.setSubTypeId(Types.InfoUpdateIssueType.UPDATE_NOT_INTIME.value);
        issue.setSiteId(testSiteId);
        issue.setDetail(url);
        issue.setIssueTime(new Date());
        issue.setCustomer2("72");
        issueMapper.insert(DBUtil.toRow(issue));

        QueryFilter filter = new QueryFilter(Table.ISSUE);
        filter.addCond(IssueTableField.TYPE_ID, Types.IssueType.INFO_UPDATE_ISSUE.value);
        filter.addCond(IssueTableField.SUBTYPE_ID, Types.InfoUpdateIssueType.UPDATE_NOT_INTIME.value);
        filter.addCond(IssueTableField.SITE_ID, testSiteId);
        final List<Issue> issues = issueMapper.select(filter);
        assertEquals(1, issues.size());
        assertEquals(url, issues.get(0).getDetail());
        assertEquals("72", issues.get(0).getCustomer2());
    }

    @Test
    @Rollback
    public void insertInfoUpdate() throws Exception {
        int chnlId = 1111;
        String url = "http://www.baidu.com";
        InfoUpdate update = new InfoUpdate();
        update.setTypeId(Types.IssueType.INFO_UPDATE_ISSUE.value);
        update.setSubTypeId(Types.InfoUpdateIssueType.UPDATE_NOT_INTIME.value);
        update.setIssueTime(new Date());
        update.setChnlUrl(url);
        update.setChnlId(chnlId);
        update.setSiteId(testSiteId);
        issueMapper.insert(DBUtil.toRow(update));

        QueryFilter filter = new QueryFilter(Table.ISSUE);
        filter.addCond(IssueTableField.TYPE_ID, Types.IssueType.INFO_UPDATE_ISSUE.value);
        filter.addCond(IssueTableField.SUBTYPE_ID, Types.InfoUpdateIssueType.UPDATE_NOT_INTIME.value);
        filter.addCond(IssueTableField.SITE_ID, testSiteId);
        final List<Issue> issues = issueMapper.select(filter);
        assertEquals(1, issues.size());
        assertEquals(String.valueOf(chnlId), issues.get(0).getCustomer2());
        assertEquals(url, issues.get(0).getDetail());
    }

}