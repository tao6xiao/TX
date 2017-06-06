package com.trs.gov.kpi.service;

import com.trs.gov.kpi.entity.requestdata.PageDataRequestParam;
import com.trs.gov.kpi.entity.responsedata.ApiPageData;

import java.util.Date;

/**
 * Created by ranwei on 2017/6/6.
 */
public interface WebPageService {

//    /**
//     * 查询响应速度数量
//     *
//     * @param param
//     * @return
//     */
//    int getReplySpeedCount(PageDataRequestParam param);

    /**
     * 查询响应速度
     *
     * @param param
     * @return
     */
    ApiPageData selectReplySpeed(PageDataRequestParam param);

    /**
     * 查询监测时间最早的网页分析数据
     *
     * @return
     */
    Date getEarliestCheckTime();
}
