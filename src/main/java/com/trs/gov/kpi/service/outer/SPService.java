package com.trs.gov.kpi.service.outer;

import com.trs.gov.kpi.entity.outerapi.sp.SGPageDataRes;
import com.trs.gov.kpi.entity.outerapi.sp.SPHistoryStatistics;
import com.trs.gov.kpi.entity.outerapi.sp.SPStatistics;
import com.trs.gov.kpi.entity.requestdata.PageDataRequestParam;

import java.util.List;

/**
 * Created by ranwei on 2017/6/12.
 */
public interface SPService {

    /**
     * 查询服务指南的问题列表
     *
     * @param param
     * @return
     */
    SGPageDataRes getSGList(PageDataRequestParam param);

    /**
     * 查询服务实用的统计数量
     *
     * @param param
     * @return
     */
    SPStatistics getSGCount(PageDataRequestParam param);

    /**
     * 查询服务实用统计的历史记录
     *
     * @param param
     * @return
     */
    List<SPHistoryStatistics> getSPHistoryCount(PageDataRequestParam param);
}
