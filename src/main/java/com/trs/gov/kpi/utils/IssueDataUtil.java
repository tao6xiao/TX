package com.trs.gov.kpi.utils;

import com.trs.gov.kpi.constant.*;
import com.trs.gov.kpi.entity.Issue;
import com.trs.gov.kpi.entity.IssueBase;
import com.trs.gov.kpi.service.OperationService;

import java.util.ArrayList;
import java.util.List;

/**
 * Issue传入的issue对象参数数据处理
 * Created by he.lang on 2017/5/19.
 */
public class IssueDataUtil {

    /**
     * 用于获取前端数据后的处理，通过传入的指定的数据String类型对应指定的service将searchText转为List，给issue对象传入指定类型的List集合，以及处理其他属性
     *
     * @param issue
     * @param operationService
     * @return
     */
    public static IssueBase getIssueToGetPageData(IssueBase issue, OperationService operationService, Integer isResolved, Integer isDel) {
        //接受searchField和searchText，并返回对应的问题类型ID集合
        if (issue.getSearchText() != null && !issue.getSearchText().trim().isEmpty()) {
            List list = InitQueryFiled.init(issue.getSearchText(), operationService);
            if (list.size() == 0 || list == null) {
                list.add(0);
            }
            issue.setIds(list);
        }
        //解决searchField和searchText为null或空字符串的情况
        if (issue.getSearchText() == null || "".equals(issue.getSearchText())) {
            List<Integer> list = new ArrayList<>();
            Integer exception = 0;
            list.add(exception);
            issue.setIds(list);
        }
        //解决searchText为null的情况，避免不合法的sql查询
        if (issue.getSearchText() == null) {
            issue.setSearchText("");
        }
        //增加查询条件：未解决、未删除
        issue.setIsResolved(isResolved);
        issue.setIsDel(isDel);
        return issue;
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
