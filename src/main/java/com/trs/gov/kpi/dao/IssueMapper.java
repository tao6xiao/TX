package com.trs.gov.kpi.dao;

import com.trs.gov.kpi.entity.Issue;
import com.trs.gov.kpi.entity.IssueExample;
import java.util.List;

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

    int getAllIssueCount(@Param("issue") Issue issue);

    List<Issue> getAllIssueList(@Param("currPage") Integer currPage, @Param("pageSize") Integer pageSize,@Param("issue") Issue issue);

    int getAllWarningCount(@Param("issue") Issue issue);

    List<Issue> getAllWarningList(@Param("currPage") Integer currPage, @Param("pageSize") Integer pageSize,@Param("issue") Issue issue);
}