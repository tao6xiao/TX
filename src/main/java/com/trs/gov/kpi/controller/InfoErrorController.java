package com.trs.gov.kpi.controller;


import com.trs.gov.kpi.entity.InfoError;
import com.trs.gov.kpi.entity.IssueBase;
import com.trs.gov.kpi.entity.exception.BizException;
import com.trs.gov.kpi.entity.responsedata.ApiPageData;
import com.trs.gov.kpi.entity.responsedata.HistoryStatistics;
import com.trs.gov.kpi.service.InfoErrorService;
import com.trs.gov.kpi.utils.InitQueryFiled;
import com.trs.gov.kpi.utils.IssueCounter;
import com.trs.gov.kpi.utils.PageInfoDeal;
import com.trs.gov.kpi.utils.ParamCheckUtil;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


/**
 * 信息错误问题
 */
@RestController
@RequestMapping("/gov/kpi/content/issue")
public class InfoErrorController {

    @Resource
    private InfoErrorService infoErrorService;


    /**
     * 查询待解决和已解决问题数量
     *
     * @param issueBase
     * @return
     */
    @RequestMapping(value = "/bytype/count", method = RequestMethod.GET)
    public List getIssueCount(IssueBase issueBase) throws BizException {
        // TODO: 2017/5/26 param check
//        ParamCheckUtil.paramCheck(issueBase);
        return IssueCounter.getIssueCount(infoErrorService, issueBase);
    }

    /**
     * 查询历史记录
     *
     * @param issueBase
     * @return
     */
    @RequestMapping(value = "/all/count/history", method = RequestMethod.GET)
    public List<HistoryStatistics> getIssueHistoryCount(@ModelAttribute IssueBase issueBase) throws BizException {
        if (issueBase.getBeginDateTime() == null || issueBase.getBeginDateTime().trim().isEmpty()) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            issueBase.setBeginDateTime(sdf.format(infoErrorService.getEarliestIssueTime()));
        }
        if(issueBase.getEndDateTime() ==null || issueBase.getEndDateTime().trim().isEmpty()){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            issueBase.setEndDateTime(sdf.format(new Date()));
        }
        // TODO: 2017/5/26 param check
//        ParamCheckUtil.paramCheck(issueBase);
        return infoErrorService.getIssueHistoryCount(issueBase);
    }

    /**
     * 查询待解决问题列表
     *
     * @param pageIndex
     * @param pageSize
     * @param issueBase
     * @return
     * @throws BizException
     */
    @RequestMapping(value = "/unhandled", method = RequestMethod.GET)
    public ApiPageData getIssueList(Integer pageIndex, Integer pageSize, @ModelAttribute IssueBase issueBase) throws BizException {

        // TODO: 2017/5/26 param check
//        ParamCheckUtil.pagerCheck(pageIndex, pageSize);
        if (issueBase.getSearchText() != null && !issueBase.getSearchText().trim().isEmpty()) {
            List list = InitQueryFiled.init(issueBase.getSearchText(), infoErrorService);
            issueBase.setIds(list);
        }
//        ParamCheckUtil.paramCheck(issueBase);
        int itemCount = infoErrorService.getUnhandledIssueCount(issueBase);
        ApiPageData apiPageData = PageInfoDeal.buildApiPageData(pageIndex, pageSize, itemCount);
        List<InfoError> infoErrorList = infoErrorService.getIssueList((apiPageData.getPager().getCurrPage() - 1) * apiPageData.getPager().getPageSize(), apiPageData.getPager().getPageSize(), issueBase);
        apiPageData.setData(infoErrorList);
        return apiPageData;
    }

    /**
     * 批量处理
     *
     * @param siteId
     * @param ids
     * @return
     */
    @RequestMapping(value = "/handle", method = RequestMethod.POST)
    public String handIssuesByIds(int siteId, Integer[] ids) {
        infoErrorService.handIssuesByIds(siteId, Arrays.asList(ids));
        return null;
    }

    /**
     * 批量忽略
     *
     * @param siteId
     * @param ids
     * @return
     */
    @RequestMapping(value = "/ignore", method = RequestMethod.POST)
    public String ignoreIssuesByIds(int siteId, Integer[] ids) {
        infoErrorService.ignoreIssuesByIds(siteId, Arrays.asList(ids));
        return null;
    }

    /**
     * 批量删除
     *
     * @param siteId
     * @param ids
     * @return
     */
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public String delIssueByIds(int siteId, Integer[] ids) {
        infoErrorService.delIssueByIds(siteId, Arrays.asList(ids));
        return null;
    }

}
