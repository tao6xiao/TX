package com.trs.gov.kpi.service;

import com.trs.gov.kpi.entity.Issue;
import com.trs.gov.kpi.entity.responsedata.IssueWarningResponseDetail;

import java.util.List;

/**
 * 综合实时监测预警提醒service
 * Created by he.lang on 2017/5/18.
 */
public interface IntegratedMonitorWarningService extends OperationService{

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

    /**
     * 获取分页数据
     * @param pageIndex
     * @param pageSize
     * @param issue
     * @return
     */
    List<IssueWarningResponseDetail> getPageDataWaringList(Integer pageIndex, Integer pageSize, Issue issue);

    /**
     * 获取数据总条数
     * @param issue
     * @return
     */
    int getItemCount(Issue issue);
}
