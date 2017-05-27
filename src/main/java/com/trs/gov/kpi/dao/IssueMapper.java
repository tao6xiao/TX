package com.trs.gov.kpi.dao;

import com.trs.gov.kpi.entity.*;
import com.trs.gov.kpi.entity.dao.QueryFilter;
import com.trs.gov.kpi.entity.requestdata.PageDataRequestParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Mapper
public interface IssueMapper {
    int countByExample(IssueExample example);

    int insert(Issue record);

    int insertSelective(Issue record);

    List<Issue> selectByExample(IssueExample example);

    Issue selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Issue record, @Param("example") IssueExample example);

    int updateByExample(@Param("record") Issue record, @Param("example") IssueExample example);

    int updateByPrimaryKeySelective(Issue record);

    int updateByPrimaryKey(Issue record);

    int getAllIssueCount(@Param("issue") IssueBase issue);

    List<Issue> getAllIssueList(@Param("currPage") Integer currPage, @Param("pageSize") Integer pageSize,@Param("issue") IssueBase issue);

    /**
     * 获取预警提醒记录总数
     * @param issue
     * @return
     */
    int getAllWarningCount(@Param("issue") IssueBase issue);

    /**
     * 获取预警提醒当前页的分页数据
     * @param currPage
     * @param pageSize
     * @param issue
     * @return
     */
    List<Issue> getAllWarningList(@Param("currPage") Integer currPage, @Param("pageSize") Integer pageSize,@Param("issue") IssueBase issue);

    /**
     * 获取已解决分页数据（通用方法，已解决，已忽略）
     * @param pageCalculate
     * @param pageSize
     * @param issue
     * @return
     */
    List<Issue> selectPageDataIsResolvedList(@Param("pageCalculate") Integer pageCalculate, @Param("pageSize") Integer pageSize,@Param("issue") IssueBase issue);

    /**
     * 获取已解决的数据条数（通用方法，已解决，已忽略）
     * @param issue
     * @return
     */
    int selectPageDataIsResolvedItemCount(@Param("issue") IssueBase issue);


    /**
     * 查询问题
     *
     * @param filter
     * @return
     */
    List<Issue> select(QueryFilter filter);

    /**
     * 查询InfoUpdate
     * @param filter
     * @return
     */
    List<InfoUpdate> selectInfoUpdate(QueryFilter filter);

    /**
     * 查询InfoUpdate
     * @param filter
     * @return
     */
    List<InfoError> selectInfoError(QueryFilter filter);

    /**
     * 查询LinkAvailability
     * @param filter
     * @return
     */
    List<LinkAvailability> selectLinkAvailability(QueryFilter filter);

    /**
     * 根据站点编号获取首页
     *
     * @param param
     * @return
     */
    String getIndexUrl(@Param("param")PageDataRequestParam param);

    /**
     * 查询首页不可用问题的监测时间
     *
     * @param filter
     * @return
     */
    Date getMonitorTime(QueryFilter filter);

    /**
     * 查询问题中是否包含首页不可用
     *
     * @param filter
     * @return
     */
    int getIndexAvailability(QueryFilter filter);

    /**
     * 查询数量
     * @param filter
     * @return
     */
    int count(QueryFilter filter);

    /**
     * 查询各栏目的更新不及时的mapList
     * @param filter
     * @return
     */
    List<Map<Integer,Integer>> countList(QueryFilter filter);

    /**
     * 获取最早时间
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
     * 获取更新不及时的栏目id
     *
     * @param filter
     * @return
     */
    List<Integer> getIdsUpdateNotInTime(QueryFilter filter);



}