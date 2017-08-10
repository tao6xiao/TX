package com.trs.gov.kpi.controller;

import com.trs.gov.kpi.constant.Authority;
import com.trs.gov.kpi.constant.Constants;
import com.trs.gov.kpi.constant.OperationType;
import com.trs.gov.kpi.entity.exception.BizException;
import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.gov.kpi.entity.requestdata.DutyDeptRequest;
import com.trs.gov.kpi.entity.requestdata.PageDataRequestParam;
import com.trs.gov.kpi.entity.responsedata.ApiPageData;
import com.trs.gov.kpi.service.DutyDeptService;
import com.trs.gov.kpi.service.outer.AuthorityService;
import com.trs.gov.kpi.service.outer.SiteApiService;
import com.trs.gov.kpi.utils.LogUtil;
import com.trs.gov.kpi.utils.ParamCheckUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 设置->为栏目设置部门
 * Created by he.lang on 2017/7/5.
 */
@Slf4j
@RestController
@RequestMapping("/gov/kpi/setting")
public class DutyDeptController {

    public static final String DEPT_REQUEST = "deptRequest";
    @Resource
    DutyDeptService deptService;

    @Resource
    SiteApiService siteApiService;

    @Resource
    private AuthorityService authorityService;

    /**
     * 设置部门或者修改栏目和部门关系
     *
     * @param deptRequest
     * @return
     * @throws BizException
     */
    @RequestMapping(value = "/dept", method = RequestMethod.POST)
    @ResponseBody
    public String set(@ModelAttribute DutyDeptRequest deptRequest) throws BizException, RemoteException {
        String logDesc = "设置栏目和部门的关系" + LogUtil.paramsToLogString(DEPT_REQUEST, deptRequest);
        return LogUtil.ControlleFunctionWrapper(() -> {
            if (deptRequest.getSiteId() == null || deptRequest.getChnlId() == null || deptRequest.getDeptId() == null || deptRequest.getContain() == null) {
                log.error("Invalid parameter: 传入的参数siteId、chnlId、deptId、contain中至少有一个存在null值");
                throw new BizException(Constants.INVALID_PARAMETER);
            }
            authorityService.checkRight(Authority.KPIWEB_INDEXSETUP_DUTYDEPT, deptRequest.getSiteId());

            if (deptService.getByChnlId(deptRequest.getChnlId()) == null) {
                add(deptRequest);
            } else {
                update(deptRequest);
            }
            return null;
        }, OperationType.ADD + "," + OperationType.UPDATE, logDesc, LogUtil.getSiteNameForLog(siteApiService, deptRequest.getSiteId()));
    }

    private Integer update(DutyDeptRequest deptRequest) throws RemoteException, BizException {
        String logDesc = "修改栏目和部门的关系" + LogUtil.paramsToLogString(DEPT_REQUEST, deptRequest);
        return LogUtil.ControlleFunctionWrapper(() -> {
            deptService.update(deptRequest);
            return null;
        }, OperationType.UPDATE, logDesc, LogUtil.getSiteNameForLog(siteApiService, deptRequest.getSiteId()));
    }

    private Integer add(DutyDeptRequest deptRequest) throws RemoteException, BizException {
        String logDesc = "添加栏目和部门的关系" + LogUtil.paramsToLogString(DEPT_REQUEST, deptRequest);
        return LogUtil.ControlleFunctionWrapper(() -> {
            deptService.add(deptRequest);
            return null;
        }, OperationType.ADD, logDesc, LogUtil.getSiteNameForLog(siteApiService, deptRequest.getSiteId()));
    }

    /**
     * 查询对应记录（模糊查询）
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/dept", method = RequestMethod.GET)
    @ResponseBody
    public ApiPageData get(@ModelAttribute PageDataRequestParam param) throws BizException, RemoteException {
        String logDesc = "查询栏目和部门的关系" + LogUtil.paramsToLogString("param", param);
        return LogUtil.ControlleFunctionWrapper(() -> {
            ParamCheckUtil.paramCheck(param);
            authorityService.checkRight(Authority.KPIWEB_INDEXSETUP_SEARCH, param.getSiteId());
            return deptService.get(param);
        }, OperationType.QUERY, logDesc, LogUtil.getSiteNameForLog(siteApiService, param.getSiteId()));
    }

    /**
     * 删除对于站点栏目下设置的部门
     *
     * @param siteId
     * @param chnlIds
     * @return
     * @throws BizException
     */
    @RequestMapping(value = "/dept", method = RequestMethod.DELETE)
    @ResponseBody
    public String delete(Integer siteId, Integer[] chnlIds) throws BizException, RemoteException {
        String logDesc = "删除对于站点栏目下设置的部门" + LogUtil.paramsToLogString(Constants.DB_FIELD_SITE_ID, siteId, "chnlIds", chnlIds);
        return LogUtil.ControlleFunctionWrapper(() -> {
            if (siteId == null || chnlIds == null || chnlIds.length == 0) {
                log.error("Invalid parameter: 参数siteId存在null值或者数组chnlIds为null");
                throw new BizException(Constants.INVALID_PARAMETER);
            }
            authorityService.checkRight(Authority.KPIWEB_INDEXSETUP_DELDUTYDEPT, siteId);
            ParamCheckUtil.integerArrayParamCheck(chnlIds);
            deptService.delete(siteId, chnlIds);
            return null;
        }, OperationType.DELETE, logDesc, LogUtil.getSiteNameForLog(siteApiService, siteId));
    }
}
