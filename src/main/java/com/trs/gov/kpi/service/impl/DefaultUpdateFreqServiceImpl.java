package com.trs.gov.kpi.service.impl;

import com.trs.gov.kpi.dao.DefaultUpdateFreqMapper;
import com.trs.gov.kpi.entity.DefaultUpdateFreq;
import com.trs.gov.kpi.service.DefaultUpdateFreqService;
import com.trs.gov.kpi.utils.InitTime;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.ParseException;
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
        DefaultUpdateFreq defaultUpdateFreq = defaultUpdateFreqMapper.selectByPrimaryKey(siteId);
        return defaultUpdateFreq;
    }

    @Override
    public int addDefaultUpdateFreq(DefaultUpdateFreq defaultUpdateFreq) throws ParseException {
        String nowTime = InitTime.getNowTimeFormat(new Date());
        defaultUpdateFreq.setSetTime(InitTime.getNowTimeFormat(nowTime));
        int num = defaultUpdateFreqMapper.insert(defaultUpdateFreq);
        return num;
    }

    @Override
    public int updateDefaultUpdateFreq(DefaultUpdateFreq defaultUpdateFreq) {
        int num = defaultUpdateFreqMapper.updateByPrimaryKey(defaultUpdateFreq);
        return num;
    }


}
