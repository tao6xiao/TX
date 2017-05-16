package com.trs.gov.kpi.model;

/**
 * 监测频率类型
 * Created by helang on 2017/5/12.
 */
public class MonitorFrequencyType {
    // TODO: 2017/5/16 enum deal with

    public static final String HOMEPAGE_AVAILABILITY = "首页可用性";

    public static final String TOTAL_BROKEN_LINKS = "全站失效链接";

    public static final String WRONG_INFORMATION = "信息错误";

    public static final Integer FREQUNIT_ONE = 1;// 次/天
    public static final Integer FREQUNIT_TWO = 2;// 天/次

    public static final Integer TYPE_NUM = 3;//类型数量

    private String name;//类型名
    private Integer freqUnit;//频率粒度

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getFreqUnit() {
        return freqUnit;
    }

    public void setFreqUnit(Integer freqUnit) {
        this.freqUnit = freqUnit;
    }
    public MonitorFrequencyType(){}
    public MonitorFrequencyType(int id){
        if(id == 1){//首页可用性
            name = HOMEPAGE_AVAILABILITY;
            freqUnit = FREQUNIT_ONE;
        }else if(id == 2){//全站失效链接
            name = TOTAL_BROKEN_LINKS;
            freqUnit =FREQUNIT_TWO;
        }else if(id == 3){//信息错误
            name = WRONG_INFORMATION;
            freqUnit = FREQUNIT_TWO;
        }
    }
}
