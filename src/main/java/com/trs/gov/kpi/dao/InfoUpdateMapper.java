package com.trs.gov.kpi.dao;

import com.trs.gov.kpi.entity.InfoUpdate;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by rw103 on 2017/5/13.
 */
@Mapper
public interface InfoUpdateMapper extends OperationMapper {

    /**
     * 获取已解决问题数
     * @param siteId
     * @return
     */
    int getHandledIssueCount(@Param("siteId") int siteId);

    /**
     * 获取更新不及时问题数
     * @param siteId
     * @return
     */
    int getUpdateNotIntimeCount(@Param("siteId") int siteId);

    /**
     * 获取更新预警问题数
     * @param siteId
     * @return
     */
    int getUpdateWarningCount(@Param("siteId") int siteId);

    /**
     *查询未解决问题集合
     * @param currPage
     * @param pageSize
     * @param infoUpdate
     * @return
     */
    List<InfoUpdate> getIssueList(@Param("currPage") int currPage,@Param("pageSize") int pageSize,@Param("infoUpdate") InfoUpdate infoUpdate);
}
