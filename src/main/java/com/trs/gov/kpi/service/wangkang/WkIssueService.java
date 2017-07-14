package com.trs.gov.kpi.service.wangkang;

/**
 * Created by linwei on 2017/7/14.
 */
public interface WkIssueService {

    int getErrorWordsCount(Integer siteId, Integer checkId);

    int getInvalidLinkCount(Integer siteId, Integer checkId);
}
