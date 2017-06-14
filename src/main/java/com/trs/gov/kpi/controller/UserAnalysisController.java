package com.trs.gov.kpi.controller;

import com.trs.gov.kpi.entity.exception.BizException;
import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.gov.kpi.entity.requestdata.BasRequest;
import com.trs.gov.kpi.entity.responsedata.HistoryStatistics;
import com.trs.gov.kpi.service.outer.BasService;
import com.trs.gov.kpi.utils.ParamCheckUtil;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.List;

/**
 * Created by ranwei on 2017/6/13.
 */
@RestController
@RequestMapping(value = "/gov/kpi/analysis/user")
public class UserAnalysisController {

    @Resource
    private BasService basService;

    @RequestMapping(value = "/access", method = RequestMethod.GET)
    public Integer getVisits(@ModelAttribute BasRequest basRequest) throws BizException, RemoteException, ParseException {
        check(basRequest);
        return basService.getVisits(basRequest);
    }

    @RequestMapping(value = "/access/history", method = RequestMethod.GET)
    public List<HistoryStatistics> getHistoryVisits(@ModelAttribute BasRequest basRequest) throws BizException, RemoteException, ParseException {
        check(basRequest);
        return basService.getHistoryVisits(basRequest);
    }

    @RequestMapping(value = "/stay", method = RequestMethod.GET)
    public Integer getStayTime(@ModelAttribute BasRequest basRequest) throws BizException, RemoteException, ParseException {
        check(basRequest);
        return basService.getStayTime(basRequest);
    }

    @RequestMapping(value = "/stay/history", method = RequestMethod.GET)
    public List<HistoryStatistics> geHistoryStayTime(@ModelAttribute BasRequest basRequest) throws BizException, RemoteException, ParseException {
        check(basRequest);
        return basService.geHistoryStayTime(basRequest);
    }

    private void check(BasRequest basRequest) throws BizException {
        ParamCheckUtil.checkTime(basRequest.getBeginDateTime());
        ParamCheckUtil.checkTime(basRequest.getEndDateTime());
    }
}
