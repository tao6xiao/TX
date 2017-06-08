package com.trs.gov.kpi.dao;

import com.trs.gov.kpi.entity.FrequencySetup;
import com.trs.gov.kpi.entity.Issue;
import com.trs.gov.kpi.entity.dao.DBPager;
import com.trs.gov.kpi.entity.dao.QueryFilter;
import com.trs.gov.kpi.entity.requestdata.FrequencySetupSelectRequest;
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
     * 模糊查询的分页查询
     * @param selectRequest
     * @param pager
     * @return
     */
    List<FrequencySetup> selectPageDataList(@Param("selectRequest") FrequencySetupSelectRequest selectRequest, @Param("pager") DBPager pager, @Param("frequencySetup") FrequencySetup frequencySetup);

    /**
     * 获取当前siteId的记录总数
     * @param siteId
     * @return
     */
    int selectCountFrequencySetupBySiteId(int siteId);

    /**
     * 通过站点id和栏目id获取对应的栏目更新频率记录
     * @param siteId
     * @param chnlId
     * @return
     */
    FrequencySetup selectFrequencySetupBySiteIdAndChnlId(@Param("siteId") int siteId, @Param("chnlId") int chnlId);

    /**
     * 更新对应的更新频率记录
     * @param frequencySetup
     * @return
     */
     int updateBySiteIdAndIdAndChnlId(FrequencySetup frequencySetup);

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

    /**
     * 批量关闭/开启
     * @param siteId
     * @param id
     * @param isOpen
     */
    int closeOrOpen(@Param("siteId") int siteId, @Param("id") int id, @Param("isOpen") byte isOpen);

    FrequencySetup selectBySiteIdAndId(@Param("siteId") int siteId, @Param("id") Integer id);

    /**
     * 查询数量
     *
     * @param filter
     * @return
     */
    int count(QueryFilter filter);

    /**
     * 查询问题
     *
     * @param filter
     * @return
     */
    List<FrequencySetup> select(QueryFilter filter);
}