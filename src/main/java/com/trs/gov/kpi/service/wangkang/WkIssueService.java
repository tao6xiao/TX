package com.trs.gov.kpi.service.wangkang;

import com.trs.gov.kpi.entity.wangkang.WkIssue;

import java.util.List;

/**
 * Created by linwei on 2017/7/14.
 */
public interface WkIssueService {

    int getErrorWordsCount(Integer siteId, Integer checkId);

    int getInvalidLinkCount(Integer siteId, Integer checkId);

    List<WkIssue> getErrorWordsList(Integer siteId, Integer checkId);

    List<WkIssue> getInvalidLinkList(Integer siteId, Integer checkId);
}
