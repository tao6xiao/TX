package com.trs.gov.kpi.service.impl;

import com.trs.gov.kpi.constant.ChnlGroups;
import com.trs.gov.kpi.entity.responsedata.ChnlGroupsResponseDetail;
import com.trs.gov.kpi.service.ChnlGroupService;
import org.springframework.stereotype.Service;

/**
 * Created by he.lang on 2017/5/16.
 */
@Service
public class ChnlGroupServiceImp implements ChnlGroupService {

    @Override
    public ChnlGroupsResponseDetail[] getChnlGroupsResponseDetailArray() {
        ChnlGroups[] chnlGroups = ChnlGroups.getChnlGroups();
        ChnlGroupsResponseDetail[] chnlGroupsResponseDetails = new ChnlGroupsResponseDetail[chnlGroups.length];
        for (int i = 0; i < chnlGroups.length; i++){
            chnlGroupsResponseDetails[i] = new ChnlGroupsResponseDetail();
            chnlGroupsResponseDetails[i].setId(chnlGroups[i].getId());
            chnlGroupsResponseDetails[i].setName(chnlGroups[i].getName());
        }
        return chnlGroupsResponseDetails;
    }
}
