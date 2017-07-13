package com.trs.gov.kpi.service.impl.wangkang;

import com.trs.gov.kpi.dao.CommonMapper;
import com.trs.gov.kpi.entity.wangkang.WkEveryLink;
import com.trs.gov.kpi.service.wangkang.WkEveryLinkService;
import com.trs.gov.kpi.utils.DBUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by li.hao on 2017/7/13.
 */
@Service
public class WkEveryLinkServiceImpl implements WkEveryLinkService {

    @Resource
    CommonMapper commonMapper;

    @Override
    public void insertWkEveryLinkAccessSpeed(WkEveryLink wkEveryLink) {

        commonMapper.insert(DBUtil.toRow(wkEveryLink));
    }
}
