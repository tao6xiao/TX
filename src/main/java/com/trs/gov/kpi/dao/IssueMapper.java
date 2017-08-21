package com.trs.gov.kpi.dao;

import com.trs.gov.kpi.entity.*;
import com.trs.gov.kpi.entity.dao.DBRow;
import com.trs.gov.kpi.entity.dao.QueryFilter;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Mapper
public interface IssueMapper {

    int insert(DBRow row);

    Issue selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Issue record);

    int updateByPrimaryKey(Issue record);

    /**
     * 查询问题
     *
     * @param filter
     * @return
     */
    List<Issue> select(QueryFilter filter);

    /**
     * 查询InfoUpdate
     *
     * @param filter
     * @return
     */
    List<InfoUpdate> selectInfoUpdate(QueryFilter filter);

    /**
     * 查询InfoError
     *
     * @param filter
     * @return
     */
    List<InfoError> selectInfoError(QueryFilter filter);

    /**
     * 查询LinkAvailability
     *
     * @param filter
     * @return
     */
    List<LinkAvailability> selectLinkAvailability(QueryFilter filter);

    /**
     * 查询信息更新的工单
     *
     * @param filter
     * @return
     */
    List<InfoUpdateOrder> selectInfoUpdateOrder(QueryFilter filter);

    /**
     * 查询信息错误的工单
     *
     * @param filter
     * @return
     */
    List<InfoErrorOrder> selectInfoErrorOrder(QueryFilter filter);

    /**
     * 查询数量
     *
     * @param filter
     * @return
     */
    int count(QueryFilter filter);

    /**
     * 查询各栏目的更新不及时的mapList
     *
     * @param filter
     * @return
     */
    List<Map<Integer, Integer>> countList(QueryFilter filter);

    /**
     * 获取最早时间
     *
     * @return
     */
    Date getEarliestIssueTime();

    /**
     * 根据id批量处理问题
     *
     * @param siteId
     * @param ids
     */
    void handIssuesByIds(@Param("siteId") int siteId, @Param("ids") List<Integer> ids);

    /**
     * 根据id批量忽略问题
     *
     * @param siteId
     * @param ids
     */
    void ignoreIssuesByIds(@Param("siteId") int siteId, @Param("ids") List<Integer> ids);

    /**
     * 根据id批量删除问题
     *
     * @param siteId
     * @param ids
     */
    void delIssueByIds(@Param("siteId") int siteId, @Param("ids") List<Integer> ids);

    /**
     * 根据id批量设置问题所属部门
     *
     * @param siteId
     * @param ids
     */
    void updateDeptByIds(@Param("siteId") int siteId, @Param("ids") List<Integer> ids, @Param("deptId") int deptId);


    /**
     * 根据id批量更新工单状态
     *
     * @param workOrderStatus
     * @param ids
     */
    void updateOrderByIds(@Param("workOrderStatus") int workOrderStatus, @Param("ids") List<Integer> ids);

    /**
     * 获取当前站点下部门的id
     *
     * @param filter
     * @return
     */
    List<Map<String, Object>> getDepIssueCount(QueryFilter filter);

    /**
     * 获取指定站点下面部门的对应类型的map List
     *
     * @param filter
     * @return
     */
    List<Map<String, Object>> getDeptIdMap(QueryFilter filter);

    /**
     * 获取当前栏目最近一条记录的发现问题时间
     * @param filter
     * @return
     */
    List<Issue> getLastTimeCheckIssueList(QueryFilter filter);

    /**
     * 获取上一次Issue的checkTime
     * @param siteId
     * @param typeId
     * @param customer3
     * @return
     */
    Date grtLastTimeIssueCheckTime(@Param("siteId") int siteId, @Param("typeId")Integer typeId, @Param("customer3")String customer3);
}