package com.trs.gov.kpi.entity.dao;

import com.trs.gov.kpi.constant.IssueTableField;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by he.lang on 2017/6/16.
 */
public class QueryFilterTest {
    @Test
    public void removeCond() throws Exception {
        QueryFilter filter = new QueryFilter(Table.ISSUE);
        filter.addCond(IssueTableField.SITE_ID, 1).setRangeBegin(true);
        filter.addCond(IssueTableField.SITE_ID, 10).setRangeEnd(true);
        assertTrue(filter.removeCond(IssueTableField.SITE_ID));
    }

}