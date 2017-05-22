package com.trs.gov.kpi.service.impl;

import com.trs.gov.kpi.dao.FrequencyPresetMapper;
import com.trs.gov.kpi.dao.FrequencySetupMapper;
import com.trs.gov.kpi.entity.FrequencyPreset;
import com.trs.gov.kpi.entity.FrequencySetup;
import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.gov.kpi.entity.requestdata.FrequencySetupSetRequestDetail;
import com.trs.gov.kpi.entity.requestdata.FrequencySetupUpdateRequestDetail;
import com.trs.gov.kpi.entity.responsedata.FrequencySetupResponseDetail;
import com.trs.gov.kpi.service.FrequencySetupService;
import com.trs.gov.kpi.service.outer.SiteApiService;
import com.trs.gov.kpi.utils.InitTime;
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
    public List<FrequencySetupResponseDetail> getPageDataFrequencySetupList(int siteId, int pageIndex, int pageSize) throws RemoteException {
        int pageCalculate = pageIndex * pageSize;
        List<FrequencySetup> frequencySetupList = frequencySetupMapper.selectPageDataFrequencySetupList(siteId, pageCalculate, pageSize);
        List<FrequencySetupResponseDetail> frequencySetupResponseDetails = getFrequencySetupDetailListByFrequencySetupList(frequencySetupList);
        return frequencySetupResponseDetails;
    }

    @Override
    public int getCountFrequencySetupBySite(int SiteId) {
        int itemCount = frequencySetupMapper.selectCountFrequencySetupBySiteId(SiteId);
        return itemCount;
    }

    @Override
    public FrequencySetup getFrequencySetupBySiteIdAndChnlId(int siteId, int chnlId) {
        FrequencySetup frequencySetup = frequencySetupMapper.selectFrequencySetupBySiteIdAndChnlId(siteId, chnlId);
        return frequencySetup;
    }

    @Override
    public int updateFrequencySetupById(FrequencySetup frequencySetup) {
        int num = frequencySetupMapper.updateByPrimaryKey(frequencySetup);
        return num;
    }

    @Override
    public int updateFrequencySetupById(FrequencySetupUpdateRequestDetail frequencySetupUpdateRequestDetail) {
        FrequencySetup frequencySetup = frequencySetupUpdateRequestDetail;
        int num = frequencySetupMapper.updateBySiteIdAndidAndChnlId(frequencySetup);
        return num;
    }

    @Override
    public int insert(FrequencySetup frequencySetup) throws ParseException {
        String nowTime = InitTime.getNowTimeFormat(new Date());
        frequencySetup.setSetTime(InitTime.getNowTimeFormat(nowTime));
        int num = frequencySetupMapper.insert(frequencySetup);
        return num;
    }

    @Override
    public FrequencySetup getFrequencySetupByFrequencySetupSetRequestDetail(FrequencySetupSetRequestDetail frequencySetupSetRequestDetail, int chnlId) {
        FrequencySetup frequencySetup = new FrequencySetup();
        frequencySetup.setId(null);
        frequencySetup.setSiteId(frequencySetupSetRequestDetail.getSiteId());
        frequencySetup.setPresetFeqId(frequencySetupSetRequestDetail.getPresetFeqId());
        frequencySetup.setChnlId(chnlId);
        return frequencySetup;
    }

    @Override
    public int deleteFrequencySetupBySiteIdAndId(int siteId, int id) {
        int num = frequencySetupMapper.deleteFrequencySetupBySiteIdAndId(siteId, id);
        return num;
    }

    private List<FrequencySetupResponseDetail> getFrequencySetupDetailListByFrequencySetupList(List<FrequencySetup> frequencySetupList) throws RemoteException {
        List<FrequencySetupResponseDetail> frequencySetupResponseDetails = new ArrayList<>();
        FrequencySetupResponseDetail frequencySetupResponseDetail = null;
        for (FrequencySetup frequencySetup : frequencySetupList) {
            frequencySetupResponseDetail = new FrequencySetupResponseDetail();
            frequencySetupResponseDetail.setId(frequencySetup.getId());
            frequencySetupResponseDetail.setPresetFeqId(frequencySetup.getPresetFeqId());
            FrequencyPreset frequencyPreset = frequencyPresetMapper.selectByPrimaryKey(frequencySetup.getPresetFeqId());
            if (frequencyPreset != null) {
                frequencySetupResponseDetail.setUpdateFreq(frequencyPreset.getUpdateFreq());
                frequencySetupResponseDetail.setAlertFreq(frequencyPreset.getAlertFreq());
            }
            frequencySetupResponseDetail.setChnlId(frequencySetup.getChnlId());
            //TODO add userName to validate
            String chnlName = siteApiService.getChannelById(frequencySetup.getChnlId(),null).getChnlName();
            frequencySetupResponseDetail.setChnlName(chnlName);
            frequencySetupResponseDetails.add(frequencySetupResponseDetail);
        }
        return frequencySetupResponseDetails;
    }
}
