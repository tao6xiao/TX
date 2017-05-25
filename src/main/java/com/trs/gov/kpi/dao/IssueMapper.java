package com.trs.gov.kpi.dao;

import com.trs.gov.kpi.entity.Issue;
import com.trs.gov.kpi.entity.IssueBase;
import com.trs.gov.kpi.entity.IssueExample;
import java.util.List;

import com.trs.gov.kpi.entity.dao.QueryFilter;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

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

}