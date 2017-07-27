package com.trs.gov.kpi.controller;

import com.trs.gov.kpi.constant.OperationType;
import com.trs.gov.kpi.entity.exception.BizException;
import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.gov.kpi.entity.requestdata.WorkOrderRequest;
import com.trs.gov.kpi.entity.responsedata.ApiPageData;
import com.trs.gov.kpi.entity.responsedata.InfoErrorOrderRes;
import com.trs.gov.kpi.entity.responsedata.InfoUpdateOrderRes;
import com.trs.gov.kpi.service.InfoErrorService;
import com.trs.gov.kpi.service.InfoUpdateService;
import com.trs.gov.kpi.service.IssueService;
import com.trs.gov.kpi.utils.ParamCheckUtil;
import com.trs.gov.kpi.utils.TRSLogUserUtil;
import com.trs.mlf.simplelog.SimpleLogServer;
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

    @Resource
    private InfoErrorService infoErrorService;

    @Resource
    private IssueService issueService;

    /**
     * 查询更新监测数据列表
     * @param request
     * @return
     * @throws BizException
     * @throws RemoteException
     */
    @RequestMapping(value = "/channel/update", method = RequestMethod.GET)
    public ApiPageData selectInfoUpdateOrder(@ModelAttribute WorkOrderRequest request) throws BizException, RemoteException {
        ParamCheckUtil.paramCheck(request);
        ApiPageData apiPageData = infoUpdateService.selectInfoUpdateOrder(request);
        SimpleLogServer.getInstance(TRSLogUserUtil.getLogUser()).operation(OperationType.QUERY, "查询更新监测数据列表（为工单模块提供）", "").info();
        return apiPageData;
    }

    /**
     * 查询单条更新频率问题
     * @param request
     * @return
     * @throws BizException
     * @throws RemoteException
     */
    @RequestMapping(value = "/channel/update/single", method = RequestMethod.GET)
    public InfoUpdateOrderRes getInfoUpdateOrderById(@ModelAttribute WorkOrderRequest request) throws BizException, RemoteException {
        ParamCheckUtil.paramCheck(request);
        InfoUpdateOrderRes updateOrderRes = infoUpdateService.getInfoUpdateOrderById(request);
        SimpleLogServer.getInstance(TRSLogUserUtil.getLogUser()).operation(OperationType.QUERY, "查询单条更新频率问题（为工单模块提供）", "").info();
        return updateOrderRes;
    }

    /**
     * 查询敏感信息监测数据列表
     * @param request
     * @return
     * @throws BizException
     * @throws RemoteException
     */
    @RequestMapping(value = "/document/error", method = RequestMethod.GET)
    public ApiPageData selectInfoErrorOrder(@ModelAttribute WorkOrderRequest request) throws BizException, RemoteException {
        ParamCheckUtil.paramCheck(request);
        ApiPageData apiPageData = infoErrorService.selectInfoErrorOrder(request);
        SimpleLogServer.getInstance(TRSLogUserUtil.getLogUser()).operation(OperationType.QUERY, "查询敏感信息监测数据列表（为工单模块提供）", "").info();
        return apiPageData;
    }

    /**
     * 查询单条敏感信息监测问题
     * @param request
     * @return
     * @throws BizException
     * @throws RemoteException
     */
    @RequestMapping(value = "/document/error/single", method = RequestMethod.GET)
    public InfoErrorOrderRes getInfoErrorOrderById(@ModelAttribute WorkOrderRequest request) throws BizException, RemoteException {
        ParamCheckUtil.paramCheck(request);
        InfoErrorOrderRes errorOrderRes = infoErrorService.getInfoErrorOrderById(request);
        SimpleLogServer.getInstance(TRSLogUserUtil.getLogUser()).operation(OperationType.QUERY, "查询单条敏感信息监测问题（为工单模块提供）", "").info();

        return errorOrderRes;
    }

    /**
     * 修改问题工单处理状态
     * @param workOrderStatus
     * @param ids
     * @return
     * @throws BizException
     */
    @RequestMapping(value = "/workorder", method = RequestMethod.POST)
    public String updateOrderByIds(Integer workOrderStatus, Integer[] ids) throws BizException, RemoteException {
        if (workOrderStatus == null) {
            throw new BizException("参数不合法！");
        }
        issueService.updateOrderByIds(workOrderStatus, Arrays.asList(ids));
        SimpleLogServer.getInstance(TRSLogUserUtil.getLogUser()).operation(OperationType.UPDATE, "修改问题工单处理状态（为工单模块提供）", "").info();
        return null;
    }
}
