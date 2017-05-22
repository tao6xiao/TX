package com.trs.gov.kpi.dao;

import com.trs.gov.kpi.entity.LinkAvailability;
import com.trs.gov.kpi.entity.dao.CondDBField;
import com.trs.gov.kpi.entity.dao.QueryFilter;
import com.trs.gov.kpi.entity.dao.QueryFilterPager;
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

    /**
     * 查询未解决问题集合
     * @param selectSql
     * @param condFields
     * @return
     */
    List<LinkAvailability> getIssueListBySql(@Param("selectSql") String selectSql, @Param("condFields") List<CondDBField> condFields, @Param("pager") QueryFilterPager pager);


    /**
     * 批量添加链接问题
     * @param linkAvailabilities
     */
    void batchInsertLinkAvailabilities(@Param("linkAvailabilities") List<LinkAvailability> linkAvailabilities);

    /**
     * 获取符合条件的链接问题数量
     *
     * @param condFields
     * @return
     */
    int getIssueCount(@Param("condFields") List<CondDBField> condFields);

}
