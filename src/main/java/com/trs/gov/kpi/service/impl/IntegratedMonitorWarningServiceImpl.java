package com.trs.gov.kpi.service.impl;

import com.trs.gov.kpi.service.IntegratedMonitorWarningService;

import java.util.Arrays;
import java.util.List;

/**
 * Created by he.lang on 2017/5/18.
 */
public class IntegratedMonitorWarningServiceImpl implements IntegratedMonitorWarningService {
    @Override
    public int dealWithWarningBySiteIdAndId(int siteId, Integer[] ids) {
        List<Integer> idList = Arrays.asList(ids);
        return 0;
    }

    @Override
    public int ignoreWarningBySiteIdAndId(int siteId, Integer[] ids) {
        List<Integer> idList = Arrays.asList(ids);
        return 0;
    }

    @Override
    public int deleteWarningBySiteIdAndId(int siteId, Integer[] ids) {
        List<Integer> idList = Arrays.asList(ids);
        return 0;
    }
}
