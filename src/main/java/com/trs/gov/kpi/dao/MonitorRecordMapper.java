package com.trs.gov.kpi.dao;

import com.trs.gov.kpi.entity.MonitorRecord;
import com.trs.gov.kpi.entity.dao.QueryFilter;
import org.apache.ibatis.annotations.Mapper;

import java.util.Date;
import java.util.List;

/**
 * Created by li.hao on 2017/8/8.
 */
@Mapper
public interface MonitorRecordMapper {

    /**
     * 根据最后一次监测完成时间获取首页可用性的状态
     *
     * @param filter
     * @return
     */
    Integer getResuleByLastEndTime(QueryFilter filter);

    /**
     * 查询最近的监测任务的结束时间
     *
     * @param filter
     * @return
     */
    Date getLastMonitorEndTime(QueryFilter filter);

    /**
     * 根据filter中的条件查询数据数量
     * @param filter
     * @return
     */
    // TODO REVIEW LINWEI DO_li.hao FIXED 在CommonMap里面弄一个通用的Count方法
    int selectMonitorRecordCount(QueryFilter filter);

    /**
     * 根据filter中的条件查询数据列表
     * @param filter
     * @return
     */
    List<MonitorRecord> selectMonitorRecordList(QueryFilter filter);
}
