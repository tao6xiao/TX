package com.trs.gov.kpi.service.impl;

import com.trs.gov.kpi.constant.Status;
import com.trs.gov.kpi.dao.FrequencyPresetMapper;
import com.trs.gov.kpi.dao.FrequencySetupMapper;
import com.trs.gov.kpi.entity.FrequencyPreset;
import com.trs.gov.kpi.entity.FrequencySetup;
import com.trs.gov.kpi.entity.dao.DBPager;
import com.trs.gov.kpi.entity.exception.BizException;
import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.gov.kpi.entity.outerapi.Channel;
import com.trs.gov.kpi.entity.requestdata.FrequencySetupSelectRequest;
import com.trs.gov.kpi.entity.requestdata.FrequencySetupSetRequest;
import com.trs.gov.kpi.entity.requestdata.FrequencySetupUpdateRequest;
import com.trs.gov.kpi.entity.responsedata.FrequencySetupResponse;
import com.trs.gov.kpi.entity.responsedata.Pager;
import com.trs.gov.kpi.service.FrequencySetupService;
import com.trs.gov.kpi.service.outer.SiteApiService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

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
    public List<FrequencySetupResponse> getPageDataFrequencySetupList(FrequencySetupSelectRequest selectRequest, Pager pager) throws RemoteException {
        List<FrequencySetup> frequencySetupList = new ArrayList<>();
        DBPager dbPager = new DBPager((pager.getCurrPage() - 1) * pager.getPageSize(), pager.getPageSize());
        String searchField = selectRequest.getSearchField();
        if ("chnlName".equals(searchField)) {
            frequencySetupList = getListForChnlName(selectRequest, dbPager, frequencySetupList);
        } else if ("updateFreq".equals(searchField)) {
            frequencySetupList = getListForUpdateFreq(selectRequest, dbPager, frequencySetupList);
        } else if ("chnlId".equals(searchField)) {
            frequencySetupList = frequencySetupMapper.selectPageDataList(selectRequest, dbPager, null);
        } else {
            frequencySetupList = getListForChnlName(selectRequest, dbPager, frequencySetupList);
            if (!"".equals(selectRequest.getSearchText())) {
                frequencySetupList = getListForUpdateFreq(selectRequest, dbPager, frequencySetupList);
            }

            if (frequencySetupList.isEmpty()) {
                FrequencySetup frequencySetup = new FrequencySetup();
                frequencySetupList = frequencySetupMapper.selectPageDataList(selectRequest, dbPager, frequencySetup);

            }
        }
        return getFrequencySetupDetailListByFrequencySetupList(frequencySetupList);
    }

    private List<FrequencySetup> getListForUpdateFreq(FrequencySetupSelectRequest selectRequest, DBPager dbPager, List<FrequencySetup> frequencySetupList) {
        List<FrequencyPreset> presetList = frequencyPresetMapper.selectBySiteIdAndUpdateFreq(selectRequest.getSiteId(), selectRequest.getSearchText());
        for (FrequencyPreset preset : presetList) {
            FrequencySetup frequencySetup = new FrequencySetup();
            frequencySetup.setPresetFeqId(preset.getId());
            frequencySetupList = getList(selectRequest, dbPager, frequencySetup, frequencySetupList);
        }
        return frequencySetupList;
    }

    private List<FrequencySetup> getListForChnlName(FrequencySetupSelectRequest selectRequest, DBPager dbPager, List<FrequencySetup> frequencySetupList) {
        // TODO: 2017/6/6 get chnlId By chnlName
        Integer[] chnlIds = new Integer[0];
        for (int i = 0; i < chnlIds.length; i++) {
            FrequencySetup frequencySetup = new FrequencySetup();
            frequencySetup.setChnlId(chnlIds[i]);
            frequencySetupList = getList(selectRequest, dbPager, frequencySetup, frequencySetupList);
        }
        return frequencySetupList;
    }

    private List<FrequencySetup> getList(FrequencySetupSelectRequest selectRequest, DBPager dbPager, FrequencySetup frequencySetup, List<FrequencySetup> frequencySetupList) {
        List<FrequencySetup> setupList = frequencySetupMapper.selectPageDataList(selectRequest, dbPager, frequencySetup);
        for (FrequencySetup setup : setupList) {
            frequencySetupList.add(setup);
        }
        return frequencySetupList;
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
        frequencySetup.setIsOpen((byte) Status.Open.OPEN.value);
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

    @Override
    public void closeOrOpen(int siteId, Integer[] ids, int isOpen) {
        for (int i = 0; i < ids.length; i++) {
            frequencySetupMapper.closeOrOpen(siteId, ids[i], (byte) isOpen);
        }
    }

    @Override
    public boolean isIdExist(int siteId, int id) {
        if(frequencySetupMapper.selectBySiteIdAndId(siteId, id) != null){
            return true;
        }
        return false;
    }


    private List<FrequencySetupResponse> getFrequencySetupDetailListByFrequencySetupList(List<FrequencySetup> frequencySetupList) throws RemoteException {
        List<FrequencySetupResponse> frequencySetupResponses = new ArrayList<>();
        FrequencySetupResponse frequencySetupResponse = null;
        for (FrequencySetup frequencySetup : frequencySetupList) {
            frequencySetupResponse = new FrequencySetupResponse();
            frequencySetupResponse.setId(frequencySetup.getId());
            frequencySetupResponse.setPresetFeqId(frequencySetup.getPresetFeqId());
            Integer isOpen = Integer.valueOf(frequencySetup.getIsOpen());
            frequencySetupResponse.setIsOpen(isOpen);
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
                if (childChnl != null && childChnl.getChnlName() != null) {
                    frequencySetupResponse.setChnlName(childChnl.getChnlName());
                    frequencySetupResponses.add(frequencySetupResponse);
                }
            }
        }
        return frequencySetupResponses;
    }
}
