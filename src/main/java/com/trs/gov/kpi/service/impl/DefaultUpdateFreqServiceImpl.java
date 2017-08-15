package com.trs.gov.kpi.service.impl;

import com.trs.gov.kpi.dao.DefaultUpdateFreqMapper;
import com.trs.gov.kpi.entity.DefaultUpdateFreq;
import com.trs.gov.kpi.service.DefaultUpdateFreqService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * Created by he.lang on 2017/5/15.
 */
@Service
public class DefaultUpdateFreqServiceImpl implements DefaultUpdateFreqService {
    @Resource
    DefaultUpdateFreqMapper defaultUpdateFreqMapper;

    @Override
    public DefaultUpdateFreq getDefaultUpdateFreqBySiteId(int siteId) {
        return defaultUpdateFreqMapper.selectByPrimaryKey(siteId);
    }

    @Override
    public int addDefaultUpdateFreq(DefaultUpdateFreq defaultUpdateFreq) {
        defaultUpdateFreq.setSetTime(new Date());
        return defaultUpdateFreqMapper.insert(defaultUpdateFreq);
    }

    @Override
    public int updateDefaultUpdateFreq(DefaultUpdateFreq defaultUpdateFreq) {
        return defaultUpdateFreqMapper.updateByPrimaryKey(defaultUpdateFreq);
    }


}
