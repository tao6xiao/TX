package com.trs.gov.kpi.service.impl.wangkang;

import com.trs.gov.kpi.constant.Types;
import com.trs.gov.kpi.dao.WkSiteDetailMapper;
import com.trs.gov.kpi.dao.WkStatsCountMapper;
import com.trs.gov.kpi.entity.dao.QueryFilter;
import com.trs.gov.kpi.entity.requestdata.PageDataRequestParam;
import com.trs.gov.kpi.entity.responsedata.WkOneSiteScoreResponse;
import com.trs.gov.kpi.entity.responsedata.WkStatsCountResponse;
import com.trs.gov.kpi.entity.wangkang.WkSocre;
import com.trs.gov.kpi.entity.wangkang.WkStatsCount;
import com.trs.gov.kpi.service.helper.QueryFilterHelper;
import com.trs.gov.kpi.service.wangkang.WkOneSiteDetailService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by li.hao on 2017/7/12.
 */
@Service
class WkOneSiteDetailServiceImpl implements WkOneSiteDetailService {

    @Resource
    WkSiteDetailMapper wkSiteDetailMapper;

    @Resource
    WkStatsCountMapper wkStatsCountMapper;

    @Override
    public WkOneSiteScoreResponse getOneSiteScoreBySiteId(Integer siteId) {
        WkSocre wkScore = wkSiteDetailMapper.getOneSiteScoreBySiteId(siteId);
        WkOneSiteScoreResponse wkOneSiteScore = null;
        if(wkScore != null){
            wkOneSiteScore.setTotal(wkScore.getTotal());
            wkOneSiteScore.setContentError(wkScore.getContentError());
            wkOneSiteScore.setInvalidLink(wkScore.getInvalidLink());
            wkOneSiteScore.setOverSpeed(wkScore.getOverSpeed());
            wkOneSiteScore.setUpdateContent(wkScore.getUpdateContent());
        }
        return wkOneSiteScore;
    }

    @Override
    public List<WkOneSiteScoreResponse> getOneSiteScoreListBySiteId(Integer siteId) {
        List<WkSocre> wkSocreList = wkSiteDetailMapper.getOneSiteScoreListBySiteId(siteId);
        List<WkOneSiteScoreResponse> wkOneSiteScoreList = new ArrayList<>();

        if(wkSocreList.size() != 0){
            for (WkSocre wkScore:wkSocreList) {
            WkOneSiteScoreResponse wkOneSiteScore = new WkOneSiteScoreResponse();
                wkOneSiteScore.setCheckTime(wkScore.getCheckTime());
                wkOneSiteScore.setTotal(wkScore.getTotal());
                wkOneSiteScore.setContentError(wkScore.getContentError());
                wkOneSiteScore.setInvalidLink(wkScore.getInvalidLink());
                wkOneSiteScore.setOverSpeed(wkScore.getOverSpeed());
                wkOneSiteScore.setUpdateContent(wkScore.getUpdateContent());

                wkOneSiteScoreList.add(wkOneSiteScore);
            }
        }
        return wkOneSiteScoreList;
    }

    /*---链接可用性---*/
    @Override
    public WkStatsCountResponse getInvalidlinkStatsBySiteId(Integer siteId) {

        PageDataRequestParam param = new PageDataRequestParam();
        param.setSiteId(siteId);
        QueryFilter queryFilter = QueryFilterHelper.toFilter(param);
        queryFilter.addCond("typeId", Types.WkSiteCheckType.INVALID_LINK.value);

        WkStatsCount wkStatsCount = wkStatsCountMapper.getInvalidlinkStatsBySiteId(queryFilter);
        WkStatsCountResponse wkStatsCountResponse = null;
        if(wkStatsCount != null){
            wkStatsCountResponse.setUnhandleIssue(wkStatsCount.getUnResolved());
            wkStatsCountResponse.setHandleIssue(wkStatsCount.getIsResolved());
        }
        return wkStatsCountResponse;
    }

    @Override
    public List<WkStatsCountResponse> getInvalidlinkHistoryStatsBySiteId(Integer siteId) {
        PageDataRequestParam param = new PageDataRequestParam();
        param.setSiteId(siteId);
        QueryFilter queryFilter = QueryFilterHelper.toFilter(param);
        queryFilter.addCond("typeId", Types.WkSiteCheckType.INVALID_LINK.value);

        List<WkStatsCount> wkStatsCountList = wkStatsCountMapper.getInvalidlinkHistoryStatsBySiteId(queryFilter);
        List<WkStatsCountResponse> wkStatsCountResponseList = new ArrayList<>();

        if(wkStatsCountList.size() != 0){
            for (WkStatsCount wkStatsCount: wkStatsCountList) {
                WkStatsCountResponse wkStatsCountResponse = new WkStatsCountResponse();
                wkStatsCountResponse.setHandleIssue(wkStatsCount.getIsResolved());
                wkStatsCountResponse.setUnhandleIssue(wkStatsCount.getUnResolved());
                wkStatsCountResponse.setCheckTime(wkStatsCount.getCheckTime());

                wkStatsCountResponseList.add(wkStatsCountResponse);
            }
        }
        return wkStatsCountResponseList;
    }


    /*---内容检测---*/
    @Override
    public WkStatsCountResponse getContentErorStatsBySiteId(Integer siteId) {
        PageDataRequestParam param = new PageDataRequestParam();
        param.setSiteId(siteId);
        QueryFilter queryFilter = QueryFilterHelper.toFilter(param);
        queryFilter.addCond("typeId", Types.WkSiteCheckType.CONTENT_ERROR.value);

        WkStatsCount wkStatsCount = wkStatsCountMapper.getInvalidlinkStatsBySiteId(queryFilter);
        WkStatsCountResponse wkStatsCountResponse = null;
        if(wkStatsCount != null){
            wkStatsCountResponse.setUnhandleIssue(wkStatsCount.getUnResolved());
            wkStatsCountResponse.setHandleIssue(wkStatsCount.getIsResolved());
        }
        return wkStatsCountResponse;
    }

    @Override
    public List<WkStatsCountResponse> getContentErorHistoryStatsBySiteId(Integer siteId) {
        PageDataRequestParam param = new PageDataRequestParam();
        param.setSiteId(siteId);
        QueryFilter queryFilter = QueryFilterHelper.toFilter(param);
        queryFilter.addCond("typeId", Types.WkSiteCheckType.CONTENT_ERROR.value);

        List<WkStatsCount> wkStatsCountList = wkStatsCountMapper.getInvalidlinkHistoryStatsBySiteId(queryFilter);
        List<WkStatsCountResponse> wkStatsCountResponseList = new ArrayList<>();

        if(wkStatsCountList.size() != 0){
            for (WkStatsCount wkStatsCount: wkStatsCountList) {
                WkStatsCountResponse wkStatsCountResponse = new WkStatsCountResponse();
                wkStatsCountResponse.setHandleIssue(wkStatsCount.getIsResolved());
                wkStatsCountResponse.setUnhandleIssue(wkStatsCount.getUnResolved());
                wkStatsCountResponse.setCheckTime(wkStatsCount.getCheckTime());

                wkStatsCountResponseList.add(wkStatsCountResponse);
            }
        }
        return wkStatsCountResponseList;
    }

    /*---访问速度---*/
    /*---网站更新---*/
}
