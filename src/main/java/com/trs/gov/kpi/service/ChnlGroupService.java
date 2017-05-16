package com.trs.gov.kpi.service;

import com.trs.gov.kpi.constant.ChnlGroups;
import com.trs.gov.kpi.entity.responsedata.ChnlGroupsResponseDetail;

/**
 * 栏目分类service接口
 * Created by he.lang on 2017/5/16.
 */
public interface ChnlGroupService {

    /**
     * 获取栏目分类
     * @return
     */
    ChnlGroupsResponseDetail[] getChnlGroupsResponseDetailArray();
}
