package com.trs.gov.kpi.config;

import com.trs.gov.kpi.constant.Authority;
import com.trs.gov.kpi.constant.UrlPath;
import com.trs.gov.kpi.entity.exception.BizException;
import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.gov.kpi.ids.ContextHelper;
import com.trs.gov.kpi.service.outer.AuthorityService;
import com.trs.gov.kpi.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by linwei on 2017/6/1.
 */
@Slf4j
public class AccessInterceptor extends HandlerInterceptorAdapter {

    private AuthorityService authorityService;

    public AccessInterceptor(AuthorityService authorityService) {
        this.authorityService = authorityService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        StringBuilder requestInfo = new StringBuilder();
        requestInfo.append("request url: ").append(request.getRequestURL()).append(", ");
        requestInfo.append(" method: ").append(request.getMethod()).append(", ");
        requestInfo.append(" Content-type: ").append(request.getContentType()).append(", ");
        requestInfo.append(" parameter: ");
        Map<String, String[]> params = request.getParameterMap();
        if (params != null && !params.isEmpty()) {
            Iterator<String> iter = params.keySet().iterator();
            while (iter.hasNext()) {
                String paramKey = iter.next();
                requestInfo.append(paramKey).append("=");

                String[] paramValues = params.get(paramKey);
                if (paramValues == null || paramValues.length == 0) {
                    requestInfo.append("NULL");
                } else {
                    requestInfo.append(Arrays.toString(paramValues));
                }
                requestInfo.append(" ");
            }
        }
        log.info(requestInfo.toString());

        checkAuthority(request);

        return super.preHandle(request, response, handler);
    }

    /**
     * 权限校验
     *
     * @param request
     * @throws RemoteException
     * @throws BizException
     */
    private void checkAuthority(HttpServletRequest request) throws RemoteException, BizException {
        Integer siteId = paramCheckAndParse(request);
        if (request.getRequestURL().indexOf(UrlPath.INTEGRATED_MONITOR_ISSUE_PATH + UrlPath.HANDLE_PATH) >= 0) {//待解决问题
            checkRight(Authority.KPIWEB_ISSUE_HANDLE, siteId);
        } else if (request.getRequestURL().indexOf(UrlPath.INTEGRATED_MONITOR_ISSUE_PATH + UrlPath.IGNORE_PATH) >= 0) {
            checkRight(Authority.KPIWEB_ISSUE_IGNORE, siteId);
        } else if (request.getRequestURL().indexOf(UrlPath.INTEGRATED_MONITOR_ISSUE_PATH + UrlPath.DELETE_PATH) >= 0) {
            checkRight(Authority.KPIWEB_ISSUE_DELETE, siteId);
        } else if (request.getRequestURL().indexOf(UrlPath.INTEGRATED_MONITOR_ISSUE_PATH + UrlPath.UPDATE_DEPT_PATH) >= 0) {
            checkRight(Authority.KPIWEB_ISSUE_UPDATEDEPT, siteId);
        } else if (request.getRequestURL().indexOf(UrlPath.INTEGRATED_MONITOR_WARNING_PATH + UrlPath.HANDLE_PATH) >= 0) {//待解决预警
            checkRight(Authority.KPIWEB_WARNING_HANDLE, siteId);
        } else if (request.getRequestURL().indexOf(UrlPath.INTEGRATED_MONITOR_WARNING_PATH + UrlPath.IGNORE_PATH) >= 0) {
            checkRight(Authority.KPIWEB_WARNING_IGNORE, siteId);
        } else if (request.getRequestURL().indexOf(UrlPath.INTEGRATED_MONITOR_WARNING_PATH + UrlPath.DELETE_PATH) >= 0) {
            checkRight(Authority.KPIWEB_WARNING_DELETE, siteId);
        } else if (request.getRequestURL().indexOf(UrlPath.INTEGRATED_MONITOR_WARNING_PATH + UrlPath.UPDATE_DEPT_PATH) >= 0) {
            checkRight(Authority.KPIWEB_WARNING_UPDATEDEPT, siteId);
        } else if (request.getRequestURL().indexOf(UrlPath.LINK_AVAILABILITY_PATH + UrlPath.HANDLE_PATH) >= 0) {//链接可用性
            checkRight(Authority.KPIWEB_AVAILABILITY_HANDLE, siteId);
        } else if (request.getRequestURL().indexOf(UrlPath.LINK_AVAILABILITY_PATH + UrlPath.IGNORE_PATH) >= 0) {
            checkRight(Authority.KPIWEB_AVAILABILITY_IGNORE, siteId);
        } else if (request.getRequestURL().indexOf(UrlPath.LINK_AVAILABILITY_PATH + UrlPath.DELETE_PATH) >= 0) {
            checkRight(Authority.KPIWEB_AVAILABILITY_DELETE, siteId);
        } else if (request.getRequestURL().indexOf(UrlPath.LINK_AVAILABILITY_PATH + UrlPath.UPDATE_DEPT_PATH) >= 0) {
            checkRight(Authority.KPIWEB_AVAILABILITY_UPDATEDEPT, siteId);
        } else if (request.getRequestURL().indexOf(UrlPath.INFO_ERROR_PATH + UrlPath.HANDLE_PATH) >= 0) {//信息错误
            checkRight(Authority.KPIWEB_INFOERROR_HANDLE, siteId);
        } else if (request.getRequestURL().indexOf(UrlPath.INFO_ERROR_PATH + UrlPath.IGNORE_PATH) >= 0) {
            checkRight(Authority.KPIWEB_INFOERROR_IGNORE, siteId);
        } else if (request.getRequestURL().indexOf(UrlPath.INFO_ERROR_PATH + UrlPath.DELETE_PATH) >= 0) {
            checkRight(Authority.KPIWEB_INFOERROR_DELETE, siteId);
        } else if (request.getRequestURL().indexOf(UrlPath.INFO_ERROR_PATH + UrlPath.UPDATE_DEPT_PATH) >= 0) {
            checkRight(Authority.KPIWEB_INFOERROR_UPDATEDEPT, siteId);
        } else if (request.getRequestURL().indexOf(UrlPath.INFO_UPDATE_PATH + UrlPath.HANDLE_PATH) >= 0) {//信息更新
            checkRight(Authority.KPIWEB_INFOUPDATE_HANDLE, siteId);
        } else if (request.getRequestURL().indexOf(UrlPath.INFO_UPDATE_PATH + UrlPath.IGNORE_PATH) >= 0) {
            checkRight(Authority.KPIWEB_INFOUPDATE_IGNORE, siteId);
        } else if (request.getRequestURL().indexOf(UrlPath.INFO_UPDATE_PATH + UrlPath.DELETE_PATH) >= 0) {
            checkRight(Authority.KPIWEB_INFOUPDATE_DELETE, siteId);
        } else if (request.getRequestURL().indexOf(UrlPath.INFO_UPDATE_PATH + UrlPath.UPDATE_DEPT_PATH) >= 0) {
            checkRight(Authority.KPIWEB_INFOUPDATE_UPDATEDEPT, siteId);
        } else if (request.getRequestURL().indexOf(UrlPath.SERVICE_LINK_PATH + UrlPath.HANDLE_PATH) >= 0) {//服务实用
            checkRight(Authority.KPIWEB_SERVICE_HANDLE, siteId);
        } else if (request.getRequestURL().indexOf(UrlPath.SERVICE_LINK_PATH + UrlPath.IGNORE_PATH) >= 0) {
            checkRight(Authority.KPIWEB_SERVICE_IGNORE, siteId);
        } else if (request.getRequestURL().indexOf(UrlPath.SERVICE_LINK_PATH + UrlPath.DELETE_PATH) >= 0) {
            checkRight(Authority.KPIWEB_SERVICE_DELETE, siteId);
        } else if (request.getRequestURL().indexOf(UrlPath.SERVICE_LINK_PATH + UrlPath.UPDATE_DEPT_PATH) >= 0) {
            checkRight(Authority.KPIWEB_SERVICE_UPDATEDEPT, siteId);
        }
    }

    private void checkRight(String right, Integer siteId) throws RemoteException, BizException {
        if (!authorityService.hasRight(ContextHelper.getLoginUser().getUserName(), siteId, null, right) && !authorityService.hasRight(ContextHelper
                .getLoginUser().getUserName(), null, null, right)) {
            throw new BizException(Authority.NO_AUTHORITY);
        }
    }


    private Integer paramCheckAndParse(HttpServletRequest request) {
        String siteIdStr = request.getParameter("siteId");
        if (StringUtil.isEmpty(siteIdStr)) {
            throw new IllegalArgumentException();
        }
        return Integer.valueOf(siteIdStr);
    }


}
