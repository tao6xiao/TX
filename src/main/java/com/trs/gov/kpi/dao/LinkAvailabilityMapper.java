package com.trs.gov.kpi.dao;

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
     * @param linkAvailability
     * @return
     */
    int getIssueHistoryCount(@Param("linkAvailability") LinkAvailability linkAvailability);

    /**
     * 查询未解决问题集合
     *
     * @param currPage
     * @param pageSize
     * @param linkAvailability
     * @return
     */
    List<LinkAvailability> getIssueList(@Param("currPage") Integer currPage, @Param("pageSize") Integer pageSize, @Param("linkAvailability") LinkAvailability linkAvailability);

}
