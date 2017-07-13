package com.trs.gov.kpi.service.impl.wangkang;

import com.trs.gov.kpi.dao.WkIdMapper;
import com.trs.gov.kpi.entity.wangkang.WkId;
import com.trs.gov.kpi.service.wangkang.WkIdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by linwei on 2017/7/13.
 */
@Service
public class WkIdServiceImpl implements WkIdService {

    @Autowired
    private WkIdMapper wkIdMapper;

    @Override
    public int getNewCheckId() {
        final WkId checkId = WkId.createCheckId();
        wkIdMapper.incId(checkId);
        return checkId.getId();
    }
}
