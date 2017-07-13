package com.trs.gov.kpi.service.wangkang;

import com.trs.gov.kpi.entity.responsedata.WkAvgSpeedAndUpdateContentResponse;
import com.trs.gov.kpi.entity.wangkang.WkAvgSpeed;
import com.trs.gov.kpi.entity.wangkang.WkUpdateContent;

import java.util.List;

/**
 * Created by li.hao on 2017/7/12.
 */
public interface WkAvgSpeedUpdateContentService {

    /**
     * 访问速度---查询网站平均访问速度历史记录
     *
     * @return
     */
    List<WkAvgSpeedAndUpdateContentResponse> getAvgSpeedHistory();

    /**
     * 网站更新---查询网站每次更新数量的历史记录
     *
     * @return
     */
    List<WkAvgSpeedAndUpdateContentResponse> getUpdateContentHistory();

    /**
     * 添加一次检测的平均访问速度
     *
     * @param wkAvgSpeed
     */
    void insertOnceCheckWkAvgSpeed(WkAvgSpeed wkAvgSpeed);

    /**
     * 添加一次检测的网站更新数
     *
     * @param wkUpdateContent
     */
    void insertOnceCheckWkUpdateContent(WkUpdateContent wkUpdateContent);

}
