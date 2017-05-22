package com.trs.gov.kpi.service.impl;

import com.trs.gov.kpi.constant.EnumChnlGroup;
import com.trs.gov.kpi.dao.ChnlGroupMapper;
import com.trs.gov.kpi.entity.ChnlGroup;
import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.gov.kpi.entity.outerapi.Channel;
import com.trs.gov.kpi.entity.requestdata.ChnlGroupChnlRequestDetail;
import com.trs.gov.kpi.entity.requestdata.ChnlGroupChnlsAddRequestDetail;
import com.trs.gov.kpi.entity.responsedata.Chnl;
import com.trs.gov.kpi.entity.responsedata.ChnlGroupChnlResponseDetail;
import com.trs.gov.kpi.entity.responsedata.ChnlGroupChnlsResponseDetail;
import com.trs.gov.kpi.entity.responsedata.ChnlGroupsResponseDetail;
import com.trs.gov.kpi.service.ChnlGroupService;
import com.trs.gov.kpi.service.outer.SiteApiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by he.lang on 2017/5/16.
 */
@Slf4j
@Service
public class ChnlGroupServiceImp implements ChnlGroupService {
    @Resource
    ChnlGroupMapper chnlGroupMapper;

    @Resource
    SiteApiService siteApiService;

    @Override
    public ChnlGroupsResponseDetail[] getChnlGroupsResponseDetailArray() {
        EnumChnlGroup[] chnlGroups = EnumChnlGroup.getChnlGroups();
        ChnlGroupsResponseDetail[] chnlGroupsResponseDetails = new ChnlGroupsResponseDetail[chnlGroups.length];
        for (int i = 0; i < chnlGroups.length; i++) {
            chnlGroupsResponseDetails[i] = new ChnlGroupsResponseDetail();
            chnlGroupsResponseDetails[i].setId(chnlGroups[i].getId());
            chnlGroupsResponseDetails[i].setName(chnlGroups[i].getName());
        }
        return chnlGroupsResponseDetails;
    }

    @Override
    public ChnlGroupChnlResponseDetail getBySiteIdAndGroupIdAndChnlId(int siteId, int groupId, int chnlId) {
        com.trs.gov.kpi.entity.ChnlGroup chnlGroup = chnlGroupMapper.selectBySiteIdAndGroupIdAndChnlId(siteId, groupId, chnlId);
        ChnlGroupChnlResponseDetail chnlGroupChnlResponseDetail = getChnlGroupByChnlGroupChnlReponseDetail(chnlGroup);
        return chnlGroupChnlResponseDetail;
    }

    @Override
    public List<ChnlGroupChnlsResponseDetail> getPageDataBySiteIdAndGroupId(int siteId, int groupId, int pageIndex, int pageSize) {
        int pageCalculate = pageIndex * pageSize;
        List<com.trs.gov.kpi.entity.ChnlGroup> chnlGroupList = chnlGroupMapper.selectPageDataBySiteIdAndGroupId(siteId, groupId, pageCalculate, pageSize);
        List<ChnlGroupChnlsResponseDetail> chnlGroupChnlsResponseDetailList = new ArrayList<>();
        ChnlGroupChnlsResponseDetail chnlGroupChnlsResponseDetail = null;

        Map<Integer, String> chnlNamCache = new HashMap<>();
        for (com.trs.gov.kpi.entity.ChnlGroup chnlGroup : chnlGroupList) {
            chnlGroupChnlsResponseDetail = getChnlGroupChnlsResponseDetailByChnlGroup(chnlGroup, chnlNamCache);
            chnlGroupChnlsResponseDetailList.add(chnlGroupChnlsResponseDetail);
        }
        return chnlGroupChnlsResponseDetailList;
    }

    private Chnl getChnl(com.trs.gov.kpi.entity.ChnlGroup chnlGroup, Map<Integer, String> chnlNamCache) {
        Chnl chnl = new Chnl();
        chnl.setChannelId(chnlGroup.getChnlId());
        String name = chnlNamCache.get(chnlGroup.getChnlId());
        if (name != null) {
            chnl.setChnlName(name);
        } else {
            try {
                Channel outerChannel = siteApiService.getChannelById(chnlGroup.getChnlId(), "");
                chnl.setChnlName(outerChannel.getChnlName());
                chnlNamCache.put(outerChannel.getChannelId(), outerChannel.getChnlName());
            } catch (RemoteException e) {
                log.error("failed to get channl id: " + chnlGroup.getChnlId(), e);
            }
        }
        return chnl;
    }

    private ChnlGroupChnlsResponseDetail getChnlGroupChnlsResponseDetailByChnlGroup(ChnlGroup chnlGroup, Map<Integer, String> chnlNamCache) {
        ChnlGroupChnlsResponseDetail responseChnl = new ChnlGroupChnlsResponseDetail();
        responseChnl.setId(chnlGroup.getId());
        responseChnl.setChnlGroupName(EnumChnlGroup.valueOf(chnlGroup.getGroupId()).getName());
        responseChnl.setChnl(getChnl(chnlGroup, chnlNamCache));
        return responseChnl;

    }

    @Override
    public int getItemCountBySiteIdAndGroupId(int siteId, int groupId) {
        int itemCount = chnlGroupMapper.selectItemCountBySiteIdAndGroupId(siteId, groupId);
        return itemCount;
    }

    @Override
    public int updateBySiteIdAndId(ChnlGroupChnlRequestDetail chnlGroupChnlRequestDetail) {
        com.trs.gov.kpi.entity.ChnlGroup chnlGroup = chnlGroupMapper.selectBySiteIdAndGroupIdAndChnlId(chnlGroupChnlRequestDetail.getSiteId(), chnlGroupChnlRequestDetail.getGroupId(), chnlGroupChnlRequestDetail.getChnlId());
        int num = 0;
        if (chnlGroup == null) {
            chnlGroup = getChnlGroupByChnlGroupChnlRequestDetail(chnlGroupChnlRequestDetail);
            num = chnlGroupMapper.updateBySiteIdAndId(chnlGroup);
        } else {//不为null，证明修改之后的记录与数据库表中记录相同，继续修改将冲突，所以不做操作

        }
        return num;
    }

    private com.trs.gov.kpi.entity.ChnlGroup getChnlGroupByChnlGroupChnlRequestDetail(ChnlGroupChnlRequestDetail chnlGroupChnlRequestDetail) {
        com.trs.gov.kpi.entity.ChnlGroup chnlGroup = new com.trs.gov.kpi.entity.ChnlGroup();
        chnlGroup.setSiteId(chnlGroupChnlRequestDetail.getSiteId());
        chnlGroup.setId(chnlGroupChnlRequestDetail.getId());
        chnlGroup.setGroupId(chnlGroupChnlRequestDetail.getGroupId());
        chnlGroup.setChnlId(chnlGroupChnlRequestDetail.getChnlId());
        return chnlGroup;
    }

    private ChnlGroupChnlResponseDetail getChnlGroupByChnlGroupChnlReponseDetail(com.trs.gov.kpi.entity.ChnlGroup chnlGroup) {
        ChnlGroupChnlResponseDetail chnlGroupChnlResponseDetail = new ChnlGroupChnlResponseDetail();
        chnlGroupChnlResponseDetail.setSiteId(chnlGroup.getSiteId());
        chnlGroupChnlResponseDetail.setId(chnlGroup.getId());
        chnlGroupChnlResponseDetail.setGroupId(chnlGroup.getGroupId());
        chnlGroupChnlResponseDetail.setChnlId(chnlGroup.getChnlId());
        return chnlGroupChnlResponseDetail;
    }

    @Override
    public int deleteBySiteIdAndId(int siteId, int id) {
        int num = chnlGroupMapper.deleteBySiteIdAndId(siteId, id);
        return num;
    }

    @Override
    public int addChnlGroupChnls(ChnlGroupChnlsAddRequestDetail chnlGroupChnlsAddRequestDetail) {
        int siteId = chnlGroupChnlsAddRequestDetail.getSiteId();
        int groupId = chnlGroupChnlsAddRequestDetail.getGroupId();
        Integer[] chnlIds = chnlGroupChnlsAddRequestDetail.getChnlIds();
        int num = 0;
        for (int i = 0; i < chnlIds.length; i++) {
            int chnlId = chnlIds[i];
            com.trs.gov.kpi.entity.ChnlGroup chnlGroup = chnlGroupMapper.selectBySiteIdAndGroupIdAndChnlId(siteId, groupId, chnlId);
            if (chnlGroup != null) {//存在当前记录，无需添加，直接跳过
                continue;
            } else {
                chnlGroup = new com.trs.gov.kpi.entity.ChnlGroup();
                chnlGroup.setId(null);
                chnlGroup.setSiteId(siteId);
                chnlGroup.setGroupId(groupId);
                chnlGroup.setChnlId(chnlId);
                num = chnlGroupMapper.insert(chnlGroup);
            }
        }
        return num;
    }
}
