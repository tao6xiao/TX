package com.trs.gov.kpi.model;

import java.util.HashMap;

/**
 * 单例模式，用于获取现在存在的所有频率类型，返回HashMap的数据
 * Created by helang on 2017/5/12.
 */
public class MonitorFrequencyTypeModel {

    private HashMap<Integer, MonitorFrequencyType> types ;

    private MonitorFrequencyTypeModel() {
        types.put(1, new MonitorFrequencyType(1));
        types.put(2, new MonitorFrequencyType(2));
        types.put(3, new MonitorFrequencyType(3));
    }

    public HashMap<Integer, MonitorFrequencyType> getTypes() {
        return types;
    }


}
