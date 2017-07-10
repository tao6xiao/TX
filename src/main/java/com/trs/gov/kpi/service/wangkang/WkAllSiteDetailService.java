package com.trs.gov.kpi.service.wangkang;

import com.trs.gov.kpi.entity.requestdata.WkAllSiteDetailRequest;
import com.trs.gov.kpi.entity.responsedata.ApiPageData;
import com.trs.gov.kpi.entity.responsedata.WkAllSiteScoreResponsed;

import java.util.List;

/**
 * 网康---所有网站检测
 * Created by li.hao on 2017/7/10.
 */
public interface WkAllSiteDetailService {

    /**
     * 所有网站分数查询
     *
     * @return
     */
    List<WkAllSiteScoreResponsed> queryAllSiteScore();

    /**
     * 查询所有网站首页可用性的详细信息
     *
     * @param wkAllSiteDetail
     * @return
     */
    ApiPageData queryAllWkSiteAvailable(WkAllSiteDetailRequest wkAllSiteDetail);
}
