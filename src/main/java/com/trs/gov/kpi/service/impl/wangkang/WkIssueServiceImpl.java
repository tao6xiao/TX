package com.trs.gov.kpi.service.impl.wangkang;

import com.trs.gov.kpi.constant.Types;
import com.trs.gov.kpi.constant.WkAllStatsTableField;
import com.trs.gov.kpi.dao.CommonMapper;
import com.trs.gov.kpi.dao.WkIssueMapper;
import com.trs.gov.kpi.entity.dao.QueryFilter;
import com.trs.gov.kpi.entity.dao.Table;
import com.trs.gov.kpi.entity.wangkang.WkIssue;
import com.trs.gov.kpi.service.wangkang.WkIssueService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by linwei on 2017/7/14.
 */
@Service
public class WkIssueServiceImpl implements WkIssueService {

    @Resource
    private CommonMapper commonMapper;

    @Resource
    private WkIssueMapper wkIssueMapper;

    @Override
    public int getErrorWordsCount(Integer siteId, Integer checkId) {
        QueryFilter filter = new QueryFilter(Table.WK_ISSUE);
        filter.addCond(WkAllStatsTableField.CHECK_ID, checkId);
        filter.addCond(WkAllStatsTableField.SITE_ID, siteId);
        filter.addCond("typeId", Types.WkSiteCheckType.CONTENT_ERROR.value);
        return commonMapper.count(filter);
    }

    @Override
    public int getInvalidLinkCount(Integer siteId, Integer checkId) {
        QueryFilter filter = new QueryFilter(Table.WK_ISSUE);
        filter.addCond(WkAllStatsTableField.CHECK_ID, checkId);
        filter.addCond(WkAllStatsTableField.SITE_ID, siteId);
        filter.addCond("typeId", Types.WkSiteCheckType.INVALID_LINK.value);
        return commonMapper.count(filter);
    }

    @Override
    public List<WkIssue> getErrorWordsList(Integer siteId, Integer checkId) {
        QueryFilter filter = new QueryFilter(Table.WK_ISSUE);
        filter.addCond(WkAllStatsTableField.CHECK_ID, checkId);
        filter.addCond(WkAllStatsTableField.SITE_ID, siteId);
        filter.addCond("typeId", Types.WkSiteCheckType.CONTENT_ERROR.value);
        return wkIssueMapper.select(filter);
    }

    @Override
    public List<WkIssue> getInvalidLinkList(Integer siteId, Integer checkId) {
        QueryFilter filter = new QueryFilter(Table.WK_ISSUE);
        filter.addCond(WkAllStatsTableField.CHECK_ID, checkId);
        filter.addCond(WkAllStatsTableField.SITE_ID, siteId);
        filter.addCond("typeId", Types.WkSiteCheckType.INVALID_LINK.value);
        return wkIssueMapper.select(filter);
    }
}
