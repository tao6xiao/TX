package com.trs.gov.kpi.service;

import com.trs.gov.kpi.entity.FrequencyPreset;
import com.trs.gov.kpi.entity.requestdata.FrequencyPresetRequest;
import com.trs.gov.kpi.entity.responsedata.FrequencyPresetResponse;

import java.util.List;

/**
 * 更新频率及预警初设service接口
 * Created by he.lang on 2017/5/15.
 */
public interface FrequencyPresetService {

    /**
     * 通过当前站点id获取对应初设记录总数
     * @param siteId
     * @return
     */
    int getItemCountBySiteId(int siteId);

    /**
     * 通过站点id获取对应分页和分页大小的分页记录
     * @param siteId
     * @param pageIndex
     * @param pageSize
     * @return
     */
    List<FrequencyPresetResponse> getPageDataBySiteId(int siteId, int pageIndex, int pageSize);

    /**
     * 添加预设记录
     * @param frequencyPresetRequest
     * @return
     */
    int addFrequencyPreset(FrequencyPresetRequest frequencyPresetRequest);

    /**
     * 通过站点id和id修改更新频率和预警预设记录
     * @param frequencyPreset
     * @return
     */
    int updateBySiteIdAndId(FrequencyPreset frequencyPreset);

    /**
     * 通过站点id和id删除更新频率和预警预设记录
     * @param siteId
     * @param id
     * @return
     */
    int deleteFrequencyPresetBySiteIdAndId(int siteId, int id);

    /**
     * 检查当前预设id是否存在
     * @param id
     * @return
     */
    boolean isPresetFeqIdExist(int siteId, int id);
}
