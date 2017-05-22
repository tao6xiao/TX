package com.trs.gov.kpi.dao;

import com.trs.gov.kpi.entity.InfoUpdate;
import com.trs.gov.kpi.entity.IssueBase;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by rw103 on 2017/5/13.
 */
@Mapper
public interface InfoUpdateMapper extends OperationMapper {


    /**
     * 获取每月新增问题数
     *
     * @param issueBase
     * @return
     */
    int getIssueHistoryCount(@Param("issueBase") IssueBase issueBase);

    /**
     * 查询未解决问题集合
     *
     * @param currPage
     * @param pageSize
     * @param issueBase
     * @return
     */
    List<InfoUpdate> getIssueList(@Param("pageIndex") Integer pageIndex, @Param("pageSize") Integer pageSize, @Param("issueBase") IssueBase issueBase);

    /**
     * 查询更新不及时的问题数
     *
     * @param issueBase
     * @return
     */
    int getUpdateNotIntimeCount(@Param("issueBase") IssueBase issueBase);

    /**
     * 查询信息自查的预警数
     *
     * @param issueBase
     * @return
     */
    int getSelfWarningCount(@Param("issueBase") IssueBase issueBase);

    /**
     * 查询信息更新不及时的所有栏目数
     *
     * @param issueBase
     * @return
     */
    List<Map<Integer, Integer>> getAllUpdateNotInTime(@Param("issueBase") IssueBase issueBase);

    /**
     * 获取更新不及时的栏目id（通用）
     *
     * @param issueBase
     * @return
     */
    List<Integer> getIdsUpdateNotInTime(@Param("issueBase") IssueBase issueBase);

    /**
     * 获取更新不及时的栏目数（通用）
     *
     * @param issueBase
     * @return
     */
    List<Map<Integer, Integer>> getUpdateNotInTime(@Param("issueBase") IssueBase issueBase, @Param("childChnlIds") Set childChnlIds);

}
