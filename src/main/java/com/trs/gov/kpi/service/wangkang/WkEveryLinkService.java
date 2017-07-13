package com.trs.gov.kpi.service.wangkang;

import com.trs.gov.kpi.entity.wangkang.WkEveryLink;

/**
 * Created by li.hao on 2017/7/13.
 */
public interface WkEveryLinkService {

    /**
     * 插入每个链接的访问速度
     *
     * @param wkEveryLink
     */
    void insertWkEveryLinkAccessSpeed(WkEveryLink wkEveryLink);

    /**
     * 查询一次检测的平均速度
     *
     * @param siteId
     * @param checkId
     * @return
     */
    Integer selectOnceCheckAvgSpeed(Integer siteId, Integer checkId);

    /**
     * 查询一次检测的网站更新数
     *
     * @param siteId
     * @param checkId
     * @return
     */
    Integer selectOnceCheckUpdateContent(Integer siteId, Integer checkId);

}
