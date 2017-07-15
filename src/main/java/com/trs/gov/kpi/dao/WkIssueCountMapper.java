package com.trs.gov.kpi.dao;

import com.trs.gov.kpi.entity.wangkang.WkIssueCount;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by li.hao on 2017/7/12.
 */
@Mapper
public interface WkIssueCountMapper {

    /**
     * 链接可用性---根据网站编号查询已解决和未解决问题总数
     *
     * @param siteId
     * @param typeId
     * @return
     */
    WkIssueCount getlinkAndContentStatsBySiteId(@Param("siteId") Integer siteId, @Param("typeId") Integer typeId);

    /**
     *  链接可用性---根据网站编号已解决和未解决问题总数历史记录
     *
     * @param siteId
     * @param typeId
     * @return
     */
    List<WkIssueCount> getlinkAndContentHistoryStatsBySiteId(@Param("siteId") Integer siteId, @Param("typeId") Integer typeId);

}
