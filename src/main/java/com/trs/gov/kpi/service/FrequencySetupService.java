package com.trs.gov.kpi.service;

import com.trs.gov.kpi.entity.FrequencySetup;
import com.trs.gov.kpi.entity.requestdata.FrequencySetupSetRequestDetail;
import com.trs.gov.kpi.entity.responsedata.FrequencySetupResponseDetail;

import java.util.List;

/**
 * 栏目更新频率设置Service
 * Created by he.lang on 2017/5/16.
 */
public interface FrequencySetupService {
    /**
     * 查询当前站点的当前页数据
     * @param siteId
     * @param pageIndex
     * @param pageSize
     * @return
     */
    List<FrequencySetupResponseDetail> getPageDataFrequencySetupList(int siteId, int pageIndex, int pageSize);

    /**
     * 获取当前siteId的记录总数
     * @param SiteId
     * @return
     */
    int getCountFrequencySetupBySite(int SiteId);

    /**
     * 通过站点id和栏目id获取对应的栏目更新频率记录
     * @param siteId
     * @param chnlId
     * @return
     */
    FrequencySetup getFrequencySetupBySiteIdAndChnlId(int siteId, int chnlId);

    /**
     * 通过id修改对应的栏目更新频率记录
     * @param frequencySetup
     * @return
     */
    int updateFrequencySetupById(FrequencySetup frequencySetup );

    /**
     * 插入更新频率记录
     * @param frequencySetup
     * @return
     */
    int insert(FrequencySetup frequencySetup);

    /**
     * 通过前段传入的frequencySetupSetRequestDetail对象获取想应的FrequencySetup对象
     * @param frequencySetupSetRequestDetail
     * @return
     */
    FrequencySetup getFrequencySetupByFrequencySetupSetRequestDetail(FrequencySetupSetRequestDetail frequencySetupSetRequestDetail, int chnlId);
}
