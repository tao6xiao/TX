package com.trs.gov.kpi.dao;

import com.trs.gov.kpi.entity.Report;
import com.trs.gov.kpi.entity.dao.QueryFilter;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Created by ranwei on 2017/6/12.
 */
@Mapper
public interface ReportMapper {

    /**
     * 查询报表列表
     *
     * @param filter
     * @return
     */
    List<Report> selectReportList(QueryFilter filter);

    /**
     * 查询报表列表数量
     *
     * @param filter
     * @return
     */
    int selectReportCount(QueryFilter filter);
}
