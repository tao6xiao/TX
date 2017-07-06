package com.trs.gov.kpi.controller;

import com.trs.gov.kpi.constant.Constants;
import com.trs.gov.kpi.entity.exception.BizException;
import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.gov.kpi.entity.requestdata.DutyDeptRequest;
import com.trs.gov.kpi.entity.requestdata.PageDataRequestParam;
import com.trs.gov.kpi.entity.responsedata.ApiPageData;
import com.trs.gov.kpi.service.DutyDeptService;
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

    @Resource
    DutyDeptService deptService;

    /**
     * 设置部门或者修改栏目和部门关系
     *
     * @param deptRequest
     * @return
     * @throws BizException
     */
    @RequestMapping(value = "/dept", method = RequestMethod.POST)
    @ResponseBody
    public String set(@ModelAttribute DutyDeptRequest deptRequest) throws BizException {
        if (deptRequest.getSiteId() == null || deptRequest.getChnlId() == null || deptRequest.getDeptId() == null || deptRequest.getContain() == null) {
            log.error("Invalid parameter: 传入的参数siteId、chnlId、deptId、contain中至少有一个存在null值");
            throw new BizException(Constants.INVALID_PARAMETER);
        }
        if (deptService.getByChnlId(deptRequest.getChnlId()) == null) {
            deptService.add(deptRequest);
        } else {
            deptService.update(deptRequest);
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
        return deptService.get(param);
    }
}
