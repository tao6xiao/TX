package com.trs.gov.kpi.controller;

import com.trs.gov.kpi.constant.Constants;
import com.trs.gov.kpi.entity.exception.BizException;
import com.trs.gov.kpi.entity.requestdata.IssueCountByTypeRequest;
import com.trs.gov.kpi.entity.requestdata.IssueCountRequest;
import com.trs.gov.kpi.entity.responsedata.*;
import com.trs.gov.kpi.service.IssueCountService;
import com.trs.gov.kpi.utils.ParamCheckUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.text.ParseException;
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
    public History historyCountSort(@ModelAttribute IssueCountRequest request) throws BizException, ParseException {
        ParamCheckUtil.paramCheck(request);
        return countService.historyCountSort(request);
    }

    /**
     * 部门分类查询统计数量
     * @param request
     * @return
     */
    @RequestMapping(value = "/bydept/count",method = RequestMethod.GET)
    @ResponseBody
    public List<DeptCountResponse> deptCountSort(@ModelAttribute IssueCountRequest request) throws BizException {
        ParamCheckUtil.paramCheck(request);
        return countService.deptCountSort(request);
    }

    /**
     * 更具问题类型部门分类查询统计数量
     * @param request
     * @return
     */
    @RequestMapping(value = "/unhandled/count",method = RequestMethod.GET)
    @ResponseBody
    public List<DeptCount> getDeptIssueCountByType(@ModelAttribute IssueCountByTypeRequest request) throws BizException {
        ParamCheckUtil.paramCheck(request);
        if (request.getTypeId() > 5 || request.getTypeId() < 1) {
            throw new BizException(Constants.INVALID_PARAMETER);
        }

        return countService.getDeptCountByType(request);
    }
	
	
    /**
     * 部门分类归纳查询统计数量
     * @param request
     * @return
     */
    @RequestMapping(value = "/bytype/count",method = RequestMethod.GET)
    @ResponseBody
    public DeptInductionResponse[] deptInductionSort(@ModelAttribute IssueCountRequest request) throws BizException {
        ParamCheckUtil.paramCheck(request);
        return countService.deptInductionSort(request);
    }
}
