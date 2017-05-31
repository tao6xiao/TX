package com.trs.gov.kpi.service;

import com.trs.gov.kpi.entity.IssueBase;
import com.trs.gov.kpi.entity.requestdata.PageDataRequestParam;
import com.trs.gov.kpi.entity.responsedata.ApiPageData;
import com.trs.gov.kpi.entity.responsedata.IssueWarningResponse;

import java.text.ParseException;
import java.util.List;

/**
 * 综合实时监测预警提醒service
 * Created by he.lang on 2017/5/18.
 */
public interface IntegratedMonitorWarningService{

    /**
     * 处理预警提醒
     *
     * @param siteId
     * @param ids
     * @return
     */
    int dealWithWarningBySiteIdAndId(int siteId, Integer[] ids);

    /**
     * 忽略预警提醒
     *
     * @param siteId
     * @param ids
     * @return
     */
    int ignoreWarningBySiteIdAndId(int siteId, Integer[] ids);

    /**
     * 删除预警提醒
     *
     * @param siteId
     * @param ids
     * @return
     */
    int deleteWarningBySiteIdAndId(int siteId, Integer[] ids);

//    /**
//     * 获取分页数据
//     * @param pageIndex
//     * @param pageSize
//     * @param issue
//     * @return
//     */
//    List<IssueWarningResponse> getPageDataWaringList(Integer pageIndex, Integer pageSize, IssueBase issue) throws ParseException;

//    /**
//     * 获取数据总条数
//     * @param issue
//     * @return
//     */
//    int getItemCount(IssueBase issue);

    /**
     * 获取预警提醒的分页数据
     * @param param
     * @return
     */
    ApiPageData get(PageDataRequestParam param) throws ParseException;
}
