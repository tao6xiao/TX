package com.trs.gov.kpi.service.helper;

import com.trs.gov.kpi.entity.dao.QueryFilter;
import com.trs.gov.kpi.entity.requestdata.IssueCountRequest;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by he.lang on 2017/6/8.
 */
public class QueryFilterHelperTest {
    @Test
    public void toFilter() throws Exception {
        IssueCountRequest request = new IssueCountRequest();
        request.setBeginDateTime("2017-06-07 00:00:00");
        request.setEndDateTime("2017-06-08 00:00:00");
        QueryFilter filter =  QueryFilterHelper.toFilter(request);
        assertTrue(!filter.getCondFields().isEmpty());

    }

}