package com.trs.gov.kpi.dao;

import com.trs.gov.kpi.entity.InfoError;
import com.trs.gov.kpi.entity.IssueBase;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by ran.wei on 2017/5/15.
 */
@Mapper
public interface InfoErrorMapper extends OperationMapper {


    /**
     * 获取每月新增问题数
     *
     * @param infoError
     * @return
     */
    int getIssueHistoryCount(@Param("infoError") InfoError infoError);

    /**
     * 查询未解决问题集合
     *
     * @param currPage
     * @param pageSize
     * @param infoError
     * @return
     */
    List<InfoError> getIssueList(@Param("currPage") Integer currPage, @Param("pageSize") Integer pageSize, @Param("infoError") InfoError infoError);

    /**
     * 查询未解决错别字的问题数
     *
     * @param issueBase
     * @return
     */
    int getTyposCount(@Param("issueBase") IssueBase issueBase);

    /**
     * 查询未解决敏感词的问题数
     *
     * @param issueBase
     * @return
     */
    int getSensitiveWordsCount(@Param("issueBase") IssueBase issueBase);
}
