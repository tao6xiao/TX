package com.trs.gov.kpi.utils;

import com.trs.gov.kpi.entity.CountIndicator;
import com.trs.gov.kpi.entity.IssueCount;
import com.trs.gov.kpi.service.InfoUpdateService;
import com.trs.gov.kpi.service.OperationService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ranwei on 2017/5/15.
 */
public class IssueCounter {

    public static List getIssueCount(OperationService operationService,int siteId){
        List list = new ArrayList();
        //判断问题类型是否为信息更新
        if(operationService instanceof InfoUpdateService){
            IssueCount updateNotIntimeCount = new IssueCount();
            updateNotIntimeCount.setType(CountIndicator.UPDATE_NOT_INTIME.value);
            updateNotIntimeCount.setName(CountIndicator.UPDATE_NOT_INTIME.name);
            updateNotIntimeCount.setCount(operationService.getUpdateNotIntimeCount(siteId));

            IssueCount updateWarningCount =new IssueCount();
            updateWarningCount.setType(CountIndicator.UPDATE_WARNING.value);
            updateWarningCount.setName(CountIndicator.UPDATE_WARNING.name);
            updateWarningCount.setCount(operationService.getUpdateWarningCount(siteId));

            list.add(updateNotIntimeCount);
            list.add(updateWarningCount);
        }else{
            IssueCount unhandledIssueCount = new IssueCount();
            unhandledIssueCount.setType(CountIndicator.UN_SOLVED.value);
            unhandledIssueCount.setName(CountIndicator.UN_SOLVED.name);
            unhandledIssueCount.setCount(operationService.getUnhandledIssueCount(siteId));

            list.add(unhandledIssueCount);

        }
        IssueCount handledIssueCount = new IssueCount();
        handledIssueCount.setType(CountIndicator.SOLVED.value);
        handledIssueCount.setName(CountIndicator.SOLVED.name);
        handledIssueCount.setCount(operationService.getHandledIssueCount(siteId));

        list.add(handledIssueCount);

        return list;
    }
}
