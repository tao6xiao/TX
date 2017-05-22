package com.trs.gov.kpi.utils;

import com.trs.gov.kpi.constant.*;
import com.trs.gov.kpi.entity.Issue;
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
     * @param issue
     * @param operationService
     * @return
     */
    public static Issue getIssueToGetPageData(Issue issue, OperationService operationService,Integer isResolved, Integer isDel){
        //接受searchField和searchText，并返回对应的问题类型ID集合
        if (issue.getSearchText() != null && !issue.getSearchText().trim().isEmpty()) {
            List list = InitQueryFiled.init(issue.getSearchText(), operationService);
            if(list.size() == 0 || list == null){
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
        return  issue;
    }

    /**
     * 用于返回数据的处理，需要用subTypeId去匹配对应得subTypeName
     * @param issueList
     * @return
     */
    public static List<Issue> getIssueListToSetSubTypeName(List<Issue> issueList){
        for (Issue issue : issueList) {
            if (issue.getTypeId() == IssueType.AVAILABLE_ISSUE.getCode()) {//为可用性链接问题
                if (issue.getSubTypeId() == LinkIssueType.INVALID_LINK.value) {
                    issue.setSubTypeName(LinkIssueType.INVALID_LINK.name);
                } else if (issue.getSubTypeId() == LinkIssueType.INVALID_IMAGE.value) {
                    issue.setSubTypeName(LinkIssueType.INVALID_IMAGE.name);
                } else if (issue.getSubTypeId() == LinkIssueType.CONNECTION_TIME_OUT.value) {
                    issue.setSubTypeName(LinkIssueType.CONNECTION_TIME_OUT.name);
                }
            } else if (issue.getTypeId() == IssueType.UPDATE_ISSUE.getCode()) {//为信息更新问题
                if (issue.getSubTypeId() == InfoUpdateType.UPDATE_NOT_INTIME.value) {
                    issue.setSubTypeName(InfoUpdateType.UPDATE_NOT_INTIME.name);
                }
            } else if (issue.getTypeId() == IssueType.INFO_ISSUE.getCode()) {//为信息错误问题
                if (issue.getSubTypeId() == InfoErrorType.TYPOS.value) {
                    issue.setSubTypeName(InfoErrorType.TYPOS.name);
                } else if (issue.getSubTypeId() == InfoErrorType.SENSITIVE_WORDS.value) {
                    issue.setSubTypeName(InfoErrorType.SENSITIVE_WORDS.name);
                }
            } else if (issue.getTypeId() == IssueType.INFO_UPDATE_WARNING.getCode()) {//信息更新预警
                if(issue.getSubTypeId() == InfoWarningType.UPDATE_WARNING.value){
                    issue.setSubTypeName(InfoWarningType.UPDATE_WARNING.name);
                }else if(issue.getSubTypeId() == InfoWarningType.SELF_CHECK_WARNING.value){
                    issue.setSubTypeName(InfoWarningType.SELF_CHECK_WARNING.name);
                }
            }else if(issue.getTypeId() == IssueType.RESPOND_WARNING.getCode()){//互动回应预警
                if(issue.getSubTypeId() == RespondWarningType.RESPOND_WARNING.value){
                    issue.setSubTypeName(RespondWarningType.RESPOND_WARNING.name);
                }else if(issue.getSubTypeId() == RespondWarningType.FEEDBACK_WARNING.value){
                    issue.setSubTypeName(RespondWarningType.FEEDBACK_WARNING.name);
                }
            }
        }
        return issueList;
    }
}
