package com.trs.gov.kpi.entity.requestdata;

import lombok.Data;

/**
 * 主要用与监测频率设置时传入的Freqs对象数组中单个对象构建
 * Created by helang on 2017/5/13.
 */
@Data
public class MonitorFrequencyFreq {

    private Byte id;//类型编号
    private Short value;//频率
}
