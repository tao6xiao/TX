package com.trs.gov.kpi.dao;

import com.trs.gov.kpi.entity.MonitorRecord;
import com.trs.gov.kpi.entity.dao.QueryFilter;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * Created by li.hao on 2017/8/8.
 */
@Mapper
public interface MonitorRecordMapper {

    /**
     * 查询最近的监测任务的开始时间
     *
     * @param siteId
     * @param taskId
     * @return
     */
    Date getMonitorBeginTime(@Param("siteId") Integer siteId, @Param("taskId") Integer taskId);

    /**
     * 查询最近的监测任务的结束时间
     *
     * @param siteId
     * @param taskId
     * @return
     */
    Date getMonitorEndTime(@Param("siteId") Integer siteId, @Param("taskId") Integer taskId);

    /**
     * 根据filter中的条件查询数据数量
     * @param filter
     * @return
     */
    // TODO REVIEW LINWEI 在CommonMap里面弄一个通用的Count方法
    int selectMonitorRecordCount(QueryFilter filter);

    /**
     * 根据filter中的条件查询数据列表
     * @param filter
     * @return
     */
    List<MonitorRecord> selectMonitorRecordList(QueryFilter filter);
}
