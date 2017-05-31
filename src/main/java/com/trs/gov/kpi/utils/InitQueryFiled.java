//package com.trs.gov.kpi.utils;
//
//
//import com.trs.gov.kpi.constant.*;
//import com.trs.gov.kpi.service.*;
//
//import java.lang.reflect.Type;
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * 将searchText参数由String转换成匹配的Integer的集合
// */
//public class InitQueryFiled {
//
//    public static List<Integer> init(String queryFiled, OperationService operationService) {
//        List<Integer> list = new ArrayList<>();
//        if (operationService instanceof LinkAvailabilityService) {
//            list.addAll(Types.LinkAvailableIssueType.findByName(queryFiled));
//        } else if (operationService instanceof InfoUpdateService) {
//            list.addAll(Types.InfoUpdateIssueType.findByName(queryFiled));
//        } else if (operationService instanceof InfoErrorService) {
//            list.addAll(Types.InfoErrorIssueType.findByName(queryFiled));
//        } else if (operationService instanceof IssueService) {
//            list.addAll(Types.LinkAvailableIssueType.findByName(queryFiled));
//            list.addAll(Types.InfoUpdateIssueType.findByName(queryFiled));
//            list.addAll(Types.InfoErrorIssueType.findByName(queryFiled));
//        } else if (operationService instanceof IntegratedMonitorWarningService) {//预警的name和对应得value匹配
//            list.addAll(Types.InfoUpdateWarningType.findByName(queryFiled));
//            list.addAll(Types.RespondWarningType.findByName(queryFiled));
//        } else if (operationService instanceof IntegratedMonitorIsResolvedService) {
//            list.addAll(Types.LinkAvailableIssueType.findByName(queryFiled));
//            list.addAll(Types.InfoUpdateIssueType.findByName(queryFiled));
//            list.addAll(Types.InfoErrorIssueType.findByName(queryFiled));
//            list.addAll(Types.InfoUpdateWarningType.findByName(queryFiled));
//            list.addAll(Types.RespondWarningType.findByName(queryFiled));
//        }
//        return list;
//    }
//}
