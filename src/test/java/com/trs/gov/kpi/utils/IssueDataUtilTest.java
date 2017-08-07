package com.trs.gov.kpi.utils;

import com.trs.gov.kpi.entity.Issue;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by ranwei on 2017/6/13.
 */
public class IssueDataUtilTest {
    @Test
    public void getIssueListToSetSubTypeName() throws Exception {
        List<Issue> issueList = new ArrayList<>();
        Issue issue0 = new Issue();
        issue0.setTypeId(1);
        issueList.add(issue0);

        Issue issue1 = new Issue();
        issue1.setSubTypeId(11);
        issueList.add(issue1);

        Issue issue2 = new Issue();
        issue2.setTypeId(4);
        issue2.setSubTypeId(41);
        issueList.add(issue2);

        List<Issue> list = IssueDataUtil.getIssueListToSetSubTypeName(issueList);

        assertEquals(null, list.get(0).getSubTypeName());
        assertEquals(null, list.get(1).getSubTypeName());
        assertEquals("服务链接失效", list.get(2).getSubTypeName());
    }

}