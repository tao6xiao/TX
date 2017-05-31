package com.trs.gov.kpi.dao;

import com.trs.gov.kpi.entity.FrequencySetup;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FrequencySetupMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FrequencySetup record);

    int insertSelective(FrequencySetup record);

    FrequencySetup selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(FrequencySetup record);

    int updateByPrimaryKey(FrequencySetup record);

    /**
     * 查询当前站点的当前页数据
     * @param siteId
     * @param pageCalculate
     * @param pageSize
     * @return
     */
    List<FrequencySetup> selectPageDataFrequencySetupList(@Param("siteId") int siteId, @Param("pageCalculate") int pageCalculate, @Param("pageSize") int pageSize);

    /**
     * 获取当前siteId的记录总数
     * @param SiteId
     * @return
     */
    int selectCountFrequencySetupBySiteId(int SiteId);

    /**
     * 通过站点id和栏目id获取对应的栏目更新频率记录
     * @param siteId
     * @param chnlId
     * @return
     */
    FrequencySetup selectFrequencySetupBySiteIdAndChnlId(@Param("siteId") int siteId, @Param("chnlId") int chnlId);

//    /**
//     * 通过站点id和栏目修改对应的栏目更新频率记录
//     * @param siteId
//     * @param chnlId
//     * @return
//     */
//    int updateBySiteIdAndChnlId(@Param("siteId") int siteId, @Param("chnlId") int chnlId);

    /**
     * 更新对应的更新频率记录
     * @param frequencySetup
     * @return
     */
     int updateBySiteIdAndidAndChnlId(FrequencySetup frequencySetup);

    /**
     * 删除更新频率记录
     * @param siteId
     * @param id
     * @return
     */
    int deleteFrequencySetupBySiteIdAndId(@Param("siteId")int siteId, @Param("id") int id);

    /**
     * 用于获取指定站点下的指定预设记录
     * @param siteId
     * @param presetFeqId
     * @return
     */
    List<FrequencySetup> getBySiteIdAndPresetFeqId(@Param("siteId") int siteId, @Param("presetFeqId") int presetFeqId);
}