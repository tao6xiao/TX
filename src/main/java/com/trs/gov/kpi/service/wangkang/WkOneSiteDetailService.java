package com.trs.gov.kpi.service.wangkang;

import com.trs.gov.kpi.entity.exception.BizException;
import com.trs.gov.kpi.entity.requestdata.PageDataRequestParam;
import com.trs.gov.kpi.entity.responsedata.*;

import java.util.List;

/**
 * Created by li.hao on 2017/7/12.
 */
public interface WkOneSiteDetailService {


    /**
     * 根据网站编号查询网站链接总数和类型
     *
     * @param siteId
     * @return
     */
    WkLinkTypeResponse getOneSiteLinkTypeBySiteId(Integer siteId, Integer CheckId);

    /**
     * 根据网站编号查询网站总分数 （获取最近一次检查记录）
     *
     * @param siteId
     * @return
     */
    WkOneSiteScoreResponse getOneSiteScoreBySiteId(Integer siteId, Integer checkId);

//    /**
//     * 根据网站编号查询历史评分记录
//     *
//     * @param siteId
//     * @return
//     */
//    List<WkOneSiteScoreResponse> getOneSiteScoreListBySiteId(Integer siteId);


    /*-----------------------------------------------------------------------*/


    /**
     * 链接可用性---根据网站编号查询已解决和未解决问题总数
     *
     * @param siteId
     * @return
     */
    WkStatsCountResponse getInvalidlinkStatsBySiteId(Integer siteId,Integer checkId);

    /**
     *  链接可用性---根据网站编号已解决和未解决问题总数历史记录
     *
     * @param siteId
     * @return
     */
    List<WkStatsCountResponse> getInvalidlinkHistoryStatsBySiteId(Integer siteId, Integer checkId);

    /**
     * 根据网站编号查询首页链接的可用性
     *
     * @param siteId
     * @return
     */
    WkLinkIndexPageStatus getSiteIndexpageStatusBySiteId(Integer siteId, Integer checkId);

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
    WkStatsCountResponse getContentErorStatsBySiteId(Integer siteId, Integer checkId);

    /**
     *  内容检测---根据网站编号已解决和未解决问题总数历史记录
     *
     * @param siteId
     * @return
     */
    List<WkStatsCountResponse> getContentErorHistoryStatsBySiteId(Integer siteId, Integer checkId);

    /**
     * 内容检测---查询未处理的链接的可用性
     *
     * @param param
     * @return
     */
    ApiPageData getContentErrorUnhandledList(PageDataRequestParam param);


    /**
     * 生成报告
     * @param siteId
     * @return
     */
    String generateReport(Integer siteId, Integer checkId) throws BizException;

}
