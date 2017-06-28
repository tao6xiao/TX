package com.trs.gov.kpi.service;

import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.gov.kpi.entity.requestdata.IssueCountByTypeRequest;
import com.trs.gov.kpi.entity.requestdata.IssueCountRequest;
import com.trs.gov.kpi.entity.responsedata.*;

import java.util.List;

/**
 * 统计分析->问题统计service
 * Created by he.lang on 2017/6/7.
 */
public interface IssueCountService {

    /**
     * 分类统计问题总数
     * @param request
     * @return
     */
    List<Statistics> countSort(IssueCountRequest request);

    /**
     * 分类统计历史纪录
     * @param request
     * @return
     */
    History historyCountSort(IssueCountRequest request);

    /**
     * 部门分类查询统计数量
     * @param request
     * @return
     */
    List<DeptCountResponse> deptCountSort(IssueCountRequest request) throws RemoteException;

    /**
     * 获取某一个类型下，各个部门待解决问题数量
     * @param request
     * @return
     */
    List<DeptCount> getDeptCountByType(IssueCountByTypeRequest request) throws RemoteException;
	
	/**
     * 部门分类归纳查询统计数量
     * @param request
     * @return
     */
    DeptInductionResponse[] deptInductionSort(IssueCountRequest request) throws RemoteException;
}
