package com.trs.gov.kpi.service;

import com.trs.gov.kpi.entity.FrequencySetup;
import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.gov.kpi.entity.requestdata.FrequencySetupSetRequest;
import com.trs.gov.kpi.entity.requestdata.FrequencySetupUpdateRequest;
import com.trs.gov.kpi.entity.responsedata.FrequencySetupResponse;

import java.text.ParseException;
import java.util.List;

/**
 * 栏目更新频率设置Service
 * Created by he.lang on 2017/5/16.
 */
public interface FrequencySetupService {
    /**
     * 查询当前站点的当前页数据
     *
     * @param siteId
     * @param pageIndex
     * @param pageSize
     * @return
     */
    List<FrequencySetupResponse> getPageDataFrequencySetupList(int siteId, int pageIndex, int pageSize) throws RemoteException;

    /**
     * 获取当前siteId的记录总数
     *
     * @param SiteId
     * @return
     */
    int getCountFrequencySetupBySite(int SiteId);

    /**
     * 通过站点id和栏目id获取对应的栏目更新频率记录
     *
     * @param siteId
     * @param chnlId
     * @return
     */
    FrequencySetup getFrequencySetupBySiteIdAndChnlId(int siteId, int chnlId);

    /**
     * 通过id修改对应的栏目更新频率记录
     *
     * @param frequencySetup
     * @return
     */
    int updateFrequencySetupById(FrequencySetup frequencySetup);

    /**
     * 通过id修改对应的栏目更新频率记录
     *
     * @param frequencySetupUpdateRequest
     * @return
     */
    int updateFrequencySetupById(FrequencySetupUpdateRequest frequencySetupUpdateRequest);

    /**
     * 插入更新频率记录
     *
     * @param frequencySetup
     * @return
     */
    int insert(FrequencySetup frequencySetup) throws ParseException;

    /**
     * 通过前段传入的frequencySetupSetRequestDetail对象获取想应的FrequencySetup对象
     *
     * @param frequencySetupSetRequest
     * @return
     */
    FrequencySetup getFrequencySetupByFrequencySetupSetRequestDetail(FrequencySetupSetRequest frequencySetupSetRequest, int chnlId);

    /**
     * 删除更新频率记录
     * @param siteId
     * @param id
     * @return
     */
    int deleteFrequencySetupBySiteIdAndId(int siteId, int id);
}
