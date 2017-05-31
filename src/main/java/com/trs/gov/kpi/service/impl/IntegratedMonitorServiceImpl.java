package com.trs.gov.kpi.service.impl;

import com.trs.gov.kpi.constant.Status;
import com.trs.gov.kpi.constant.Types;
import com.trs.gov.kpi.dao.IssueMapper;
import com.trs.gov.kpi.entity.IssueIndicator;
import com.trs.gov.kpi.entity.dao.QueryFilter;
import com.trs.gov.kpi.entity.requestdata.PageDataRequestParam;
import com.trs.gov.kpi.entity.responsedata.Statistics;
import com.trs.gov.kpi.service.*;
import com.trs.gov.kpi.service.helper.QueryFilterHelper;
import com.trs.gov.kpi.utils.InitTime;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by ranwei on 2017/5/24.
 */
@Service
public class IntegratedMonitorServiceImpl implements IntegratedMonitorService {

    @Resource
    private LinkAvailabilityService linkAvailabilityService;

    @Resource
    private IssueMapper issueMapper;

    @Override
    public List<Statistics> getAllIssueCount(PageDataRequestParam param) {

        param.setBeginDateTime(InitTime.checkBeginDateTime(param.getBeginDateTime(), linkAvailabilityService.getEarliestIssueTime()));
        param.setEndDateTime(InitTime.checkEndDateTime(param.getEndDateTime()));

        QueryFilter queryFilter = QueryFilterHelper.toFilter(param);
        queryFilter.addCond("typeId", Arrays.asList(Types.IssueType.LINK_AVAILABLE_ISSUE.value,Types.IssueType.INFO_ERROR_ISSUE.value,Types.IssueType.INFO_UPDATE_ISSUE.value));
        queryFilter.addCond("isResolved", Arrays.asList(Status.Resolve.IGNORED.value, Status.Resolve.RESOLVED.value));

        int handledCount = issueMapper.count(queryFilter);

        queryFilter = QueryFilterHelper.toFilter(param);
        queryFilter.addCond("typeId", Arrays.asList(Types.IssueType.LINK_AVAILABLE_ISSUE.value,Types.IssueType.INFO_ERROR_ISSUE.value,Types.IssueType.INFO_UPDATE_ISSUE.value));
        queryFilter.addCond("isResolved", Status.Resolve.UN_RESOLVED);
        queryFilter.addCond("isDel", Status.Delete.UN_DELETE.value);
        int unhandledCount = issueMapper.count(queryFilter);

        queryFilter = QueryFilterHelper.toFilter(param);
        queryFilter.addCond("typeId", Arrays.asList(Types.IssueType.INFO_UPDATE_WARNING.value,Types.IssueType.RESPOND_WARNING.value));
        queryFilter.addCond("isResolved", Status.Resolve.UN_RESOLVED);
        queryFilter.addCond("isDel", Status.Delete.UN_DELETE.value);
        int warningCount = issueMapper.count(queryFilter);

        Statistics handledStatistics = new Statistics();
        handledStatistics.setName(IssueIndicator.SOLVED.name);
        handledStatistics.setType(IssueIndicator.SOLVED.value);
        handledStatistics.setCount(handledCount);

        Statistics unhandledStatistics = new Statistics();
        unhandledStatistics.setName(IssueIndicator.UN_SOLVED.name);
        unhandledStatistics.setType(IssueIndicator.UN_SOLVED.value);
        unhandledStatistics.setCount(unhandledCount);

        Statistics warningStatistics = new Statistics();
        warningStatistics.setName(IssueIndicator.WARNING.name);
        warningStatistics.setType(IssueIndicator.WARNING.value);
        warningStatistics.setCount(warningCount);

        List<Statistics> list = new ArrayList<>();
        list.add(handledStatistics);
        list.add(unhandledStatistics);
        list.add(warningStatistics);

        return list;
    }

    @Override
    public List<Statistics> getUnhandledIssueCount(PageDataRequestParam param) {

        List<Statistics> list = new ArrayList<>();

        param.setBeginDateTime(InitTime.checkBeginDateTime(param.getBeginDateTime(), linkAvailabilityService.getEarliestIssueTime()));
        param.setEndDateTime(InitTime.checkEndDateTime(param.getEndDateTime()));

        //查询失效链接数量
        QueryFilter queryFilter = QueryFilterHelper.toFilter(param);
        queryFilter.addCond("subTypeId", Types.LinkAvailableIssueType.INVALID_LINK.value);
        queryFilter.addCond("isResolved", Status.Resolve.UN_RESOLVED.value);
        queryFilter.addCond("isDel", Status.Delete.UN_DELETE.value);
        int invalidLinkCount = issueMapper.count(queryFilter);
        Statistics invalidLinkStatistics = new Statistics();
        invalidLinkStatistics.setCount(invalidLinkCount);
        invalidLinkStatistics.setType(Types.LinkAvailableIssueType.INVALID_LINK.value);
        invalidLinkStatistics.setName(Types.LinkAvailableIssueType.INVALID_LINK.name);

        //查询失效图片数量
        queryFilter = QueryFilterHelper.toFilter(param);
        queryFilter.addCond("subTypeId", Types.LinkAvailableIssueType.INVALID_IMAGE.value);
        queryFilter.addCond("isResolved", Status.Resolve.UN_RESOLVED.value);
        queryFilter.addCond("isDel", Status.Delete.UN_DELETE.value);
        int invalidImageCount = issueMapper.count(queryFilter);
        Statistics invalidImageStatistics = new Statistics();
        invalidImageStatistics.setCount(invalidImageCount);
        invalidImageStatistics.setType(Types.LinkAvailableIssueType.INVALID_IMAGE.value);
        invalidImageStatistics.setName(Types.LinkAvailableIssueType.INVALID_IMAGE.name);

        //查询连接超时数量
        queryFilter = QueryFilterHelper.toFilter(param);
        queryFilter.addCond("subTypeId", Types.LinkAvailableIssueType.CONNECTION_TIME_OUT.value);
        queryFilter.addCond("isResolved", Status.Resolve.UN_RESOLVED.value);
        queryFilter.addCond("isDel", Status.Delete.UN_DELETE.value);
        int connTimeoutCount = issueMapper.count(queryFilter);
        Statistics connTimeoutStatistics = new Statistics();
        connTimeoutStatistics.setCount(connTimeoutCount);
        connTimeoutStatistics.setType(Types.LinkAvailableIssueType.CONNECTION_TIME_OUT.value);
        connTimeoutStatistics.setName(Types.LinkAvailableIssueType.CONNECTION_TIME_OUT.name);

        //查询失效附件数量
        queryFilter = QueryFilterHelper.toFilter(param);
        queryFilter.addCond("subTypeId", Types.LinkAvailableIssueType.INVALID_FILE.value);
        queryFilter.addCond("isResolved", Status.Resolve.UN_RESOLVED.value);
        queryFilter.addCond("isDel", Status.Delete.UN_DELETE.value);
        int invalidFileCount = issueMapper.count(queryFilter);
        Statistics invalidFileStatistics = new Statistics();
        invalidFileStatistics.setCount(invalidFileCount);
        invalidFileStatistics.setType(Types.LinkAvailableIssueType.INVALID_FILE.value);
        invalidFileStatistics.setName(Types.LinkAvailableIssueType.INVALID_FILE.name);

        //查询失效首页数量
        queryFilter = QueryFilterHelper.toFilter(param);
        queryFilter.addCond("subTypeId", Types.LinkAvailableIssueType.INVALID_HOME_PAGE.value);
        queryFilter.addCond("isResolved", Status.Resolve.UN_RESOLVED.value);
        queryFilter.addCond("isDel", Status.Delete.UN_DELETE.value);
        int invalidHomepageCount = issueMapper.count(queryFilter);
        Statistics invalidHomepageStatistics = new Statistics();
        invalidHomepageStatistics.setCount(invalidHomepageCount);
        invalidHomepageStatistics.setType(Types.LinkAvailableIssueType.INVALID_HOME_PAGE.value);
        invalidHomepageStatistics.setName(Types.LinkAvailableIssueType.INVALID_HOME_PAGE.name);

        list.add(invalidLinkStatistics);
        list.add(invalidImageStatistics);
        list.add(connTimeoutStatistics);
        list.add(invalidFileStatistics);
        list.add(invalidHomepageStatistics);


        //查询更新不及时数量
        queryFilter = QueryFilterHelper.toFilter(param);
        queryFilter.addCond("subTypeId", Types.InfoUpdateIssueType.UPDATE_NOT_INTIME.value);
        queryFilter.addCond("isResolved", Status.Resolve.UN_RESOLVED.value);
        queryFilter.addCond("isDel", Status.Delete.UN_DELETE.value);
        int updateNotIntimeCount = issueMapper.count(queryFilter);
        Statistics updateNotIntimeStatistics = new Statistics();
        updateNotIntimeStatistics.setCount(updateNotIntimeCount);
        updateNotIntimeStatistics.setType(Types.InfoUpdateIssueType.UPDATE_NOT_INTIME.value);
        updateNotIntimeStatistics.setName(Types.InfoUpdateIssueType.UPDATE_NOT_INTIME.name);

        list.add(updateNotIntimeStatistics);


        //查询错别字数量
        queryFilter = QueryFilterHelper.toFilter(param);
        queryFilter.addCond("subTypeId", Types.InfoErrorIssueType.TYPOS.value);
        queryFilter.addCond("isResolved", Status.Resolve.UN_RESOLVED.value);
        queryFilter.addCond("isDel", Status.Delete.UN_DELETE.value);
        int typosCount = issueMapper.count(queryFilter);
        Statistics typosStatistics = new Statistics();
        typosStatistics.setCount(typosCount);
        typosStatistics.setType(Types.InfoErrorIssueType.TYPOS.value);
        typosStatistics.setName(Types.InfoErrorIssueType.TYPOS.name);

        //查询敏感词数量
        queryFilter = QueryFilterHelper.toFilter(param);
        queryFilter.addCond("subTypeId", Types.InfoErrorIssueType.SENSITIVE_WORDS.value);
        queryFilter.addCond("isResolved", Status.Resolve.UN_RESOLVED.value);
        queryFilter.addCond("isDel", Status.Delete.UN_DELETE.value);
        int sensitiveWordsCount = issueMapper.count(queryFilter);
        Statistics sensitiveWordsStatistics = new Statistics();
        sensitiveWordsStatistics.setCount(sensitiveWordsCount);
        sensitiveWordsStatistics.setType(Types.InfoErrorIssueType.SENSITIVE_WORDS.value);
        sensitiveWordsStatistics.setName(Types.InfoErrorIssueType.SENSITIVE_WORDS.name);

        list.add(typosStatistics);
        list.add(sensitiveWordsStatistics);


        return list;
    }

    @Override
    public List<Statistics> getWarningCount(PageDataRequestParam param) {

        List<Statistics> list = new ArrayList<>();

        param.setBeginDateTime(InitTime.checkBeginDateTime(param.getBeginDateTime(), linkAvailabilityService.getEarliestIssueTime()));
        param.setEndDateTime(InitTime.checkEndDateTime(param.getEndDateTime()));

        //查询信息更新预警数量
        QueryFilter queryFilter = QueryFilterHelper.toFilter(param);
        queryFilter.addCond("subTypeId", Types.InfoUpdateWarningType.UPDATE_WARNING.value);
        queryFilter.addCond("isResolved", Status.Resolve.UN_RESOLVED.value);
        queryFilter.addCond("isDel", Status.Delete.UN_DELETE.value);
        int updateWarningCount = issueMapper.count(queryFilter);
        Statistics updateWarningStatistics = new Statistics();
        updateWarningStatistics.setCount(updateWarningCount);
        updateWarningStatistics.setType(Types.InfoUpdateWarningType.UPDATE_WARNING.value);
        updateWarningStatistics.setName(Types.InfoUpdateWarningType.UPDATE_WARNING.name);

        list.add(updateWarningStatistics);

        //查询自查预警数量
        queryFilter = QueryFilterHelper.toFilter(param);
        queryFilter.addCond("subTypeId", Types.InfoUpdateWarningType.SELF_CHECK_WARNING.value);
        queryFilter.addCond("isResolved", Status.Resolve.UN_RESOLVED.value);
        queryFilter.addCond("isDel", Status.Delete.UN_DELETE.value);
        int selfCheckCount = issueMapper.count(queryFilter);
        Statistics selfCheckStatistics = new Statistics();
        selfCheckStatistics.setCount(selfCheckCount);
        selfCheckStatistics.setType(Types.InfoUpdateWarningType.SELF_CHECK_WARNING.value);
        selfCheckStatistics.setName(Types.InfoUpdateWarningType.SELF_CHECK_WARNING.name);

        list.add(selfCheckStatistics);

        return list;
    }
}
