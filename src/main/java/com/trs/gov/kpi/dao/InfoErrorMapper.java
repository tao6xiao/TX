//package com.trs.gov.kpi.dao;
//
//import com.trs.gov.kpi.entity.responsedata.InfoErrorResponse;
//import com.trs.gov.kpi.entity.IssueBase;
//import org.apache.ibatis.annotations.Mapper;
//import org.apache.ibatis.annotations.Param;
//
//import java.util.List;
//
///**
// * Created by ran.wei on 2017/5/15.
// */
//@Mapper
//public interface InfoErrorMapper extends OperationMapper {
//
//
//    /**
//     * 获取每月新增问题数
//     *
//     * @param issueBase
//     * @return
//     */
//    int getIssueHistoryCount(@Param("issueBase") IssueBase issueBase);
//
//    /**
//     * 查询未解决问题集合
//     *
//     * @param currPage
//     * @param pageSize
//     * @param issueBase
//     * @return
//     */
//    List<InfoErrorResponse> getIssueList(@Param("pageIndex") Integer pageIndex, @Param("pageSize") Integer pageSize, @Param("issueBase") IssueBase issueBase);
//
//    /**
//     * 查询未解决错别字的问题数
//     *
//     * @param issueBase
//     * @return
//     */
//    int getTyposCount(@Param("issueBase") IssueBase issueBase);
//
//    /**
//     * 查询未解决敏感词的问题数
//     *
//     * @param issueBase
//     * @return
//     */
//    int getSensitiveWordsCount(@Param("issueBase") IssueBase issueBase);
//}
