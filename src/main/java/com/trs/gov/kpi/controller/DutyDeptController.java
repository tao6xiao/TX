package com.trs.gov.kpi.controller;

import com.trs.gov.kpi.constant.Authority;
import com.trs.gov.kpi.constant.Constants;
import com.trs.gov.kpi.constant.OperationType;
import com.trs.gov.kpi.entity.DutyDept;
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
import java.util.Date;

/**
 * 设置->为栏目设置部门
 * Created by he.lang on 2017/7/5.
 */
@Slf4j
@RestController
@RequestMapping("/gov/kpi/setting")
public class DutyDeptController {

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
        if (deptRequest.getSiteId() == null || deptRequest.getChnlId() == null || deptRequest.getDeptId() == null || deptRequest.getContain() == null) {
            log.error("Invalid parameter: 传入的参数siteId、chnlId、deptId、contain中至少有一个存在null值");
            throw new BizException(Constants.INVALID_PARAMETER);
        }
        authorityService.checkRight(Authority.KPIWEB_INDEXSETUP_DUTYDEPT, deptRequest.getSiteId());
        DutyDept dutyDept = null;
        try {
            dutyDept = deptService.getByChnlId(deptRequest.getChnlId());
        } catch (Exception e) {
            LogUtil.addOperationLog(OperationType.QUERY, LogUtil.buildFailOperationLogDesc("通过栏目id[" + deptRequest.getChnlId() + "]查询部门"), LogUtil.getSiteNameForLog(siteApiService,
                    deptRequest.getSiteId()));
            throw e;
        }
        if (dutyDept == null) {
            try {
                deptService.add(deptRequest);
                LogUtil.addOperationLog(OperationType.ADD, "添加栏目和部门的关系", LogUtil.getSiteNameForLog(siteApiService, deptRequest.getSiteId()));
            } catch (Exception e) {
                LogUtil.addOperationLog(OperationType.ADD, LogUtil.buildFailOperationLogDesc("添加栏目和部门的关系"), LogUtil.getSiteNameForLog(siteApiService, deptRequest.getSiteId()));
                throw e;
            }
        } else {
            try {
                deptService.update(deptRequest);
                LogUtil.addOperationLog(OperationType.UPDATE, "修改栏目和部门的关系", LogUtil.getSiteNameForLog(siteApiService, deptRequest.getSiteId()));
            } catch (Exception e) {
                LogUtil.addOperationLog(OperationType.UPDATE, LogUtil.buildFailOperationLogDesc("修改栏目和部门的关系"), LogUtil.getSiteNameForLog(siteApiService, deptRequest.getSiteId()));
                throw e;
            }
        }
        return null;
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
        Date startTime = new Date();
        ParamCheckUtil.paramCheck(param);
        String logDesc = "查询栏目和部门的关系";
        authorityService.checkRight(Authority.KPIWEB_INDEXSETUP_SEARCH, param.getSiteId());
        try {
            ApiPageData apiPageData = deptService.get(param);
            Date endTime = new Date();
            LogUtil.addOperationLog(OperationType.QUERY, logDesc, LogUtil.getSiteNameForLog(siteApiService, param.getSiteId()));
            LogUtil.addElapseLog(OperationType.QUERY, LogUtil.buildElapseLogDesc(siteApiService, param.getSiteId(), logDesc), endTime.getTime() - startTime.getTime());
            return apiPageData;
        } catch (Exception e) {
            LogUtil.addOperationLog(OperationType.QUERY, LogUtil.buildFailOperationLogDesc(logDesc), LogUtil.getSiteNameForLog(siteApiService, param.getSiteId()));
            throw e;
        }
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
        if (siteId == null || chnlIds == null || chnlIds.length == 0) {
            log.error("Invalid parameter: 参数siteId存在null值或者数组chnlIds为null");
            throw new BizException(Constants.INVALID_PARAMETER);
        }
        String logDesc = "删除对于站点栏目下设置的部门";
        authorityService.checkRight(Authority.KPIWEB_INDEXSETUP_DELDUTYDEPT, siteId);
        try {
            ParamCheckUtil.integerArrayParamCheck(chnlIds);
            deptService.delete(siteId, chnlIds);
            LogUtil.addOperationLog(OperationType.DELETE, logDesc, siteApiService.getSiteById(siteId, "").getSiteName());
            return null;
        } catch (Exception e) {
            LogUtil.addOperationLog(OperationType.DELETE, LogUtil.buildFailOperationLogDesc(logDesc), LogUtil.getSiteNameForLog(siteApiService, siteId));
            throw e;
        }
    }
}
