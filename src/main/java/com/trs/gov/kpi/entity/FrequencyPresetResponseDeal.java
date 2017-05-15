package com.trs.gov.kpi.entity;

import lombok.Data;

/**
 * 返回到前段的更新频率及预警初设的数据处理类
 * Created by he.lang on 2017/5/15.
 */
@Data
public class FrequencyPresetResponseDeal {
    private Integer id;

    private Integer updateFreq;

    private Integer alertFreq;
}
