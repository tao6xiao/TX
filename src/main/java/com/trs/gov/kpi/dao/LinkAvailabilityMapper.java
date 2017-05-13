package com.trs.gov.kpi.dao;

import com.trs.gov.kpi.entity.LinkAvailability;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by rw103 on 2017/5/11.
 */
@Mapper
public interface LinkAvailabilityMapper {

    /**
     * 获取已解决问题数
     * @param siteId
     * @return
     */
    int getHandledIssueCount(@Param("siteId") int siteId);

    /**
     * 获取未解决问题数
     * @param siteId
     * @return
     */
    int getUnhandledIssueCount(@Param("siteId") int siteId);

    /**
     * 获取月末未解决问题数
     * 实现-->查询监测时间小于当月且未解决的问题
     * @param siteId
     * @return
     */
    int getUnhandledIssueCountByTime(@Param("siteId") int siteId);

    /**
     * 查询未解决问题
     * @return
     */
    List<LinkAvailability> getIssueList(@Param("currPage") int currPage,@Param("pageSize") int pageSize,@Param("linkAvailability") LinkAvailability linkAvailability);


    /**
     * 根据id处理问题
     * @param siteId
     * @param id
     */
    void handIssueById(@Param("siteId") int siteId, @Param("id") int id);

    /**
     * 根据id批量处理问题
     * @param siteId
     * @param ids
     */
    void handIssuesByIds(@Param("siteId") int siteId, @Param("ids") List<Integer> ids);

    /**
     * 根据id忽略问题
     * @param siteId
     * @param id
     */
    void ignoreIssueById(@Param("siteId") int siteId, @Param("id") int id);

    /**
     * 根据id批量忽略问题
     * @param siteId
     * @param ids
     */
    void ignoreIssuesByIds(@Param("siteId") int siteId, @Param("ids") List<Integer> ids);

    /**
     * 根据id批量删除问题
     * @param siteId
     * @param ids
     */
    void delIssueByIds(@Param("siteId") int siteId, @Param("ids") List<Integer> ids);
}
