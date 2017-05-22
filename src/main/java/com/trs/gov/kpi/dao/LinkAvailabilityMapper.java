package com.trs.gov.kpi.dao;

import com.trs.gov.kpi.entity.IssueBase;
import com.trs.gov.kpi.entity.LinkAvailability;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by rw103 on 2017/5/11.
 */
@Mapper
public interface LinkAvailabilityMapper extends OperationMapper {

    /**
     * 获取每月新增问题数
     *
     * @param issueBase
     * @return
     */
    int getLinkIssueHistoryCount(@Param("issueBase") IssueBase issueBase);

    /**
     * 查询未解决问题集合
     *
     * @param currPage
     * @param pageSize
     * @param issueBase
     * @return
     */
    List<LinkAvailability> getIssueList(@Param("currPage") Integer currPage, @Param("pageSize") Integer pageSize, @Param("issueBase") IssueBase issueBase);

    /**
     * 批量添加链接问题
     *
     * @param linkAvailabilities
     */
    void batchInsertLinkAvailabilities(@Param("linkAvailabilities") List<LinkAvailability> linkAvailabilities);

    /**
     * 查询未解决失效链接的问题数
     *
     * @param issueBase
     * @return
     */
    int getInvalidLinkCount(@Param("issueBase") IssueBase issueBase);

    /**
     * 查询未解决失效图片的问题数
     *
     * @param issueBase
     * @return
     */
    int getInvalidImageCount(@Param("issueBase") IssueBase issueBase);

    /**
     * 查询未解决连接超时的问题数
     *
     * @param issueBase
     * @return
     */
    int getConnTimeoutCount(@Param("issueBase") IssueBase issueBase);

}
