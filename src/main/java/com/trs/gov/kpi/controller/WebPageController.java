package com.trs.gov.kpi.controller;

import com.trs.gov.kpi.constant.Authority;
import com.trs.gov.kpi.entity.exception.BizException;
import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.gov.kpi.entity.requestdata.PageDataRequestParam;
import com.trs.gov.kpi.entity.responsedata.ApiPageData;
import com.trs.gov.kpi.service.WebPageService;
import com.trs.gov.kpi.service.outer.AuthorityService;
import com.trs.gov.kpi.utils.ParamCheckUtil;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Arrays;

/**
 * Created by ranwei on 2017/6/6.
 */
@RestController
@RequestMapping("/gov/kpi/analysis/improve")
public class WebPageController {

    @Resource
    private WebPageService webPageService;

    @Resource
    private AuthorityService authorityService;

    @RequestMapping(value = "/speed/count", method = RequestMethod.GET)
    public int selectReplySpeedCount(@ModelAttribute PageDataRequestParam param) throws BizException, RemoteException {
        if (authorityService.hasRight(param.getSiteId(), null, Authority.KPIWEB_IMPROVE_SEARCH)) {
            throw new BizException(Authority.NO_AUTHORITY);
        }
        ParamCheckUtil.paramCheck(param);
        return webPageService.selectReplySpeedCount(param);
    }

    @RequestMapping(value = "/speed", method = RequestMethod.GET)
    public ApiPageData selectReplySpeed(@ModelAttribute PageDataRequestParam param) throws BizException, RemoteException {
        if (authorityService.hasRight(param.getSiteId(), null, Authority.KPIWEB_IMPROVE_SEARCH)) {
            throw new BizException(Authority.NO_AUTHORITY);
        }
        ParamCheckUtil.paramCheck(param);
        return webPageService.selectReplySpeed(param);
    }

    @RequestMapping(value = "/size/count", method = RequestMethod.GET)
    public int selectPageSpaceCount(@ModelAttribute PageDataRequestParam param) throws BizException, RemoteException {
        if (authorityService.hasRight(param.getSiteId(), null, Authority.KPIWEB_IMPROVE_SEARCH)) {
            throw new BizException(Authority.NO_AUTHORITY);
        }
        ParamCheckUtil.paramCheck(param);
        return webPageService.selectPageSpaceCount(param);
    }

    @RequestMapping(value = "/size", method = RequestMethod.GET)
    public ApiPageData selectPageSpace(@ModelAttribute PageDataRequestParam param) throws BizException, RemoteException {
        if (authorityService.hasRight(param.getSiteId(), null, Authority.KPIWEB_IMPROVE_SEARCH)) {
            throw new BizException(Authority.NO_AUTHORITY);
        }
        ParamCheckUtil.paramCheck(param);
        return webPageService.selectPageSpace(param);
    }

    @RequestMapping(value = "/depth/count", method = RequestMethod.GET)
    public int selectPageDepthCount(@ModelAttribute PageDataRequestParam param) throws BizException, RemoteException {
        if (authorityService.hasRight(param.getSiteId(), null, Authority.KPIWEB_IMPROVE_SEARCH)) {
            throw new BizException(Authority.NO_AUTHORITY);
        }
        ParamCheckUtil.paramCheck(param);
        return webPageService.selectPageDepthCount(param);
    }

    @RequestMapping(value = "/depth", method = RequestMethod.GET)
    public ApiPageData selectPageDepth(@ModelAttribute PageDataRequestParam param) throws BizException, RemoteException {
        if (authorityService.hasRight(param.getSiteId(), null, Authority.KPIWEB_IMPROVE_SEARCH)) {
            throw new BizException(Authority.NO_AUTHORITY);
        }
        ParamCheckUtil.paramCheck(param);
        return webPageService.selectPageDepth(param);
    }

    @RequestMapping(value = "/code/count", method = RequestMethod.GET)
    public int selectRepeatCodeCount(@ModelAttribute PageDataRequestParam param) throws BizException, RemoteException {
        if (authorityService.hasRight(param.getSiteId(), null, Authority.KPIWEB_IMPROVE_SEARCH)) {
            throw new BizException(Authority.NO_AUTHORITY);
        }
        ParamCheckUtil.paramCheck(param);
        return webPageService.selectRepeatCodeCount(param);
    }

    @RequestMapping(value = "/code", method = RequestMethod.GET)
    public ApiPageData selectRepeatCode(@ModelAttribute PageDataRequestParam param) throws BizException, RemoteException {
        if (authorityService.hasRight(param.getSiteId(), null, Authority.KPIWEB_IMPROVE_SEARCH)) {
            throw new BizException(Authority.NO_AUTHORITY);
        }
        ParamCheckUtil.paramCheck(param);
        return webPageService.selectRepeatCode(param);
    }

    @RequestMapping(value = "/length/count", method = RequestMethod.GET)
    public int selectUrlLengthCount(@ModelAttribute PageDataRequestParam param) throws BizException, RemoteException {
        if (authorityService.hasRight(param.getSiteId(), null, Authority.KPIWEB_IMPROVE_SEARCH)) {
            throw new BizException(Authority.NO_AUTHORITY);
        }
        ParamCheckUtil.paramCheck(param);
        return webPageService.selectUrlLengthCount(param);
    }

    @RequestMapping(value = "/length", method = RequestMethod.GET)
    public ApiPageData selectUrlLength(@ModelAttribute PageDataRequestParam param) throws BizException, RemoteException {
        if (authorityService.hasRight(param.getSiteId(), null, Authority.KPIWEB_IMPROVE_SEARCH)) {
            throw new BizException(Authority.NO_AUTHORITY);
        }
        ParamCheckUtil.paramCheck(param);
        return webPageService.selectUrlLength(param);
    }

    @RequestMapping(value = "/handle", method = RequestMethod.POST)
    public String handlePageByIds(int siteId, Integer[] ids) throws BizException, RemoteException {
        if (authorityService.hasRight(siteId, null, Authority.KPIWEB_IMPROVE_HANDLE)) {
            throw new BizException(Authority.NO_AUTHORITY);
        }
        webPageService.handlePageByIds(siteId, Arrays.asList(ids));
        return null;
    }

    @RequestMapping(value = "/ignore", method = RequestMethod.POST)
    public String ignorePageByIds(int siteId, Integer[] ids) throws RemoteException, BizException {
        if (authorityService.hasRight(siteId, null, Authority.KPIWEB_IMPROVE_IGNORE)) {
            throw new BizException(Authority.NO_AUTHORITY);
        }
        webPageService.ignorePageByIds(siteId, Arrays.asList(ids));
        return null;
    }

    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public String delPageByIds(int siteId, Integer[] ids) throws RemoteException, BizException {
        if (authorityService.hasRight(siteId, null, Authority.KPIWEB_IMPROVE_DELETE)) {
            throw new BizException(Authority.NO_AUTHORITY);
        }
        webPageService.delPageByIds(siteId, Arrays.asList(ids));
        return null;
    }
}
