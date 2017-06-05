package com.trs.gov.kpi.utils;

import com.trs.gov.kpi.constant.Types;
import com.trs.gov.kpi.entity.Issue;

import java.util.List;

/**
 * Issue传入的issue对象参数数据处理
 * Created by he.lang on 2017/5/19.
 */
public class IssueDataUtil {

    private IssueDataUtil() {

    }

    /**
     * 用于返回数据的处理，需要用subTypeId去匹配对应得subTypeName
     *
     * @param issueList
     * @return
     */
    public static List<Issue> getIssueListToSetSubTypeName(List<Issue> issueList) {
        for (Issue issue : issueList) {
            if (issue.getTypeId() == null || issue.getSubTypeId() == null) {
                continue;
            }
            issue.setSubTypeName(Types.getSubTypeName(
                    Types.IssueType.valueOf(issue.getTypeId()), issue.getSubTypeId()));
        }
        return issueList;
    }
}
