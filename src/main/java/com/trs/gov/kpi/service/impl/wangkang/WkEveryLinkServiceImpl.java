package com.trs.gov.kpi.service.impl.wangkang;

import com.trs.gov.kpi.constant.Constants;
import com.trs.gov.kpi.dao.CommonMapper;
import com.trs.gov.kpi.dao.WkEveryLinkMapper;
import com.trs.gov.kpi.entity.dao.QueryFilter;
import com.trs.gov.kpi.entity.dao.Table;
import com.trs.gov.kpi.entity.wangkang.WkEveryLink;
import com.trs.gov.kpi.service.wangkang.WkEveryLinkService;
import com.trs.gov.kpi.utils.DBUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by li.hao on 2017/7/13.
 */
@Service
public class WkEveryLinkServiceImpl implements WkEveryLinkService {

    @Resource
    CommonMapper commonMapper;

    @Resource
    WkEveryLinkMapper wkEveryLinkMapper;

    @Override
    public void insertWkEveryLinkAccessSpeed(WkEveryLink wkEveryLink) {

        commonMapper.insert(DBUtil.toRow(wkEveryLink));
    }

    @Override
    public Integer selectOnceCheckAvgSpeed(Integer siteId, Integer checkId) {

        return wkEveryLinkMapper.selectOnceCheckAvgSpeed(siteId, checkId);
    }

    @Override
    public Integer selectOnceCheckUpdateContent(Integer siteId, Integer checkId) {

        return wkEveryLinkMapper.selectOnceCheckUpdateContent(siteId, checkId);

    }

    @Override
    public List<WkEveryLink> getList(Integer siteId, Integer checkId) {

        return wkEveryLinkMapper.getList(siteId, checkId);
    }

    @Override
    public Integer count(Integer siteId, Integer checkId) {
        QueryFilter filter = new QueryFilter(Table.WK_EVERY_LINK);
        filter.addCond(Constants.DB_FIELD_CHECK_ID, checkId);
        filter.addCond(Constants.DB_FIELD_SITE_ID, siteId);
        return commonMapper.count(filter);
    }
}
