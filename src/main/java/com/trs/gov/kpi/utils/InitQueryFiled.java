package com.trs.gov.kpi.utils;


import com.trs.gov.kpi.constant.*;
import com.trs.gov.kpi.service.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 将searchText参数由String转换成匹配的Integer的集合
 */
public class InitQueryFiled {

    public static List<Integer> init(String queryFiled, OperationService operationService) {
        List<Integer> list = new ArrayList<>();
        if (operationService instanceof LinkAvailabilityService) {
            for (LinkIssueType type : LinkIssueType.values()) {
                if (type.name.contains(queryFiled)) {
                    list.add(type.value);
                }
            }
        } else if (operationService instanceof InfoUpdateService) {
            for (InfoUpdateType type : InfoUpdateType.values()) {
                if (type.name.contains(queryFiled)) {
                    list.add(type.value);
                }
            }
        } else if (operationService instanceof InfoErrorService) {
            for (InfoErrorType type : InfoErrorType.values()) {
                if (type.name.contains(queryFiled)){
                    list.add(type.value);
                }
            }
        }else if (operationService instanceof IssueService){
            for (LinkIssueType type : LinkIssueType.values()) {
                if (type.name.contains(queryFiled)) {
                    list.add(type.value);
                }
            }
            for (InfoUpdateType type : InfoUpdateType.values()) {
                if (type.name.contains(queryFiled)) {
                    list.add(type.value);
                }
            }
            for (InfoErrorType type : InfoErrorType.values()) {
                if (type.name.contains(queryFiled)){
                    list.add(type.value);
                }
            }
        }else if(operationService instanceof IntegratedMonitorWarningService){//预警的name和对应得value匹配
            for (InfoWarningType type : InfoWarningType.values()) {
                if(type.name.contains(queryFiled)){
                    list.add(type.value);
                }
            }
            for (RespondWarningType type : RespondWarningType.values()) {
                if(type.name.contains(queryFiled)){
                    list.add(type.value);
                }
            }
        }
        return list;
    }
}
