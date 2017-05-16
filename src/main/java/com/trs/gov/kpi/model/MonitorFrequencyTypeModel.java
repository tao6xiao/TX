package com.trs.gov.kpi.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 单例模式，用于获取现在存在的所有频率类型，返回HashMap的数据
 * Created by helang on 2017/5/12.
 */
public class MonitorFrequencyTypeModel {

    private static Map<Integer, MonitorFrequencyType> types;

    static {
        types = new HashMap<>();
        types.put(1, new MonitorFrequencyType(1));
        types.put(2, new MonitorFrequencyType(2));
        types.put(3, new MonitorFrequencyType(3));
        types = Collections.unmodifiableMap(types);
    }

    private static MonitorFrequencyTypeModel monitorFrequencyTypeModel = null;

    private MonitorFrequencyTypeModel() {

    }

    public static Map<Integer, MonitorFrequencyType> getTypes() {

        return types;
    }


}
