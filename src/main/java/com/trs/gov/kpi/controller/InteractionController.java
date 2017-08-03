package com.trs.gov.kpi.controller;

import com.trs.gov.kpi.entity.exception.BizException;
import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.gov.kpi.entity.outerapi.nbhd.NBHDPageDataResult;
import com.trs.gov.kpi.entity.outerapi.nbhd.NBHDRequestParam;
import com.trs.gov.kpi.entity.outerapi.nbhd.NBHDStatisticsRes;
import com.trs.gov.kpi.entity.responsedata.HistoryStatistics;
import com.trs.gov.kpi.service.outer.InteractionService;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by ranwei on 2017/6/9.
 */
@RestController
@RequestMapping(value = "/gov/kpi/interaction")
public class InteractionController {

    @Resource
    private InteractionService interactionService;

    /**
     * 查询问政互动的数量
     *
     * @param param
     * @return
     * @throws RemoteException
     */
    @RequestMapping(value = "/issue/bytype/count", method = RequestMethod.GET)
    public NBHDStatisticsRes getGovMsgBoxesCount(@ModelAttribute NBHDRequestParam param) throws RemoteException, BizException {
        NBHDStatisticsRes res = interactionService.getGovMsgBoxesCount(param);
//        SimpleLogServer.getInstance(TRSLogUserUtil.getLogUser()).operation(OperationType.QUERY, "查询问政互动的数量", "").info();
        return res;
    }

    /**
     * 查询问政互动的数量的历史记录
     *
     * @param param
     * @return
     * @throws RemoteException
     */
    @RequestMapping(value = "/issue/all/count/history", method = RequestMethod.GET)
    public List<HistoryStatistics> getGovMsgHistoryCount(@ModelAttribute NBHDRequestParam param) throws RemoteException, BizException {
        List<HistoryStatistics> historyStatisticsList = interactionService.getGovMsgHistoryCount(param);
//        SimpleLogServer.getInstance(TRSLogUserUtil.getLogUser()).operation(OperationType.QUERY, "查询问政互动的数量的历史记录", "").info();
        return historyStatisticsList;
    }

    /**
     * 查询问政互动的信件列表
     *
     * @param param
     * @return
     * @throws RemoteException
     */
    @RequestMapping(value = "/msg/unhandled", method = RequestMethod.GET)
    public NBHDPageDataResult getGovMsgBoxes(@ModelAttribute NBHDRequestParam param) throws RemoteException, BizException {
        NBHDPageDataResult result = interactionService.getGovMsgBoxes(param);
//        SimpleLogServer.getInstance(TRSLogUserUtil.getLogUser()).operation(OperationType.QUERY, "查询问政互动的信件列表", "").info();
        return result;
    }

}
