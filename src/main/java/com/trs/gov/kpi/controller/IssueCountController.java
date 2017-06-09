package com.trs.gov.kpi.controller;

import com.trs.gov.kpi.entity.exception.BizException;
import com.trs.gov.kpi.entity.requestdata.IssueCountRequest;
import com.trs.gov.kpi.entity.responsedata.History;
import com.trs.gov.kpi.entity.responsedata.Statistics;
import com.trs.gov.kpi.service.IssueCountService;
import com.trs.gov.kpi.utils.ParamCheckUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 统计分析->问题统计controller
 * Created by he.lang on 2017/6/7.
 */
@Slf4j
@RestController
@RequestMapping(value = "/gov/kpi/analysis/issue")
public class IssueCountController {

    @Resource
    IssueCountService countService;

    /**
     * 分类查询问题数量统计
     * @param request
     * @return
     * @throws BizException
     */
    @RequestMapping(value = "/count",method = RequestMethod.GET)
    @ResponseBody
    public List<Statistics> countSort(@ModelAttribute IssueCountRequest request) throws BizException {
        ParamCheckUtil.paramCheck(request);
        return countService.countSort(request);
    }

    /**
     * 分类查询统计历史数量
     * @param request
     * @return
     */
    @RequestMapping(value = "/count/history",method = RequestMethod.GET)
    @ResponseBody
    public History historyCountSort(@ModelAttribute IssueCountRequest request) throws BizException {
        ParamCheckUtil.paramCheck(request);
        return countService.historyCountSort(request);
    }
}
