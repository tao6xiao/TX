package com.trs.gov.kpi.dao;

import com.trs.gov.kpi.entity.ReplySpeed;
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
     * 查询监测时间最早的网页分析数据
     *
     * @return
     */
    Date getEarliestCheckTime();
}
