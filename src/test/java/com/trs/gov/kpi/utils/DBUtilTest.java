package com.trs.gov.kpi.utils;

import com.trs.gov.kpi.constant.Types;
import com.trs.gov.kpi.entity.Issue;
import com.trs.gov.kpi.entity.dao.DBRow;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

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

}