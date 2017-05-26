package com.trs.gov.kpi.controller;

import com.trs.gov.kpi.entity.IssueBase;
import com.trs.gov.kpi.entity.exception.BizException;
import com.trs.gov.kpi.entity.responsedata.Statistics;
import com.trs.gov.kpi.service.IntegratedMonitorService;
import com.trs.gov.kpi.utils.ParamCheckUtil;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 综合实时监测
 */
@RestController
@RequestMapping("/gov/kpi/issue")
public class IntegratedMonitorController {

    @Resource
    private IntegratedMonitorService integratedMonitorService;

    /**
     * 查询所有问题数量
     *
     * @param issueBase
     * @return
     */
    @RequestMapping(value = "/all/count", method = RequestMethod.GET)
    public List<Statistics> getAllIssueCount(@ModelAttribute IssueBase issueBase) throws BizException {

        // TODO: 2017/5/26 param check
//        ParamCheckUtil.paramCheck(issueBase);
        return integratedMonitorService.getAllIssueCount(issueBase);
    }

    /**
     * 查询各类问题的待解决问题数
     *
     * @param issueBase
     * @return
     */
    @RequestMapping(value = "/unhandled/bytype/count", method = RequestMethod.GET)
    public List<Statistics> getUnhandledIssueCount(@ModelAttribute IssueBase issueBase) throws BizException {

        // TODO: 2017/5/26 param check
//        ParamCheckUtil.paramCheck(issueBase);
        return integratedMonitorService.getUnhandledIssueCount(issueBase);
    }

    /**
     * 查询各类预警的待解决数
     *
     * @param issueBase
     * @return
     */
    @RequestMapping(value = "/warning/bytype/count")
    public List<Statistics> getWarningCount(@ModelAttribute IssueBase issueBase) throws BizException {

        // TODO: 2017/5/26 param check
//        ParamCheckUtil.paramCheck(issueBase);
        return integratedMonitorService.getWarningCount(issueBase);
    }


}
