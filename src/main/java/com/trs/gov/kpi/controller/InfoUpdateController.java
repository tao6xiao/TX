package com.trs.gov.kpi.controller;

import com.trs.gov.kpi.constant.Constants;
import com.trs.gov.kpi.entity.exception.BizException;
import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.gov.kpi.entity.requestdata.PageDataRequestParam;
import com.trs.gov.kpi.entity.responsedata.ApiPageData;
import com.trs.gov.kpi.entity.responsedata.History;
import com.trs.gov.kpi.entity.responsedata.MonthUpdateResponse;
import com.trs.gov.kpi.entity.responsedata.Statistics;
import com.trs.gov.kpi.service.InfoUpdateService;
import com.trs.gov.kpi.utils.ParamCheckUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.List;

/**
 * 信息更新问题
 */
@Slf4j
@RestController
@RequestMapping("/gov/kpi/channel/issue")
public class InfoUpdateController extends IssueHandler {

    @Resource
    private InfoUpdateService infoUpdateService;

    /**
     * 查询已解决、预警和更新不及时的数量
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/bytype/count", method = RequestMethod.GET)
    public List getIssueCount(@ModelAttribute PageDataRequestParam param) throws BizException {
        ParamCheckUtil.paramCheck(param);
        return infoUpdateService.getIssueCount(param);
    }

    /**
     * 查询历史记录
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/all/count/history", method = RequestMethod.GET)
    public History getIssueHistoryCount(@ModelAttribute PageDataRequestParam param) throws BizException, ParseException {
        ParamCheckUtil.paramCheck(param);
        return infoUpdateService.getIssueHistoryCount(param);
    }

    /**
     * 查询待解决的问题列表
     *
     * @param param
     * @return
     * @throws BizException
     */
    @RequestMapping(value = "/unhandled", method = RequestMethod.GET)
    public ApiPageData getIssueList(@ModelAttribute PageDataRequestParam param) throws BizException, RemoteException {
        ParamCheckUtil.paramCheck(param);
        return infoUpdateService.get(param);
    }

    /**
     * 获取栏目信息更新不及时的统计信息
     *
     * @param param
     * @return
     * @throws BizException
     * @throws ParseException
     * @throws RemoteException
     */
    @RequestMapping(value = "/bygroup/count", method = RequestMethod.GET)
    @ResponseBody
    public List<Statistics> getUpdateNotInTimeCountList(@ModelAttribute PageDataRequestParam param) throws BizException, ParseException, RemoteException {
        ParamCheckUtil.paramCheck(param);
        return infoUpdateService.getUpdateNotInTimeCountList(param);
    }

    /**
     * 获取更新不及时栏目的更新不及时总月数以及空栏目
     *
     * @param siteId
     * @return
     * @throws BizException
     * @throws RemoteException
     */
    @RequestMapping(value = "/month/count", method = RequestMethod.GET)
    @ResponseBody
    public MonthUpdateResponse getNotInTimeCountMonth(@RequestParam("siteId") Integer siteId) throws BizException, RemoteException {
        if(siteId == null){
            log.error("Invalid parameter: 参数siteId为null值");
            throw new BizException(Constants.INVALID_PARAMETER);
        }
        return infoUpdateService.getNotInTimeCountMonth(siteId);
    }

}
