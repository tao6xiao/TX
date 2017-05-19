package com.trs.gov.kpi.service.impl;

import com.trs.gov.kpi.entity.Issue;
import com.trs.gov.kpi.service.IntegratedMonitorWarningService;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * Created by he.lang on 2017/5/18.
 */
@Service
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

    @Override
    public List<Issue> getPageDataWaringList(Integer pageIndex, Integer pageSize, Issue issue) {
        return null;
    }
}
