package com.trs.gov.kpi.controller;

import com.trs.gov.kpi.constant.Authority;
import com.trs.gov.kpi.constant.Constants;
import com.trs.gov.kpi.constant.OperationType;
import com.trs.gov.kpi.constant.UrlPath;
import com.trs.gov.kpi.entity.exception.BizException;
import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.gov.kpi.entity.outerapi.Site;
import com.trs.gov.kpi.entity.requestdata.PageDataRequestParam;
import com.trs.gov.kpi.entity.responsedata.ApiPageData;
import com.trs.gov.kpi.entity.responsedata.History;
import com.trs.gov.kpi.entity.responsedata.MonthUpdateResponse;
import com.trs.gov.kpi.entity.responsedata.Statistics;
import com.trs.gov.kpi.ids.ContextHelper;
import com.trs.gov.kpi.service.InfoUpdateService;
import com.trs.gov.kpi.service.outer.AuthorityService;
import com.trs.gov.kpi.service.outer.SiteApiService;
import com.trs.gov.kpi.utils.LogUtil;
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
@RequestMapping(UrlPath.INFO_UPDATE_PATH)
public class InfoUpdateController extends IssueHandler {

    @Resource
    private InfoUpdateService infoUpdateService;

    @Resource
    private AuthorityService authorityService;

    @Resource
    SiteApiService siteApiService;

    /**
     * 查询已解决、预警和更新不及时的数量
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/bytype/count", method = RequestMethod.GET)
    public List getIssueCount(@ModelAttribute PageDataRequestParam param) throws BizException, RemoteException {
        if (!authorityService.hasRight(ContextHelper.getLoginUser().getUserName(), param.getSiteId(), null, Authority.KPIWEB_INFOUPDATE_SEARCH) && !authorityService.hasRight(ContextHelper
                .getLoginUser().getUserName(), null, null, Authority.KPIWEB_INFOUPDATE_SEARCH)) {
            // TODO 添加操作失败的操作日志
            throw new BizException(Authority.NO_AUTHORITY);
        }
        ParamCheckUtil.paramCheck(param);

        try {
            List list = infoUpdateService.getIssueCount(param);
            LogUtil.addOperationLog(OperationType.QUERY, "查询信息更新已解决、预警和更新不及时的数量", LogUtil.getSiteNameForLog(siteApiService, param.getSiteId()));
            return list;
        } catch ( Throwable ex) {
            // TODO 添加操作失败的操作日志
            throw ex;
        }
    }

    /**
     * 查询历史记录
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/all/count/history", method = RequestMethod.GET)
    public History getIssueHistoryCount(@ModelAttribute PageDataRequestParam param) throws BizException, RemoteException {
        if (!authorityService.hasRight(ContextHelper.getLoginUser().getUserName(), param.getSiteId(), null, Authority.KPIWEB_INFOUPDATE_SEARCH) && !authorityService.hasRight(ContextHelper
                .getLoginUser().getUserName(), null, null, Authority.KPIWEB_INFOUPDATE_SEARCH)) {
            throw new BizException(Authority.NO_AUTHORITY);
        }
        ParamCheckUtil.paramCheck(param);
        History history = infoUpdateService.getIssueHistoryCount(param);
        LogUtil.addOperationLog(OperationType.QUERY, "查询信息更新历史纪录", siteApiService.getSiteById(param.getSiteId(), "").getSiteName());
        return history;
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
        if (!authorityService.hasRight(ContextHelper.getLoginUser().getUserName(), param.getSiteId(), null, Authority.KPIWEB_INFOUPDATE_SEARCH) && !authorityService.hasRight(ContextHelper
                .getLoginUser().getUserName(), null, null, Authority.KPIWEB_INFOUPDATE_SEARCH)) {
            throw new BizException(Authority.NO_AUTHORITY);
        }
        ParamCheckUtil.paramCheck(param);
        ApiPageData apiPageData = infoUpdateService.get(param);
        LogUtil.addOperationLog(OperationType.QUERY, "查询信息更新待解决问题列表", siteApiService.getSiteById(param.getSiteId(), "").getSiteName());
        return apiPageData;
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
        if (!authorityService.hasRight(ContextHelper.getLoginUser().getUserName(), param.getSiteId(), null, Authority.KPIWEB_INFOUPDATE_SEARCH) && !authorityService.hasRight(ContextHelper
                .getLoginUser().getUserName(), null, null, Authority.KPIWEB_INFOUPDATE_SEARCH)) {
            throw new BizException(Authority.NO_AUTHORITY);
        }
        ParamCheckUtil.paramCheck(param);
        List<Statistics> list = infoUpdateService.getUpdateNotInTimeCountList(param);
        LogUtil.addOperationLog(OperationType.QUERY, "获取栏目信息更新不及时的统计信息", siteApiService.getSiteById(param.getSiteId(), "").getSiteName());
        return list;
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
        if (!authorityService.hasRight(ContextHelper.getLoginUser().getUserName(), siteId, null, Authority.KPIWEB_INFOUPDATE_SEARCH) && !authorityService.hasRight(ContextHelper
                .getLoginUser().getUserName(), null, null, Authority.KPIWEB_INFOUPDATE_SEARCH)) {
            throw new BizException(Authority.NO_AUTHORITY);
        }
        if (siteId == null) {
            log.error("Invalid parameter: 参数siteId为null值");
            throw new BizException(Constants.INVALID_PARAMETER);
        }
        MonthUpdateResponse response = infoUpdateService.getNotInTimeCountMonth(siteId);
        LogUtil.addOperationLog(OperationType.QUERY, "获取更新不及时栏目的更新不及时总月数以及空栏目", siteApiService.getSiteById(siteId, "").getSiteName());
        return response;
    }

}
