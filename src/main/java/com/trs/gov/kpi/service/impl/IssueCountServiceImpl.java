package com.trs.gov.kpi.service.impl;

import com.trs.gov.kpi.dao.IssueMapper;
import com.trs.gov.kpi.entity.responsedata.Statistics;
import com.trs.gov.kpi.service.IssueCountService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by he.lang on 2017/6/7.
 */
@Service
public class IssueCountServiceImpl implements IssueCountService {
    @Resource
    IssueMapper issueMapper;

    @Override
    public List<Statistics> countSort(Integer[] siteIds) {
        return null;
    }
}
