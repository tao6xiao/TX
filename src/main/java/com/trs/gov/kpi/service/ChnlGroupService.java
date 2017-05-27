package com.trs.gov.kpi.service;

import com.trs.gov.kpi.entity.requestdata.ChnlGroupChannelRequest;
import com.trs.gov.kpi.entity.requestdata.ChnlGroupChnlsAddRequest;
import com.trs.gov.kpi.entity.responsedata.ChnlGroupChannelResponse;
import com.trs.gov.kpi.entity.responsedata.ChnlGroupChnlsResponse;
import com.trs.gov.kpi.entity.responsedata.ChnlGroupsResponse;

import java.util.List;

/**
 * 栏目分类service接口
 * Created by he.lang on 2017/5/16.
 */
public interface ChnlGroupService {

    /**
     * 获取栏目分类
     *
     * @return
     */
    ChnlGroupsResponse[] getChnlGroupsResponseDetailArray();

    /**
     * 获取指定siteId和groupId（分类编号）和栏目编号的对象，主要用于添加栏目到当前栏目分类时的严验证当前记录是否存在
     *
     * @param siteId
     * @param groupId
     * @param chnlId
     * @return
     */
    ChnlGroupChannelResponse getBySiteIdAndGroupIdAndChnlId(int siteId, int groupId, int chnlId);

    /**
     * 通过站点id和反分类编号做分页查询
     *
     * @param siteId
     * @param groupId
     * @param pageIndex
     * @param pageSize
     * @return
     */
    List<ChnlGroupChnlsResponse> getPageDataBySiteIdAndGroupId(int siteId, int groupId, int pageIndex, int pageSize);

    /**
     * 获取指定的站点和根分类下面的记录总数
     *
     * @param siteId
     * @param groupId
     * @return
     */
    int getItemCountBySiteIdAndGroupId(int siteId, int groupId);

    /**
     * 更新当前站点、当前id记录的对应分类和栏目id
     *
     * @param chnlGroupChnlRequestDetail
     * @return
     */
    int updateBySiteIdAndId(ChnlGroupChannelRequest chnlGroupChnlRequestDetail);

    /**
     * 删除当前站点、当前id的对应记录
     *
     * @param siteId
     * @param id
     * @return
     */
    int deleteBySiteIdAndId(int siteId, int id);

    /**
     * 添加栏目
     *
     * @param chnlGroupChnlsAddRequest
     * @return
     */
    int addChnlGroupChnls(ChnlGroupChnlsAddRequest chnlGroupChnlsAddRequest);
}
