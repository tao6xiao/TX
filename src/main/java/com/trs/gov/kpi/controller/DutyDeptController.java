package com.trs.gov.kpi.controller;

import com.trs.gov.kpi.constant.Constants;
import com.trs.gov.kpi.constant.OperationType;
import com.trs.gov.kpi.entity.exception.BizException;
import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.gov.kpi.entity.requestdata.DutyDeptRequest;
import com.trs.gov.kpi.entity.requestdata.PageDataRequestParam;
import com.trs.gov.kpi.entity.responsedata.ApiPageData;
import com.trs.gov.kpi.service.DutyDeptService;
import com.trs.gov.kpi.service.outer.SiteApiService;
import com.trs.gov.kpi.utils.ParamCheckUtil;
import com.trs.gov.kpi.utils.TRSLogUserUtil;
import com.trs.mlf.simplelog.SimpleLogServer;
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

    @Resource
    DutyDeptService deptService;

    @Resource
    SiteApiService siteApiService;

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
        if (deptService.getByChnlId(deptRequest.getChnlId()) == null) {
            deptService.add(deptRequest);
            SimpleLogServer.getInstance(TRSLogUserUtil.getLogUser()).operation(OperationType.ADD, "添加栏目和部门的关系", siteApiService.getSiteById(deptRequest.getSiteId(), "").getSiteName()).info();

        } else {
            deptService.update(deptRequest);
            SimpleLogServer.getInstance(TRSLogUserUtil.getLogUser()).operation(OperationType.UPDATE, "修改栏目和部门的关系", siteApiService.getSiteById(deptRequest.getSiteId(), "").getSiteName()).info();

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
        ParamCheckUtil.paramCheck(param);
        ApiPageData apiPageData = deptService.get(param);
        SimpleLogServer.getInstance(TRSLogUserUtil.getLogUser()).operation(OperationType.QUERY, "查询栏目和部门的关系", siteApiService.getSiteById(param.getSiteId(), "").getSiteName()).info();
        return apiPageData;
    }

    /**
     * 删除对于站点栏目下设置的部门
     * @param siteId
     * @param chnlIds
     * @return
     * @throws BizException
     */
    @RequestMapping(value = "/dept", method = RequestMethod.DELETE)
    @ResponseBody
    public String delete(Integer siteId, Integer[] chnlIds) throws BizException, RemoteException {
        if(siteId == null || chnlIds == null || chnlIds.length == 0){
            log.error("Invalid parameter: 参数siteId存在null值或者数组chnlIds为null");
            throw new BizException(Constants.INVALID_PARAMETER);
        }
        ParamCheckUtil.integerArrayParamCheck(chnlIds);
        deptService.delete(siteId, chnlIds);
        SimpleLogServer.getInstance(TRSLogUserUtil.getLogUser()).operation(OperationType.DELETE, "删除对于站点栏目下设置的部门", siteApiService.getSiteById(siteId, "").getSiteName()).info();
        return null;
    }
}
