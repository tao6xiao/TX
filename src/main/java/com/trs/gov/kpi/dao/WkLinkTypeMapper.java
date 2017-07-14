package com.trs.gov.kpi.dao;

import com.trs.gov.kpi.entity.wangkang.WkLinkType;
import org.apache.ibatis.annotations.Mapper;

/**
 * Created by li.hao on 2017/7/14.
 */
@Mapper
public interface WkLinkTypeMapper {

    /**
     * 根据网站编号查询网站链接总数和类型
     *
     * @param siteId
     * @return
     */
    WkLinkType getOneSiteLinkTypeBySiteId (Integer siteId);

}
