package com.trs.gov.kpi.service.impl.wangkang;

import com.trs.gov.kpi.dao.CommonMapper;
import com.trs.gov.kpi.dao.WkEveryLinkMapper;
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
    public List<WkEveryLink> getLastTimeWkEveryLinkList(Integer siteId, Integer checkId) {

        return wkEveryLinkMapper.getLastTimeWkEveryLinkList(siteId, checkId);
    }

    @Override
    public List<WkEveryLink> getThisTimeWkEveryLinkList(Integer siteId, Integer checkId) {

        return wkEveryLinkMapper.getThisTimeWkEveryLinkList(siteId, checkId);
    }
}
