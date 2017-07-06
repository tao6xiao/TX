package com.trs.gov.kpi.service;

import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.gov.kpi.entity.requestdata.PageDataRequestParam;
import com.trs.gov.kpi.entity.responsedata.*;

import java.util.List;

/**
 * 链接可用性
 * Created by ranwei on 2017/5/11.
 */
public interface LinkAvailabilityService {

    /**
     * 查询每月新增问题数量的历史记录
     *
     * @param param
     * @return
     */
    History getIssueHistoryCount(PageDataRequestParam param);

    /**
     * 查询未解决问题的列表
     *
     * @param param
     * @return
     */
    ApiPageData getIssueList(PageDataRequestParam param) throws RemoteException;

    /**
     * 插入链接不可用的问题
     *
     * @param linkAvailabilityResponse
     */
    void insertLinkAvailability(LinkAvailabilityResponse linkAvailabilityResponse);

    /**
     * 查询网站首页的有效性
     *
     * @param param
     * @return
     */
    boolean isIndexAvailable(PageDataRequestParam param);

    /**
     * 获取网站首页
     *
     * @param param
     * @return
     */
    String getIndexUrl(PageDataRequestParam param);

    /**
     * 获取网站首页的url及有效性
     *
     * @param param
     * @return
     */
    IndexPage showIndexAvailability(PageDataRequestParam param);

    /**
     * 获取服务链接问题的列表
     *
     * @param param
     * @return
     */
    ApiPageData getServiceLinkList(PageDataRequestParam param) throws RemoteException;

    /**
     * 查询已解决的问题数量
     *
     * @param param
     * @return
     */
    int getHandledIssueCount(PageDataRequestParam param);

    /**
     * 查询未解决的问题数量
     *
     * @param param
     * @return
     */
    int getUnhandledIssueCount(PageDataRequestParam param);

    /**
     * 查询已解决和未解决的问题数量
     *
     * @param param
     * @return
     */
    List<Statistics> getIssueCount(PageDataRequestParam param);

}
