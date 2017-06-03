package com.trs.gov.kpi.service.impl;

import com.trs.gov.kpi.dao.FrequencyPresetMapper;
import com.trs.gov.kpi.dao.FrequencySetupMapper;
import com.trs.gov.kpi.entity.FrequencyPreset;
import com.trs.gov.kpi.entity.FrequencySetup;
import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.gov.kpi.entity.outerapi.Channel;
import com.trs.gov.kpi.entity.requestdata.FrequencySetupSetRequest;
import com.trs.gov.kpi.entity.requestdata.FrequencySetupUpdateRequest;
import com.trs.gov.kpi.entity.responsedata.FrequencySetupResponse;
import com.trs.gov.kpi.service.FrequencySetupService;
import com.trs.gov.kpi.service.outer.SiteApiService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by he.lang on 2017/5/16.
 */
@Service
public class FrequencySetupServiceImpl implements FrequencySetupService {
    @Resource
    FrequencySetupMapper frequencySetupMapper;

    @Resource
    FrequencyPresetMapper frequencyPresetMapper;

    @Resource
    SiteApiService siteApiService;

    @Override
    public List<FrequencySetupResponse> getPageDataFrequencySetupList(int siteId, int pageIndex, int pageSize) throws RemoteException {
        int pageCalculate = pageIndex * pageSize;
        List<FrequencySetup> frequencySetupList = frequencySetupMapper.selectPageDataFrequencySetupList(siteId, pageCalculate, pageSize);
        return getFrequencySetupDetailListByFrequencySetupList(frequencySetupList);
    }

    @Override
    public int getCountFrequencySetupBySite(int siteId) {
        return frequencySetupMapper.selectCountFrequencySetupBySiteId(siteId);
    }

    @Override
    public FrequencySetup getFrequencySetupBySiteIdAndChnlId(int siteId, int chnlId) {
        return frequencySetupMapper.selectFrequencySetupBySiteIdAndChnlId(siteId, chnlId);
    }

    @Override
    public int updateFrequencySetupById(FrequencySetup frequencySetup) {
        return frequencySetupMapper.updateByPrimaryKey(frequencySetup);
    }

    @Override
    public int updateFrequencySetupById(FrequencySetupUpdateRequest frequencySetupUpdateRequest) {
        FrequencySetup frequencySetup = frequencySetupUpdateRequest;
        return frequencySetupMapper.updateBySiteIdAndIdAndChnlId(frequencySetup);
    }

    @Override
    public int insert(FrequencySetup frequencySetup) throws ParseException {
        frequencySetup.setSetTime(new Date());
        return frequencySetupMapper.insert(frequencySetup);
    }

    @Override
    public FrequencySetup toFrequencySetupBySetupRequest(FrequencySetupSetRequest frequencySetupSetRequest, int chnlId) {
        FrequencySetup frequencySetup = new FrequencySetup();
        frequencySetup.setId(null);
        frequencySetup.setSiteId(frequencySetupSetRequest.getSiteId());
        frequencySetup.setPresetFeqId(frequencySetupSetRequest.getPresetFeqId());
        frequencySetup.setChnlId(chnlId);
        return frequencySetup;
    }

    @Override
    public int deleteFrequencySetupBySiteIdAndId(int siteId, int id) {
        return frequencySetupMapper.deleteFrequencySetupBySiteIdAndId(siteId, id);
    }

    @Override
    public boolean isPresetFeqUsed(int siteId, int presetFeqId) {
        List<FrequencySetup> setupList = frequencySetupMapper.getBySiteIdAndPresetFeqId(siteId, presetFeqId);
        return setupList != null && !setupList.isEmpty();
    }

    private List<FrequencySetupResponse> getFrequencySetupDetailListByFrequencySetupList(List<FrequencySetup> frequencySetupList) throws RemoteException {
        List<FrequencySetupResponse> frequencySetupResponses = new ArrayList<>();
        FrequencySetupResponse frequencySetupResponse = null;
        for (FrequencySetup frequencySetup : frequencySetupList) {
            frequencySetupResponse = new FrequencySetupResponse();
            frequencySetupResponse.setId(frequencySetup.getId());
            frequencySetupResponse.setPresetFeqId(frequencySetup.getPresetFeqId());
            FrequencyPreset frequencyPreset = frequencyPresetMapper.selectByPrimaryKey(frequencySetup.getPresetFeqId());
            if (frequencyPreset != null) {
                frequencySetupResponse.setUpdateFreq(frequencyPreset.getUpdateFreq());
                frequencySetupResponse.setAlertFreq(frequencyPreset.getAlertFreq());
            }
            frequencySetupResponse.setChnlId(frequencySetup.getChnlId());
            //TODO add userName to validate
            Integer chnlId = frequencySetup.getChnlId();
            if (chnlId != null) {
                Channel childChnl = siteApiService.getChannelById(chnlId, null);
                if(childChnl != null && childChnl.getChnlName() != null){
                    frequencySetupResponse.setChnlName(childChnl.getChnlName());
                    frequencySetupResponses.add(frequencySetupResponse);
                }
            }
        }
        return frequencySetupResponses;
    }
}
