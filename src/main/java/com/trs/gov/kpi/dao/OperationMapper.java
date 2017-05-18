package com.trs.gov.kpi.dao;

import com.trs.gov.kpi.entity.Issue;
import com.trs.gov.kpi.entity.IssueBase;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * Created by rw103 on 2017/5/13.
 */
@Mapper
public interface OperationMapper {

    /**
     * 获取已解决问题数
     *
     * @param issueBase
     * @return
     */
    int getHandledIssueCount(@Param("issueBase") IssueBase issueBase);

    /**
     * 获取未解决问题数
     *
     * @param issueBase
     * @return
     */
    int getUnhandledIssueCount(@Param("issueBase") IssueBase issueBase);

    /**
     * 获取更新不及时问题数
     *
     * @param issueBase
     * @return
     */
    int getUpdateNotIntimeCount(@Param("issueBase") IssueBase issueBase);

    /**
     * 获取更新预警问题数
     *
     * @param issueBase
     * @return
     */
    int getUpdateWarningCount(@Param("issueBase") IssueBase issueBase);

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
     * 查询存在时间最久的问题的时间
     *
     * @return
     */
    Date getEarliestIssueTime();
}
