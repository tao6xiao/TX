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

    List<Issue> pageQuery(@Param("siteId") Integer siteId, @Param("isResolved") Integer isResolved, @Param("isDel") Boolean isDel, @Param("from") Integer from, @Param("count") Integer count);

    Integer countIssue(@Param("siteId") Integer siteId, @Param("isResolved") Integer isResolved, @Param("isDel") Boolean isDel);
}