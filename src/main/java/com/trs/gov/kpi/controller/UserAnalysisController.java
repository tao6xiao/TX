package com.trs.gov.kpi.controller;

import com.trs.gov.kpi.constant.Authority;
import com.trs.gov.kpi.entity.exception.BizException;
import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.gov.kpi.entity.requestdata.BasRequest;
import com.trs.gov.kpi.entity.responsedata.History;
import com.trs.gov.kpi.service.outer.AuthorityService;
import com.trs.gov.kpi.service.outer.BasService;
import com.trs.gov.kpi.utils.ParamCheckUtil;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.text.ParseException;

/**
 * Created by ranwei on 2017/6/13.
 */
@RestController
@RequestMapping(value = "/gov/kpi/analysis/user")
public class UserAnalysisController {

    @Resource
    private BasService basService;

    @Resource
    private AuthorityService authorityService;

    @RequestMapping(value = "/access", method = RequestMethod.GET)
    public Integer getVisits(@ModelAttribute BasRequest basRequest) throws BizException, RemoteException, ParseException {
        if (authorityService.hasRight(basRequest.getSiteId(), null, Authority.KPIWEB_ANALYSIS_VIEWS)) {
            throw new BizException(Authority.NO_AUTHORITY);
        }
        check(basRequest);
        return basService.getVisits(basRequest);
    }

    @RequestMapping(value = "/access/history", method = RequestMethod.GET)
    public History getHistoryVisits(@ModelAttribute BasRequest basRequest) throws BizException, RemoteException, ParseException {
        if (authorityService.hasRight(basRequest.getSiteId(), null, Authority.KPIWEB_ANALYSIS_VIEWS)) {
            throw new BizException(Authority.NO_AUTHORITY);
        }
        check(basRequest);
        return basService.getHistoryVisits(basRequest);
    }

    @RequestMapping(value = "/stay", method = RequestMethod.GET)
    public Integer getStayTime(@ModelAttribute BasRequest basRequest) throws BizException, RemoteException {
        if (authorityService.hasRight(basRequest.getSiteId(), null, Authority.KPIWEB_ANALYSIS_STAYTIME)) {
            throw new BizException(Authority.NO_AUTHORITY);
        }
        check(basRequest);
        return basService.getStayTime(basRequest);
    }

    @RequestMapping(value = "/stay/history", method = RequestMethod.GET)
    public History getHistoryStayTime(@ModelAttribute BasRequest basRequest) throws BizException, RemoteException, ParseException {
        if (authorityService.hasRight(basRequest.getSiteId(), null, Authority.KPIWEB_ANALYSIS_STAYTIME)) {
            throw new BizException(Authority.NO_AUTHORITY);
        }
        check(basRequest);
        return basService.getHistoryStayTime(basRequest);
    }

    private void check(BasRequest basRequest) throws BizException {
        ParamCheckUtil.checkCommonTime(basRequest.getBeginDateTime());
        ParamCheckUtil.checkCommonTime(basRequest.getEndDateTime());
    }
}
