package com.trs.gov.kpi.utils;

import com.trs.gov.kpi.entity.IssueIndicator;
import com.trs.gov.kpi.entity.IssueBase;
import com.trs.gov.kpi.entity.responsedata.Statistics;
import com.trs.gov.kpi.service.InfoUpdateService;
import com.trs.gov.kpi.service.OperationService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ranwei on 2017/5/15.
 */
public class IssueCounter {

    public static List getIssueCount(OperationService operationService, IssueBase issueBase){
        List list = new ArrayList();
        //判断问题类型是否为信息更新
        if(operationService instanceof InfoUpdateService){
            Statistics updateNotIntimeCount = new Statistics();
            updateNotIntimeCount.setType(IssueIndicator.UPDATE_NOT_INTIME.value);
            updateNotIntimeCount.setName(IssueIndicator.UPDATE_NOT_INTIME.name);
            updateNotIntimeCount.setCount(operationService.getUnhandledIssueCount(issueBase));

            Statistics updateWarningCount =new Statistics();
            updateWarningCount.setType(IssueIndicator.UPDATE_WARNING.value);
            updateWarningCount.setName(IssueIndicator.UPDATE_WARNING.name);
            updateWarningCount.setCount(operationService.getUpdateWarningCount(issueBase));

            list.add(updateNotIntimeCount);
            list.add(updateWarningCount);
        }else{
            Statistics unhandledStatistics = new Statistics();
            unhandledStatistics.setType(IssueIndicator.UN_SOLVED.value);
            unhandledStatistics.setName(IssueIndicator.UN_SOLVED.name);
            unhandledStatistics.setCount(operationService.getUnhandledIssueCount(issueBase));

            list.add(unhandledStatistics);

        }
        Statistics handledStatistics = new Statistics();
        handledStatistics.setType(IssueIndicator.SOLVED.value);
        handledStatistics.setName(IssueIndicator.SOLVED.name);
        handledStatistics.setCount(operationService.getHandledIssueCount(issueBase));

        list.add(handledStatistics);

        return list;
    }
}
