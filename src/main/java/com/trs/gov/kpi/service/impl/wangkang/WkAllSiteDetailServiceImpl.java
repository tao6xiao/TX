package com.trs.gov.kpi.service.impl.wangkang;

import com.trs.gov.kpi.dao.WkAllSiteDetailMapper;
import com.trs.gov.kpi.entity.dao.QueryFilter;
import com.trs.gov.kpi.entity.requestdata.WkAllSiteDetailRequest;
import com.trs.gov.kpi.entity.responsedata.ApiPageData;
import com.trs.gov.kpi.entity.responsedata.Pager;
import com.trs.gov.kpi.entity.responsedata.WkAllSiteScoreResponsed;
import com.trs.gov.kpi.entity.wangkang.WkSocre;
import com.trs.gov.kpi.service.helper.QueryFilterHelper;
import com.trs.gov.kpi.service.wangkang.WkAllSiteDetailService;
import com.trs.gov.kpi.service.wangkang.WkSiteManagementService;
import com.trs.gov.kpi.utils.PageInfoDeal;
import com.trs.gov.kpi.utils.StringUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by li.hao on 2017/7/10.
 */
@Service
public class WkAllSiteDetailServiceImpl implements WkAllSiteDetailService {

    @Resource
    WkAllSiteDetailMapper wkAllSiteDetailMapper;

    @Resource
    WkSiteManagementService wkSiteManagementService;

    @Override
    public List<WkAllSiteScoreResponsed> queryAllSiteScore() {
        List<WkSocre> wkSocreList = wkAllSiteDetailMapper.selectAllSiteScore();
        List<WkAllSiteScoreResponsed> wkAllSiteScoreList = new ArrayList<>();
        for (WkSocre wkScore :wkSocreList) {
            WkAllSiteScoreResponsed wkAllSiteScore = new WkAllSiteScoreResponsed();

            wkAllSiteScore.setSiteId(wkScore.getSiteId());
            wkAllSiteScore.setSiteName(wkSiteManagementService.getSiteNameBySiteId(wkScore.getSiteId()));
            wkAllSiteScore.setTotal(wkScore.getTotal());

            wkAllSiteScoreList.add(wkAllSiteScore);
        }
        return wkAllSiteScoreList;
    }

    @Override
    public ApiPageData queryAllWkSiteAvailable(WkAllSiteDetailRequest wkAllSiteDetail) {
        if(!StringUtil.isEmpty(wkAllSiteDetail.getSearchText())){
            wkAllSiteDetail.setSearchText(StringUtil.escape(wkAllSiteDetail.getSearchText()));
        }
        QueryFilter filter = QueryFilterHelper.toWkFilter(wkAllSiteDetail);
        int itemCount = wkAllSiteDetailMapper.selectAllSiteScoreCount(filter);
        Pager pager = PageInfoDeal.buildResponsePager(wkAllSiteDetail.getPageIndex(), wkAllSiteDetail.getPageSize(), itemCount);
        filter.setPager(pager);

        return null;
    }
}
