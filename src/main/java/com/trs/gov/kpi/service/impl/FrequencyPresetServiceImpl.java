package com.trs.gov.kpi.service.impl;

import com.trs.gov.kpi.dao.FrequencyPresetMapper;
import com.trs.gov.kpi.entity.FrequencyPreset;
import com.trs.gov.kpi.entity.responsedata.FrequencyPresetResponseDeal;
import com.trs.gov.kpi.service.FrequencyPresetService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by he.lang on 2017/5/15.
 */
@Service
public class FrequencyPresetServiceImpl implements FrequencyPresetService {
    @Resource
    FrequencyPresetMapper frequencyPresetMapper;

    @Override
    public int getItemCountBySiteId(int siteId) {
        int itemCount = frequencyPresetMapper.selectItemCountBySiteId(siteId);
        return itemCount;
    }

    @Override
    public List<FrequencyPresetResponseDeal> getPageDataBySiteId(int siteId, int pageIndex, int pageSize) {
        int pageCalculate = pageIndex * pageSize;
        List<FrequencyPreset> frequencyPresetList = frequencyPresetMapper.selectPageDataBySiteId(siteId, pageCalculate, pageSize);
        List<FrequencyPresetResponseDeal> frequencyPresetResponseDealList = new ArrayList<>();
        for (FrequencyPreset frequencyPreset : frequencyPresetList) {
            FrequencyPresetResponseDeal frequencyPresetResponseDeal = getFrequencyPresetDealByfrequencyPreset(frequencyPreset);
            frequencyPresetResponseDealList.add(frequencyPresetResponseDeal);
        }
        return frequencyPresetResponseDealList;
    }

    private FrequencyPresetResponseDeal getFrequencyPresetDealByfrequencyPreset(FrequencyPreset frequencyPreset) {
        FrequencyPresetResponseDeal frequencyPresetResponseDeal = new FrequencyPresetResponseDeal();
        frequencyPresetResponseDeal.setId(frequencyPreset.getId());
        frequencyPresetResponseDeal.setUpdateFreq(frequencyPreset.getUpdateFreq());
        frequencyPresetResponseDeal.setAlertFreq(frequencyPreset.getAlertFreq());
        return frequencyPresetResponseDeal;
    }
}
