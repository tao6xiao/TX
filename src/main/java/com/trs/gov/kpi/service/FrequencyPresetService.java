package com.trs.gov.kpi.service;

import com.trs.gov.kpi.entity.FrequencyPreset;
import com.trs.gov.kpi.entity.requestdata.FrequencyPresetRequestDeal;
import com.trs.gov.kpi.entity.responsedata.FrequencyPresetResponseDeal;
import org.apache.ibatis.annotations.Param;

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
    List<FrequencyPresetResponseDeal> getPageDataBySiteId(int siteId, int pageIndex, int pageSize);

    /**
     * 添加预设记录
     * @param frequencyPresetRequestDeal
     * @return
     */
    int addFrequencyPreset(FrequencyPresetRequestDeal frequencyPresetRequestDeal);

    /**
     * 通过站点id和id修改更新频率和预警预设记录
     * @param frequencyPreset
     * @return
     */
    int updateFrequencyPresetBySiteIdAndId(FrequencyPreset frequencyPreset);

    /**
     * 通过站点id和id删除更新频率和预警预设记录
     * @param siteId
     * @param id
     * @return
     */
    int deleteFrequencyPresetBySiteIdAndId(int siteId, int id);
}
