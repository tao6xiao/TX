package com.trs.gov.kpi.entity.requestdata;

import lombok.Data;

/**
 * 获取前端的更新频率及预警初设的数据处理类
 * Created by he.lang on 2017/5/15.
 */
@Data
public class FrequencyPresetRequest {
    private Integer siteId;

    private Integer updateFreq;

    private Integer alertFreq;
}
