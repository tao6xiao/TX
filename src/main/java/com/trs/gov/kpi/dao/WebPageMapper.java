package com.trs.gov.kpi.dao;

import com.trs.gov.kpi.entity.*;
import com.trs.gov.kpi.entity.dao.QueryFilter;
import org.apache.ibatis.annotations.Mapper;

import java.util.Date;
import java.util.List;

/**
 * Created by ranwei on 2017/6/6.
 */
@Mapper
public interface WebPageMapper {

    /**
     * 查询数量
     *
     * @param filter
     * @return
     */
    int count(QueryFilter filter);

    /**
     * 查询响应速度
     *
     * @param filter
     * @return
     */
    List<ReplySpeed> selectReplySpeed(QueryFilter filter);

    /**
     * 查询过大页面
     *
     * @param filter
     * @return
     */
    List<PageSpace> selectPageSpace(QueryFilter filter);

    /**
     * 查询过深页面
     *
     * @param filter
     * @return
     */
    List<PageDepth> selectPageDepth(QueryFilter filter);

    /**
     * 查询冗余代码
     *
     * @param filter
     * @return
     */
    List<RepeatCode> selectRepeatCode(QueryFilter filter);

    /**
     * 查询过长URL页面
     *
     * @param filter
     * @return
     */
    List<UrlLength> selectUrlLength(QueryFilter filter);

    /**
     * 查询监测时间最早的网页分析数据
     *
     * @return
     */
    Date getEarliestCheckTime();
}
