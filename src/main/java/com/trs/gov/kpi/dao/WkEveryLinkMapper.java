package com.trs.gov.kpi.dao;

import com.trs.gov.kpi.entity.wangkang.WkEveryLink;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by li.hao on 2017/7/13.
 */
@Mapper
public interface WkEveryLinkMapper {

    /**
     * 查询一次检测的平均速度
     *
     * @param siteId
     * @param checkId
     * @return
     */
    Integer selectOnceCheckAvgSpeed(@Param("siteId") Integer siteId, @Param("checkId") Integer checkId);

    /**
     * 查询一次检测的网站更新数
     *
     * @param siteId
     * @param checkId
     * @return
     */
    Integer selectOnceCheckUpdateContent(@Param("siteId") Integer siteId, @Param("checkId") Integer checkId);

    /**
     * 获取当前检测所有文档信息
     *
     * @param siteId
     * @param checkId
     * @return
     */
    List<WkEveryLink> getList(@Param("siteId") Integer siteId, @Param("checkId") Integer checkId);

}
