package com.trs.gov.kpi.utils;

import com.sun.webkit.WebPage;
import com.trs.gov.kpi.constant.Constants;
import com.trs.gov.kpi.constant.Types;
import com.trs.gov.kpi.entity.*;
import com.trs.gov.kpi.entity.dao.DBRow;
import com.trs.gov.kpi.entity.dao.DBUpdater;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by linwei on 2017/6/7.
 */
public class DBUtilTest {
    @Test
    public void toRow() throws Exception {

        //非数据库po
        DBRow row = DBUtil.toRow(new Integer(0));
        assertEquals(null, row);

        Issue issue = new Issue();
        issue.setTypeId(Types.IssueType.INFO_ERROR_ISSUE.value);
        issue.setSubTypeId(Types.InfoErrorIssueType.TYPOS.value);

        row = DBUtil.toRow(issue);
        assertEquals(5, row.getFields().size());

        for (int index = 0; index < row.getFields().size(); index++) {
            if (row.getFields().get(index).equals("typeId")) {
                assertEquals(Types.IssueType.INFO_ERROR_ISSUE.value, row.getValues().get(index));
            }
        }
    }


    @Test
    public void getAllDBFieldNames_issue_table() {

        List<String> fields = Arrays.asList("id", "siteId", "typeId", "subTypeId",
                "detail", "issueTime", "checkTime", "isResolved", "isDel", "workOrderStatus", "deptId", "customer1", "customer2", "customer3");
        final List<String> allDBFieldNames = DBUtil.getAllDBFieldNames(Issue.class);

        assertTrue(fields.containsAll(allDBFieldNames));
        assertTrue(allDBFieldNames.containsAll(fields));
    }

    @Test
    public void getAllDBFieldNames_webPage_table() {

        List<String> fields = Arrays.asList(Constants.DB_FIELD_ID, Constants.DB_FIELD_SITE_ID, "typeId", Constants.DB_FIELD_CHNL_ID,
                "pageLink", "replySpeed", "pageSpace", "pageDepth", "repeatPlace", "repeatDegree", "updateTime", "urlLength", "checkTime", "isResolved", "isDel");
        List<String> allDBFieldNames = DBUtil.getAllDBFieldNames(PageIssue.class);
        assertTrue(fields.containsAll(allDBFieldNames));

        allDBFieldNames = DBUtil.getAllDBFieldNames(UrlLength.class);
        assertTrue(fields.containsAll(allDBFieldNames));

        allDBFieldNames = DBUtil.getAllDBFieldNames(PageDepth.class);
        assertTrue(fields.containsAll(allDBFieldNames));

        allDBFieldNames = DBUtil.getAllDBFieldNames(PageSpace.class);
        assertTrue(fields.containsAll(allDBFieldNames));

        allDBFieldNames = DBUtil.getAllDBFieldNames(ReplySpeed.class);
        assertTrue(fields.containsAll(allDBFieldNames));

        allDBFieldNames = DBUtil.getAllDBFieldNames(RepeatCode.class);
        assertTrue(fields.containsAll(allDBFieldNames));
    }

    @Test
    public void collectAllDBFieldNames() {
        List<String> fields = Arrays.asList(Constants.DB_FIELD_ID, Constants.DB_FIELD_SITE_ID, "typeId", Constants.DB_FIELD_CHNL_ID,
                "pageLink", "replySpeed", "pageSpace", "pageDepth", "repeatPlace", "repeatDegree", "updateTime", "urlLength", "checkTime", "isResolved", "isDel");
        List<String> allDBFieldNames = DBUtil.collectAllDBFieldNames(PageIssue.class, UrlLength.class, PageDepth.class,
                PageSpace.class, ReplySpeed.class, RepeatCode.class);

        assertTrue(fields.containsAll(allDBFieldNames));
    }

    @Test
    public void getAllDBFieldNames_frequencysetup_table() {

        List<String> fields = Arrays.asList(Constants.DB_FIELD_ID, Constants.DB_FIELD_SITE_ID, "presetFeqId", Constants.DB_FIELD_CHNL_ID, "setTime", "isOpen");
        final List<String> allDBFieldNames = DBUtil.getAllDBFieldNames(FrequencySetup.class);

        assertTrue(fields.containsAll(allDBFieldNames));
        assertTrue(allDBFieldNames.containsAll(fields));
    }

    @Test
    public void getAllDBFieldNames_report_table() {

        List<String> fields = Arrays.asList("id", "siteId", "title", "reportTime", "crTime", "type", "path");
        final List<String> allDBFieldNames = DBUtil.getAllDBFieldNames(Report.class);

        assertTrue(fields.containsAll(allDBFieldNames));
        assertTrue(allDBFieldNames.containsAll(fields));
    }

    @Test
    public void getAllDBFieldNames_dutydept_table() {

        List<String> fields = Arrays.asList(Constants.DB_FIELD_CHNL_ID, "siteId", "deptId", "contain");
        final List<String> allDBFieldNames = DBUtil.getAllDBFieldNames(DutyDept.class);

        assertTrue(fields.containsAll(allDBFieldNames));
        assertTrue(allDBFieldNames.containsAll(fields));
    }

    @Test
    public void getAllDBFieldNames_monitorrecord_table() {

        List<String> fields = Arrays.asList(Constants.DB_FIELD_ID, Constants.DB_FIELD_SITE_ID, "taskId", "taskStatus", "result", "typeId", "beginTime", "endTime");
        final List<String> allDBFieldNames = DBUtil.getAllDBFieldNames(MonitorRecord.class);

        assertTrue(fields.containsAll(allDBFieldNames));
        assertTrue(allDBFieldNames.containsAll(fields));
    }

    @Test
    public void getAllDBFieldNames_linkcontentstats_table() {

        List<String> fields = Arrays.asList(Constants.DB_FIELD_SITE_ID, "typeId", "url", "md5", "checkTime", "infoErrorCount");
        final List<String> allDBFieldNames = DBUtil.getAllDBFieldNames(LinkContentStats.class);

        assertTrue(fields.containsAll(allDBFieldNames));
        assertTrue(allDBFieldNames.containsAll(fields));
    }

}