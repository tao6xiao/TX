package com.trs.gov.kpi.dao;

import com.trs.gov.kpi.entity.ChannelGroup;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ChnlGroupMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ChannelGroup record);

    int insertSelective(ChannelGroup record);

    ChannelGroup selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ChannelGroup record);

    int updateByPrimaryKey(ChannelGroup record);

    /**
     * 获取指定siteId和groupId（分类编号）和栏目编号的对象，主要用于添加栏目到当前栏目分类时的严验证当前记录是否存在
     * @param siteId
     * @param groupId
     * @param chnlId
     * @return
     */
    ChannelGroup selectBySiteIdAndGroupIdAndChnlId(@Param("siteId") int siteId, @Param("groupId") int groupId, @Param("chnlId") int chnlId);

    /**
     * 通过站点id和反分类编号做分页查询
     * @param siteId
     * @param groupId
     * @param pageCalculate
     * @param pageSize
     * @return
     */
    List<ChannelGroup> selectPageDataBySiteIdAndGroupId(@Param("siteId") int siteId, @Param("groupId") int groupId, @Param("pageCalculate") int pageCalculate, @Param("pageSize") int pageSize);

    /**
     * 获取指定的站点和根分类下面的记录总数
     * @param siteId
     * @param groupId
     * @return
     */
    int selectItemCountBySiteIdAndGroupId(@Param("siteId") int siteId, @Param("groupId") int groupId);

    /**
     * 更新当前站点、当前id记录的对应分类和栏目id
     * @param channelGroup
     * @return
     */
    int updateBySiteIdAndId(ChannelGroup channelGroup);

    /**
     * 删除当前站点、当前id的对应记录
     * @param siteId
     * @param id
     * @return
     */
    int deleteBySiteIdAndId(@Param("siteId") int siteId, @Param("id") int id);
}