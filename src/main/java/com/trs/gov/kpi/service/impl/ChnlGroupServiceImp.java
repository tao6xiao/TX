package com.trs.gov.kpi.service.impl;

import com.trs.gov.kpi.constant.EnumChannelGroup;
import com.trs.gov.kpi.dao.ChnlGroupMapper;
import com.trs.gov.kpi.entity.ChannelGroup;
import com.trs.gov.kpi.entity.exception.BizException;
import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.gov.kpi.entity.outerapi.Channel;
import com.trs.gov.kpi.entity.requestdata.ChnlGroupChannelRequest;
import com.trs.gov.kpi.entity.requestdata.ChnlGroupChnlsAddRequest;
import com.trs.gov.kpi.entity.responsedata.Chnl;
import com.trs.gov.kpi.entity.responsedata.ChnlGroupChannelResponse;
import com.trs.gov.kpi.entity.responsedata.ChnlGroupChnlsResponse;
import com.trs.gov.kpi.entity.responsedata.ChnlGroupsResponse;
import com.trs.gov.kpi.service.ChnlGroupService;
import com.trs.gov.kpi.service.outer.SiteApiService;
import com.trs.gov.kpi.utils.LogUtil;
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
    public ChnlGroupsResponse[] getChnlGroupsResponseDetailArray() {
        EnumChannelGroup[] chnlGroups = EnumChannelGroup.getChnlGroups();
        ChnlGroupsResponse[] chnlGroupsResponses = new ChnlGroupsResponse[chnlGroups.length];
        for (int i = 0; i < chnlGroups.length; i++) {
            chnlGroupsResponses[i] = new ChnlGroupsResponse();
            chnlGroupsResponses[i].setId(chnlGroups[i].getId());
            chnlGroupsResponses[i].setName(chnlGroups[i].getName());
        }
        return chnlGroupsResponses;
    }

    @Override
    public ChnlGroupChannelResponse getBySiteIdAndGroupIdAndChnlId(int siteId, int groupId, int chnlId) {
        ChannelGroup channelGroup = chnlGroupMapper.selectBySiteIdAndGroupIdAndChnlId(siteId, groupId, chnlId);
        return getChnlGroupByChnlGroupChnlReponseDetail(channelGroup);
    }

    @Override
    public List<ChnlGroupChnlsResponse> getPageDataBySiteIdAndGroupId(int siteId, int groupId, int pageIndex, int pageSize) {
        int pageCalculate = pageIndex * pageSize;
        List<ChannelGroup> channelGroupList = chnlGroupMapper.selectPageDataBySiteIdAndGroupId(siteId, groupId, pageCalculate, pageSize);
        List<ChnlGroupChnlsResponse> chnlGroupChnlsResponseList = new ArrayList<>();
        ChnlGroupChnlsResponse chnlGroupChnlsResponse = null;

        Map<Integer, String> chnlNamCache = new HashMap<>();
        for (ChannelGroup channelGroup : channelGroupList) {
            chnlGroupChnlsResponse = getChnlGroupChnlsResponseDetailByChnlGroup(channelGroup, chnlNamCache);
            chnlGroupChnlsResponseList.add(chnlGroupChnlsResponse);
        }
        return chnlGroupChnlsResponseList;
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
                chnl.setChnlName(outerChannel.getChnlDesc());
                chnlNamCache.put(outerChannel.getChannelId(), outerChannel.getChnlDesc());
            } catch (RemoteException e) {
                log.error("failed to get channel by id: " + channelGroup.getChnlId(), e);
                LogUtil.addSystemLog("failed to get channel by id: " + channelGroup.getChnlId(), e);
            }
        }
        return chnl;
    }

    private ChnlGroupChnlsResponse getChnlGroupChnlsResponseDetailByChnlGroup(ChannelGroup channelGroup, Map<Integer, String> chnlNamCache) {
        ChnlGroupChnlsResponse responseChnl = new ChnlGroupChnlsResponse();
        responseChnl.setId(channelGroup.getId());
        responseChnl.setChnlGroupName(EnumChannelGroup.valueOf(channelGroup.getGroupId()).getName());
        responseChnl.setChnl(getChnl(channelGroup, chnlNamCache));
        return responseChnl;

    }

    @Override
    public int getItemCountBySiteIdAndGroupId(int siteId, int groupId) {
        return chnlGroupMapper.selectItemCountBySiteIdAndGroupId(siteId, groupId);
    }

    @Override
    public int updateBySiteIdAndId(ChnlGroupChannelRequest chnlGroupChnlRequestDetail) throws BizException {
        ChannelGroup channelGroup = chnlGroupMapper.selectBySiteIdAndGroupIdAndChnlId(chnlGroupChnlRequestDetail.getSiteId(), chnlGroupChnlRequestDetail.getGroupId(), chnlGroupChnlRequestDetail.getChnlId());
        int num = 0;
        if (channelGroup == null) {
            channelGroup = getChnlGroupByChnlGroupChnlRequestDetail(chnlGroupChnlRequestDetail);
            num = chnlGroupMapper.updateBySiteIdAndId(channelGroup);
        } else {
            //不为null，证明修改之后的记录与数据库表中记录相同，继续修改将冲突，所以不做操作
            log.info("当前站点："+chnlGroupChnlRequestDetail.getSiteId()+"下该栏目:"+ EnumChannelGroup.valueOf(chnlGroupChnlRequestDetail.getGroupId())+"下面已经存在此子栏目"+chnlGroupChnlRequestDetail.getChnlId());
            throw new BizException("该栏目下面已经存在此子栏目");
        }
        return num;
    }

    private ChannelGroup getChnlGroupByChnlGroupChnlRequestDetail(ChnlGroupChannelRequest chnlGroupChnlRequestDetail) {
        ChannelGroup channelGroup = new ChannelGroup();
        channelGroup.setSiteId(chnlGroupChnlRequestDetail.getSiteId());
        channelGroup.setId(chnlGroupChnlRequestDetail.getId());
        channelGroup.setGroupId(chnlGroupChnlRequestDetail.getGroupId());
        channelGroup.setChnlId(chnlGroupChnlRequestDetail.getChnlId());
        return channelGroup;
    }

    private ChnlGroupChannelResponse getChnlGroupByChnlGroupChnlReponseDetail(ChannelGroup channelGroup) {
        ChnlGroupChannelResponse chnlGroupChnlResponseDetail = new ChnlGroupChannelResponse();
        chnlGroupChnlResponseDetail.setSiteId(channelGroup.getSiteId());
        chnlGroupChnlResponseDetail.setId(channelGroup.getId());
        chnlGroupChnlResponseDetail.setGroupId(channelGroup.getGroupId());
        chnlGroupChnlResponseDetail.setChnlId(channelGroup.getChnlId());
        return chnlGroupChnlResponseDetail;
    }

    @Override
    public int deleteBySiteIdAndId(int siteId, int id) {
        return chnlGroupMapper.deleteBySiteIdAndId(siteId, id);
    }

    @Override
    public int addChnlGroupChnls(ChnlGroupChnlsAddRequest chnlGroupChnlsAddRequest) {
        int siteId = chnlGroupChnlsAddRequest.getSiteId();
        int groupId = chnlGroupChnlsAddRequest.getGroupId();
        Integer[] chnlIds = chnlGroupChnlsAddRequest.getChnlIds();
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
