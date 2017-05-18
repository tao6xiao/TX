package com.trs.gov.kpi.controller;

import com.trs.gov.kpi.constant.InfoErrorType;
import com.trs.gov.kpi.constant.InfoUpdateType;
import com.trs.gov.kpi.constant.InfoWarningType;
import com.trs.gov.kpi.constant.LinkIssueType;
import com.trs.gov.kpi.entity.Issue;
import com.trs.gov.kpi.entity.IssueBase;
import com.trs.gov.kpi.entity.IssueType;
import com.trs.gov.kpi.entity.exception.BizException;
import com.trs.gov.kpi.entity.responsedata.ApiPageData;
import com.trs.gov.kpi.entity.responsedata.Statistics;
import com.trs.gov.kpi.service.*;
import com.trs.gov.kpi.utils.InitQueryFiled;
import com.trs.gov.kpi.utils.PageInfoDeal;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 综合实时监测
 */
@RestController
@RequestMapping("/gov/kpi/issue")
public class IntegratedMonitorController {

    @Resource
    private LinkAvailabilityService linkAvailabilityService;

    @Resource
    private InfoUpdateService infoUpdateService;

    @Resource
    private InfoErrorService infoErrorService;

    @Resource
    private IssueService issueService;


    /**
     * 查询所有问题数量
     *
     * @param issueBase
     * @return
     */
    @RequestMapping(value = "/all/count", method = RequestMethod.GET)
    public Integer getAllIssueCount(@ModelAttribute IssueBase issueBase) {
        int linkAvailabilityCount = linkAvailabilityService.getHandledIssueCount(issueBase) + linkAvailabilityService.getUnhandledIssueCount(issueBase);
        int infoUpdateCount = infoUpdateService.getHandledIssueCount(issueBase) + infoUpdateService.getUpdateNotIntimeCount(issueBase) + infoUpdateService.getUpdateWarningCount(issueBase);
        int infoErrorCount = infoErrorService.getHandledIssueCount(issueBase) + infoErrorService.getUnhandledIssueCount(issueBase);
        return linkAvailabilityCount + infoErrorCount + infoUpdateCount;
    }

    /**
     * 查询各类问题的待解决问题数
     *
     * @param issueBase
     * @return
     */
    @RequestMapping(value = "/unhandled/bytype/count", method = RequestMethod.GET)
    public List<Statistics> getUnhandledIssueCount(@ModelAttribute IssueBase issueBase) {
        int linkAvailabilityCount = linkAvailabilityService.getUnhandledIssueCount(issueBase);
        int infoUpdateCount = infoUpdateService.getUpdateNotIntimeCount(issueBase);

        Statistics linkAvailabilityStatistics = new Statistics();
        linkAvailabilityStatistics.setCount(linkAvailabilityCount);
        linkAvailabilityStatistics.setType(IssueType.INVALID_LINK.value);
        linkAvailabilityStatistics.setName(IssueType.INVALID_LINK.name);

        Statistics infoUpdateStatistics = new Statistics();
        infoUpdateStatistics.setCount(infoUpdateCount);
        infoUpdateStatistics.setType(IssueType.UPDATE_NOT_INTIME.value);
        infoUpdateStatistics.setName(IssueType.UPDATE_NOT_INTIME.name);

        List<Statistics> list = new ArrayList<>();
        list.add(linkAvailabilityStatistics);
        list.add(infoUpdateStatistics);

        return list;
    }

    /**
     * 查询各类预警的待解决数
     *
     * @param issueBase
     * @return
     */
    @RequestMapping(value = "/warning/bytype/count")
    public List<Statistics> getWarningCount(@ModelAttribute IssueBase issueBase) {
        int infoUpdateCount = infoUpdateService.getUpdateWarningCount(issueBase);
        Statistics infoUpdateStatistics = new Statistics();
        infoUpdateStatistics.setCount(infoUpdateCount);
        infoUpdateStatistics.setType(InfoWarningType.UPDATE_WARNING.value);
        infoUpdateStatistics.setName(InfoWarningType.UPDATE_WARNING.name);

        List<Statistics> list = new ArrayList<>();
        list.add(infoUpdateStatistics);

        return list;
    }

    @RequestMapping(value = "/unhandled",method = RequestMethod.GET)
    public ApiPageData getAllIssueList(Integer currPage, Integer pageSize,@ModelAttribute Issue issue) throws BizException{
        if (issue.getSiteId() == null) {
            throw new BizException("站点编号为空");
        }
        //接受searchField和searchText，并返回对应的问题类型ID集合
        if (issue.getSearchText() != null && !issue.getSearchText().trim().isEmpty()) {
            List list = InitQueryFiled.init(issue.getSearchText(), issueService);
            issue.setIds(list);
        }
        //解决searchField和searchText为null或空字符串的情况
        if (issue.getSearchText() == null || issue.getSearchText() == "") {
            List list = new ArrayList();
            Integer exception = 0;
            list.add(exception);
            issue.setIds(list);
        }
        //解决searchText为null的情况，避免不合法的sql查询
        if (issue.getSearchText() == null) {
            issue.setSearchText("");
        }
        //增加查询条件：未解决、未删除
        issue.setIsResolved(0);
        issue.setIsDel(0);
        int itemCount = issueService.getAllIssueCount(issue);
        ApiPageData apiPageData = PageInfoDeal.getApiPageData(currPage, pageSize, itemCount);
        List<Issue> linkAvailabilityList = issueService.getAllIssueList(apiPageData.getPager().getCurrPage() - 1, apiPageData.getPager().getPageSize(), issue);
        for (Issue is : linkAvailabilityList) {
            if (is.getTypeId() == 1) {//为可用性链接问题
                if (is.getSubTypeId() == LinkIssueType.INVALID_LINK.value) {
                    is.setSubTypeName(LinkIssueType.INVALID_LINK.name);
                } else if (is.getSubTypeId() == LinkIssueType.INVALID_IMAGE.value) {
                    is.setSubTypeName(LinkIssueType.INVALID_IMAGE.name);
                } else if (is.getSubTypeId() == LinkIssueType.LINK_TIME_OUT.value) {
                    is.setSubTypeName(LinkIssueType.LINK_TIME_OUT.name);
                }
            } else if (is.getTypeId() == 2) {//为信息更新问题
                if (is.getSubTypeId() == InfoUpdateType.UPDATE_NOT_INTIME.value) {
                    is.setSubTypeName(InfoUpdateType.UPDATE_NOT_INTIME.name);
                }
            } else if (is.getTypeId() == 3) {//为信息错误问题
                if (is.getSubTypeId() == InfoErrorType.TYPOS.value) {
                    is.setSubTypeName(InfoErrorType.TYPOS.name);
                } else if (is.getSubTypeId() == InfoErrorType.SENSITIVE_WORDS.value) {
                    is.setSubTypeName(InfoErrorType.SENSITIVE_WORDS.name);
                }
            }
        }
        apiPageData.setData(linkAvailabilityList);
        return apiPageData;
    }


}
