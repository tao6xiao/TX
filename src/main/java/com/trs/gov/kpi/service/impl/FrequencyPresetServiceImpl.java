package com.trs.gov.kpi.service.impl;

import com.trs.gov.kpi.dao.FrequencyPresetMapper;
import com.trs.gov.kpi.entity.FrequencyPreset;
import com.trs.gov.kpi.entity.exception.BizException;
import com.trs.gov.kpi.entity.requestdata.FrequencyPresetRequest;
import com.trs.gov.kpi.entity.responsedata.FrequencyPresetResponse;
import com.trs.gov.kpi.service.FrequencyPresetService;
import lombok.extern.slf4j.Slf4j;
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
        return frequencyPresetMapper.selectItemCountBySiteId(siteId);
    }

    @Override
    public List<FrequencyPresetResponse> getPageDataBySiteId(int siteId, int pageIndex, int pageSize) {
        int pageCalculate = pageIndex * pageSize;
        List<FrequencyPreset> frequencyPresetList = frequencyPresetMapper.selectPageDataBySiteId(siteId, pageCalculate, pageSize);
        List<FrequencyPresetResponse> frequencyPresetResponseList = new ArrayList<>();
        for (FrequencyPreset frequencyPreset : frequencyPresetList) {
            FrequencyPresetResponse frequencyPresetResponse = getResponseByPresetRequest(frequencyPreset);
            frequencyPresetResponseList.add(frequencyPresetResponse);
        }
        return frequencyPresetResponseList;
    }

    @Override
    public int addFrequencyPreset(FrequencyPresetRequest presetRequest) throws BizException {
        FrequencyPreset preset = toFrequencyPresetByRequest(presetRequest);
        if (isPresetExist(preset)) {
            return 0;
        }
        return frequencyPresetMapper.insert(preset);
    }

    private boolean isPresetExist(FrequencyPreset preset) {
        return frequencyPresetMapper.selectByPreset(preset) != null;
    }

    @Override
    public int updateBySiteIdAndId(FrequencyPreset preset) {
        if (isPresetExist(preset)) {
            return 0;
        }
        return frequencyPresetMapper.updateBySiteIdAndId(preset);
    }

    @Override
    public int deleteBySiteIdAndId(int siteId, int id) {
        return frequencyPresetMapper.deleteBySiteIdAndId(siteId, id);
    }

    @Override
    public boolean isPresetFeqIdExist(int siteId, int id) {
        FrequencyPreset preset = frequencyPresetMapper.selectBySiteIdAndId(siteId, id);
        return preset != null;
    }

    private FrequencyPreset toFrequencyPresetByRequest(FrequencyPresetRequest frequencyPresetRequest) {
        FrequencyPreset frequencyPreset = new FrequencyPreset();
        frequencyPreset.setId(null);
        frequencyPreset.setSiteId(frequencyPresetRequest.getSiteId());
        frequencyPreset.setUpdateFreq(frequencyPresetRequest.getUpdateFreq());
        frequencyPreset.setAlertFreq(frequencyPresetRequest.getAlertFreq());
        return frequencyPreset;
    }

    private FrequencyPresetResponse getResponseByPresetRequest(FrequencyPreset frequencyPreset) {
        FrequencyPresetResponse frequencyPresetResponse = new FrequencyPresetResponse();
        frequencyPresetResponse.setId(frequencyPreset.getId());
        frequencyPresetResponse.setUpdateFreq(frequencyPreset.getUpdateFreq());
        frequencyPresetResponse.setAlertFreq(frequencyPreset.getAlertFreq());
        return frequencyPresetResponse;
    }
}
