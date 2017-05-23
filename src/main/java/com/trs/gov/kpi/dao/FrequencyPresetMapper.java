package com.trs.gov.kpi.dao;

import com.trs.gov.kpi.entity.FrequencyPreset;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 更新频率及预警初设mapper
 */
@Mapper
public interface FrequencyPresetMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FrequencyPreset record);

    int insertSelective(FrequencyPreset record);

    FrequencyPreset selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(FrequencyPreset record);

    int updateByPrimaryKey(FrequencyPreset record);

    /**
     * 通过当前站点id获取对应初设记录总数
     * @param siteId
     * @return
     */
    int selectItemCountBySiteId(int siteId);

    /**
     * 通过站点id获取对应分页和分页大小的分页记录
     * @param siteId
     * @param pageCalculate(=currPage*pageSize)
     * @param pageSize
     * @return
     */
    List<FrequencyPreset> selectPageDataBySiteId(@Param("siteId") int siteId, @Param("pageCalculate") int pageCalculate, @Param("pageSize") int pageSize);

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
    int deleteFrequencyPresetBySiteIdAndId(@Param("siteId") int siteId, @Param("id") int id);

    /**
     * 通过id查询预设记录
     *
     * @param siteId
     * @param id
     * @return
     */
    FrequencyPreset selectById(@Param("siteId") int siteId, @Param("id") int id);
}