package com.trs.gov.kpi.service.impl;

import com.trs.gov.kpi.constant.EnumChnlGroup;
import com.trs.gov.kpi.dao.ChnlGroupMapper;
import com.trs.gov.kpi.entity.ChannelGroup;
import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.gov.kpi.entity.outerapi.Channel;
import com.trs.gov.kpi.entity.requestdata.ChnlGroupChannelRequestDetail;
import com.trs.gov.kpi.entity.requestdata.ChnlGroupChnlsAddRequestDetail;
import com.trs.gov.kpi.entity.responsedata.Chnl;
import com.trs.gov.kpi.entity.responsedata.ChnlGroupChannelResponseDetail;
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
    public ChnlGroupChannelResponseDetail getBySiteIdAndGroupIdAndChnlId(int siteId, int groupId, int chnlId) {
        ChannelGroup channelGroup = chnlGroupMapper.selectBySiteIdAndGroupIdAndChnlId(siteId, groupId, chnlId);
        ChnlGroupChannelResponseDetail chnlGroupChnlResponseDetail = getChnlGroupByChnlGroupChnlReponseDetail(channelGroup);
        return chnlGroupChnlResponseDetail;
    }

    @Override
    public List<ChnlGroupChnlsResponseDetail> getPageDataBySiteIdAndGroupId(int siteId, int groupId, int pageIndex, int pageSize) {
        int pageCalculate = pageIndex * pageSize;
        List<ChannelGroup> channelGroupList = chnlGroupMapper.selectPageDataBySiteIdAndGroupId(siteId, groupId, pageCalculate, pageSize);
        List<ChnlGroupChnlsResponseDetail> chnlGroupChnlsResponseDetailList = new ArrayList<>();
        ChnlGroupChnlsResponseDetail chnlGroupChnlsResponseDetail = null;

        Map<Integer, String> chnlNamCache = new HashMap<>();
        for (ChannelGroup channelGroup : channelGroupList) {
            chnlGroupChnlsResponseDetail = getChnlGroupChnlsResponseDetailByChnlGroup(channelGroup, chnlNamCache);
            chnlGroupChnlsResponseDetailList.add(chnlGroupChnlsResponseDetail);
        }
        return chnlGroupChnlsResponseDetailList;
    }

    private Chnl getChnl(ChannelGroup channelGroup, Map<Integer, String> chnlNamCache) {
        Chnl chnl = new Chnl();
        chnl.setChannelId(channelGroup.getChnlId());
        String name = chnlNamCache.get(channelGroup.getChnlId());
        if (name != null) {
            chnl.setChnlName(name);
        } else {
            try {
                Channel outerChannel = siteApiService.getChannelById(channelGroup.getChnlId(), "");
                chnl.setChnlName(outerChannel.getChnlName());
                chnlNamCache.put(outerChannel.getChannelId(), outerChannel.getChnlName());
            } catch (RemoteException e) {
                log.error("failed to get channl id: " + channelGroup.getChnlId(), e);
            }
        }
        return chnl;
    }

    private ChnlGroupChnlsResponseDetail getChnlGroupChnlsResponseDetailByChnlGroup(ChannelGroup channelGroup, Map<Integer, String> chnlNamCache) {
        ChnlGroupChnlsResponseDetail responseChnl = new ChnlGroupChnlsResponseDetail();
        responseChnl.setId(channelGroup.getId());
        responseChnl.setChnlGroupName(EnumChnlGroup.valueOf(channelGroup.getGroupId()).getName());
        responseChnl.setChnl(getChnl(channelGroup, chnlNamCache));
        return responseChnl;

    }

    @Override
    public int getItemCountBySiteIdAndGroupId(int siteId, int groupId) {
        return chnlGroupMapper.selectItemCountBySiteIdAndGroupId(siteId, groupId);
    }

    @Override
    public int updateBySiteIdAndId(ChnlGroupChannelRequestDetail chnlGroupChnlRequestDetail) {
        ChannelGroup channelGroup = chnlGroupMapper.selectBySiteIdAndGroupIdAndChnlId(chnlGroupChnlRequestDetail.getSiteId(), chnlGroupChnlRequestDetail.getGroupId(), chnlGroupChnlRequestDetail.getChnlId());
        int num = 0;
        if (channelGroup == null) {
            channelGroup = getChnlGroupByChnlGroupChnlRequestDetail(chnlGroupChnlRequestDetail);
            num = chnlGroupMapper.updateBySiteIdAndId(channelGroup);
        } else {
            //不为null，证明修改之后的记录与数据库表中记录相同，继续修改将冲突，所以不做操作
        }
        return num;
    }

    private ChannelGroup getChnlGroupByChnlGroupChnlRequestDetail(ChnlGroupChannelRequestDetail chnlGroupChnlRequestDetail) {
        ChannelGroup channelGroup = new ChannelGroup();
        channelGroup.setSiteId(chnlGroupChnlRequestDetail.getSiteId());
        channelGroup.setId(chnlGroupChnlRequestDetail.getId());
        channelGroup.setGroupId(chnlGroupChnlRequestDetail.getGroupId());
        channelGroup.setChnlId(chnlGroupChnlRequestDetail.getChnlId());
        return channelGroup;
    }

    private ChnlGroupChannelResponseDetail getChnlGroupByChnlGroupChnlReponseDetail(ChannelGroup channelGroup) {
        ChnlGroupChannelResponseDetail chnlGroupChnlResponseDetail = new ChnlGroupChannelResponseDetail();
        chnlGroupChnlResponseDetail.setSiteId(channelGroup.getSiteId());
        chnlGroupChnlResponseDetail.setId(channelGroup.getId());
        chnlGroupChnlResponseDetail.setGroupId(channelGroup.getGroupId());
        chnlGroupChnlResponseDetail.setChnlId(channelGroup.getChnlId());
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
            ChannelGroup channelGroup = chnlGroupMapper.selectBySiteIdAndGroupIdAndChnlId(siteId, groupId, chnlId);
            if (channelGroup != null) {//存在当前记录，无需添加，直接跳过
                continue;
            } else {
                channelGroup = new ChannelGroup();
                channelGroup.setId(null);
                channelGroup.setSiteId(siteId);
                channelGroup.setGroupId(groupId);
                channelGroup.setChnlId(chnlId);
                num = chnlGroupMapper.insert(channelGroup);
            }
        }
        return num;
    }
}
