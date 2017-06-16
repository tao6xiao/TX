package com.trs.gov.kpi.service;

import com.trs.gov.kpi.entity.requestdata.IssueCountRequest;
import com.trs.gov.kpi.entity.responsedata.DeptCountResponse;
import com.trs.gov.kpi.entity.responsedata.History;
import com.trs.gov.kpi.entity.responsedata.Statistics;

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
    List<DeptCountResponse> deptCountSort(IssueCountRequest request);
}
