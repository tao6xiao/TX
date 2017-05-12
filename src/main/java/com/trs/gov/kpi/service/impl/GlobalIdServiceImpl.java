package com.trs.gov.kpi.service.impl;

import com.trs.gov.kpi.dao.GlobalIdMapper;
import com.trs.gov.kpi.entity.GlobalId;
import com.trs.gov.kpi.service.GlobalIdService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * Created by linwei on 2017/5/12.
 */

@Service
public class GlobalIdServiceImpl implements GlobalIdService{

    @Resource
    GlobalIdMapper globalIdMapper;

    @Override
    public int genGlobalId() {
        GlobalId id = new GlobalId();
        id.setGenTime(new Date());

        globalIdMapper.insert(id);
        return id.getId();
    }
}
