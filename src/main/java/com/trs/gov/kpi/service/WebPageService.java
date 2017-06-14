package com.trs.gov.kpi.service;

import com.trs.gov.kpi.entity.PageSpace;
import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.gov.kpi.entity.requestdata.PageDataRequestParam;
import com.trs.gov.kpi.entity.responsedata.ApiPageData;

import java.util.Date;
import java.util.List;

/**
 * Created by ranwei on 2017/6/6.
 */
public interface WebPageService {

    /**
     * 查询响应速度
     *
     * @param param
     * @return
     */
    ApiPageData selectReplySpeed(PageDataRequestParam param) throws RemoteException;

    /**
     * 查询响应速度数量
     *
     * @param param
     * @return
     */
    int selectReplySpeedCount(PageDataRequestParam param) throws RemoteException;

    /**
     * 查询过大页面
     *
     * @param param
     * @return
     */
    ApiPageData selectPageSpace(PageDataRequestParam param) throws RemoteException;

    /**
     * 查询过大页面数量
     *
     * @param param
     * @return
     */
    int selectPageSpaceCount(PageDataRequestParam param) throws RemoteException;

    /**
     * 查询过深页面
     *
     * @param param
     * @return
     */
    ApiPageData selectPageDepth(PageDataRequestParam param) throws RemoteException;

    /**
     * 查询过深页面数量
     *
     * @param param
     * @return
     */
    int selectPageDepthCount(PageDataRequestParam param) throws RemoteException;

    /**
     * 查询冗余代码
     *
     * @param param
     * @return
     */
    ApiPageData selectRepeatCode(PageDataRequestParam param) throws RemoteException;

    /**
     * 查询冗余代码数量
     *
     * @param param
     * @return
     */
    int selectRepeatCodeCount(PageDataRequestParam param) throws RemoteException;

    /**
     * 查询过长URL页面
     *
     * @param param
     * @return
     */
    ApiPageData selectUrlLength(PageDataRequestParam param) throws RemoteException;

    /**
     * 查询过长URL页面数量
     *
     * @param param
     * @return
     */
    int selectUrlLengthCount(PageDataRequestParam param) throws RemoteException;

    /**
     * 查询监测时间最早的网页分析数据
     *
     * @return
     */
    Date getEarliestCheckTime();

    /**
     * 批量处理网页问题
     *
     * @param siteId
     * @param ids
     */
    void handlePageByIds(int siteId, List<Integer> ids);

    /**
     * 批量忽略网页问题
     *
     * @param siteId
     * @param ids
     */
    void ignorePageByIds(int siteId, List<Integer> ids);

    /**
     * 批量删除网页问题
     *
     * @param siteId
     * @param ids
     */
    void delPageByIds(int siteId, List<Integer> ids);

    /**
     * 插入过大页面信息
     *
     * @param pageSpace
     */
    void insertPageSpace(PageSpace pageSpace);
}
