package com.trs.gov.kpi.entity;

import lombok.Data;

/**
 * Created by helang on 2017/5/12.
 */
@Data
public class MonitorFrequencyDeal extends MonitorFrequency {
    private String name;//类型名
    private Integer freqUnit;//频率粒度

}
