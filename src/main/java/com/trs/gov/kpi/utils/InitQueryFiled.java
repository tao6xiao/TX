package com.trs.gov.kpi.utils;


import com.trs.gov.kpi.constant.InfoErrorType;
import com.trs.gov.kpi.constant.InfoUpdateType;
import com.trs.gov.kpi.constant.LinkIssueType;
import com.trs.gov.kpi.service.InfoErrorService;
import com.trs.gov.kpi.service.InfoUpdateService;
import com.trs.gov.kpi.service.LinkAvailabilityService;
import com.trs.gov.kpi.service.OperationService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ranwei on 2017/5/17.
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
        }
        return list;
    }
}
