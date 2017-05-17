package com.trs.gov.kpi.service.impl;

import com.trs.gov.kpi.constant.ChnlGroups;
import com.trs.gov.kpi.dao.ChnlGroupMapper;
import com.trs.gov.kpi.entity.ChnlGroup;
import com.trs.gov.kpi.entity.requestdata.ChnlGroupChnlRequestDetail;
import com.trs.gov.kpi.entity.requestdata.ChnlGroupChnlsAddRequestDetail;
import com.trs.gov.kpi.entity.responsedata.Chnl;
import com.trs.gov.kpi.entity.responsedata.ChnlGroupChnlResponseDetail;
import com.trs.gov.kpi.entity.responsedata.ChnlGroupChnlsResponseDetail;
import com.trs.gov.kpi.entity.responsedata.ChnlGroupsResponseDetail;
import com.trs.gov.kpi.service.ChnlGroupService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by he.lang on 2017/5/16.
 */
@Service
public class ChnlGroupServiceImp implements ChnlGroupService {
    @Resource
    ChnlGroupMapper chnlGroupMapper;

    @Override
    public ChnlGroupsResponseDetail[] getChnlGroupsResponseDetailArray() {
        ChnlGroups[] chnlGroups = ChnlGroups.getChnlGroups();
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
        ChnlGroup chnlGroup = chnlGroupMapper.selectBySiteIdAndGroupIdAndChnlId(siteId, groupId, chnlId);
        ChnlGroupChnlResponseDetail chnlGroupChnlResponseDetail = getChnlGroupByChnlGroupChnlReponseDetail(chnlGroup);
        return chnlGroupChnlResponseDetail;
    }

    @Override
    public List<ChnlGroupChnlsResponseDetail> getPageDataBySiteIdAndGroupId(int siteId, int groupId, int pageIndex, int pageSize) {
        int pageCalculate = pageIndex * pageSize;
        List<ChnlGroup> chnlGroupList = chnlGroupMapper.selectPageDataBySiteIdAndGroupId(siteId, groupId, pageCalculate, pageSize);
        List<ChnlGroupChnlsResponseDetail> chnlGroupChnlsResponseDetailList = new ArrayList<>();
        ChnlGroupChnlsResponseDetail chnlGroupChnlsResponseDetail = null;
        for (ChnlGroup chnlGroup : chnlGroupList) {
            chnlGroupChnlsResponseDetail = getChnlGroupChnlsResponseDetailByChnlGroup(chnlGroup);
            chnlGroupChnlsResponseDetailList.add(chnlGroupChnlsResponseDetail);
        }
        return chnlGroupChnlsResponseDetailList;
    }

    private Chnl getChnl(ChnlGroup chnlGroup) {
        // TODO: 2017/5/17 get chnl by siteId and chnlId from 采编中心
        Chnl chnl = new Chnl();
        return chnl;
    }

    private ChnlGroupChnlsResponseDetail getChnlGroupChnlsResponseDetailByChnlGroup(ChnlGroup chnlGroup) {
        ChnlGroupChnlsResponseDetail chnlGroupChnlsResponseDetail = new ChnlGroupChnlsResponseDetail();
        chnlGroupChnlsResponseDetail.setId(chnlGroup.getId());
        Chnl chnl = getChnl(chnlGroup);
        chnlGroupChnlsResponseDetail.setChnl(chnl);
        return chnlGroupChnlsResponseDetail;

    }

    @Override
    public int getItemCountBySiteIdAndGroupId(int siteId, int groupId) {
        int itemCount = chnlGroupMapper.selectItemCountBySiteIdAndGroupId(siteId, groupId);
        return itemCount;
    }

    @Override
    public int updateBySiteIdAndId(ChnlGroupChnlRequestDetail chnlGroupChnlRequestDetail) {
        ChnlGroup chnlGroup = chnlGroupMapper.selectBySiteIdAndGroupIdAndChnlId(chnlGroupChnlRequestDetail.getSiteId(), chnlGroupChnlRequestDetail.getGroupId(), chnlGroupChnlRequestDetail.getChnlId());
        int num = 0;
        if (chnlGroup == null) {
            chnlGroup = getChnlGroupByChnlGroupChnlRequestDetail(chnlGroupChnlRequestDetail);
            num = chnlGroupMapper.updateBySiteIdAndId(chnlGroup);
        } else {//不为null，证明修改之后的记录与数据库表中记录相同，继续修改将冲突，所以不做操作

        }
        return num;
    }

    private ChnlGroup getChnlGroupByChnlGroupChnlRequestDetail(ChnlGroupChnlRequestDetail chnlGroupChnlRequestDetail) {
        ChnlGroup chnlGroup = new ChnlGroup();
        chnlGroup.setSiteId(chnlGroupChnlRequestDetail.getSiteId());
        chnlGroup.setId(chnlGroupChnlRequestDetail.getId());
        chnlGroup.setGroupId(chnlGroupChnlRequestDetail.getGroupId());
        chnlGroup.setChnlId(chnlGroupChnlRequestDetail.getChnlId());
        return chnlGroup;
    }

    private ChnlGroupChnlResponseDetail getChnlGroupByChnlGroupChnlReponseDetail(ChnlGroup chnlGroup) {
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
            ChnlGroup chnlGroup = chnlGroupMapper.selectBySiteIdAndGroupIdAndChnlId(siteId, groupId, chnlId);
            if (chnlGroup != null) {//存在当前记录，无需添加，直接跳过
                continue;
            } else {
                chnlGroup = new ChnlGroup();
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
