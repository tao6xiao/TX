package com.trs.gov.kpi.entity;

import lombok.Data;

/**
 * Created by helang on 2017/5/12.
 */
@Data
public class MonitorFrequencyDeal {
    private Byte id;

    private String name;//类型名

    private Short value;

    private Integer freqUnit;//频率粒度

}
