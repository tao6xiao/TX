package com.trs.gov.kpi.controller;

import com.trs.gov.kpi.entity.exception.BizException;
import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.gov.kpi.entity.requestdata.WorkOrderRequest;
import com.trs.gov.kpi.entity.responsedata.ApiPageData;
import com.trs.gov.kpi.service.InfoUpdateService;
import com.trs.gov.kpi.utils.ParamCheckUtil;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Arrays;

/**
 * Created by ranwei on 2017/6/7.
 * 信息更新工单
 */

@RestController
@RequestMapping("/gov/kpi/opendata/issue")
public class WorkOrderController {

    @Resource
    private InfoUpdateService infoUpdateService;

    @RequestMapping(value = "/channel/update", method = RequestMethod.GET)
    public ApiPageData selectInfoUpdateOrder(@ModelAttribute WorkOrderRequest request) throws BizException, RemoteException {
        ParamCheckUtil.paramCheck(request);
        return infoUpdateService.selectInfoUpdateOrder(request);
    }


    @RequestMapping(value = "/workorder", method = RequestMethod.GET)
    public String updateOrderByIds(Integer workOrderStatus, Integer[] ids) throws BizException {
        if (workOrderStatus == null) {
            throw new BizException("参数不合法！");
        }
        infoUpdateService.updateOrderByIds(workOrderStatus, Arrays.asList(ids));
        return null;
    }
}
