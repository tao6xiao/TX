package com.trs.gov.kpi.service.wangkang;

import com.trs.gov.kpi.entity.requestdata.PageDataRequestParam;
import com.trs.gov.kpi.entity.responsedata.ApiPageData;
import com.trs.gov.kpi.entity.responsedata.WkOneSiteScoreResponse;
import com.trs.gov.kpi.entity.responsedata.WkStatsCountResponse;

import java.util.List;

/**
 * Created by li.hao on 2017/7/12.
 */
public interface WkOneSiteDetailService {

    /**
     * 根据网站编号查询网站总分数 （获取最近一次检查记录）
     *
     * @param siteId
     * @return
     */
    WkOneSiteScoreResponse getOneSiteScoreBySiteId(Integer siteId);

    /**
     * 根据网站编号查询历史评分记录
     *
     * @param siteId
     * @return
     */
    List<WkOneSiteScoreResponse> getOneSiteScoreListBySiteId(Integer siteId);


    /*-----------------------------------------------------------------------*/


    /**
     * 链接可用性---根据网站编号查询已解决和未解决问题总数
     *
     * @param siteId
     * @return
     */
    WkStatsCountResponse getInvalidlinkStatsBySiteId(Integer siteId);

    /**
     *  链接可用性---根据网站编号已解决和未解决问题总数历史记录
     *
     * @param siteId
     * @return
     */
    List<WkStatsCountResponse> getInvalidlinkHistoryStatsBySiteId(Integer siteId);

    /**
     * 链接可用性---查询未处理的链接的可用性
     *
     * @param param
     * @return
     */
    ApiPageData getInvalidLinkUnhandledList(PageDataRequestParam param);


    /*----------------------------------------------------------------------------*/


    /**
     * 内容检测---根据网站编号查询已解决和未解决问题总数
     *
     * @param siteId
     * @return
     */
    WkStatsCountResponse getContentErorStatsBySiteId(Integer siteId);

    /**
     *  内容检测---根据网站编号已解决和未解决问题总数历史记录
     *
     * @param siteId
     * @return
     */
    List<WkStatsCountResponse> getContentErorHistoryStatsBySiteId(Integer siteId);

    /**
     * 内容检测---查询未处理的链接的可用性
     *
     * @param param
     * @return
     */
    ApiPageData getContentErrorUnhandledList(PageDataRequestParam param);
}
