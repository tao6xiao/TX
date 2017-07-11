package com.trs.gov.kpi.dao;

import com.trs.gov.kpi.entity.dao.QueryFilter;
import com.trs.gov.kpi.entity.wangkang.WkIssue;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Created by li.hao on 2017/7/11.
 */
@Mapper
public interface WkIssueMapper {

    /**
     * 查询所有错误网站
     * @param filter
     * @return
     */
    List<WkIssue> selectIssueSiteList(QueryFilter filter);

    /**
     * 根据网站编号，检查编号，问题类型编号查询可用链接数/错误信息数
     *
     * @param siteId
     * @param checkId
     * @param typeId
     * @return
     */
    Integer getWkIssueCount(Integer siteId, Integer checkId, Integer typeId);

}
