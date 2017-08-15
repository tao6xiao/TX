package com.trs.gov.kpi.entity.requestdata;

import lombok.Data;

import java.util.Arrays;

/**
 * 主要用于获取监测频率设置的参数
 * Created by helang on 2017/5/13.
 */
@Data
public class MonitorFrequencySetUp {

    private Integer siteId;
    private MonitorFrequencyFreq[] freqs;

    @Override
    public String toString() {
        return "{" +
                "siteId=" + siteId +
                ", freqs=" + Arrays.toString(freqs) +
                '}';
    }
}
