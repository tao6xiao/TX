package com.trs.gov.kpi.config;

import com.trs.gov.kpi.constant.Authority;
import com.trs.gov.kpi.entity.exception.BizException;
import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.gov.kpi.service.outer.AuthorityService;
import com.trs.gov.kpi.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by linwei on 2017/6/1.
 */
@Slf4j
public class AccessInterceptor extends HandlerInterceptorAdapter {

    private AuthorityService authorityService;

    private Map<String, String> urlPathMap;

    public AccessInterceptor(AuthorityService authorityService) {
        this.authorityService = authorityService;
        urlPathMap = getUrlPathMap();
    }

    private Map<String, String> getUrlPathMap() {
        Map<String, String> map = new HashMap<>();
        map.put(Authority.KPIWEB_ISSUE_HANDLE, "/gov/kpi/issue/handle");
        map.put(Authority.KPIWEB_ISSUE_DELETE, "/gov/kpi/issue/delete");
        map.put(Authority.KPIWEB_ISSUE_IGNORE, "/gov/kpi/issue/ignore");
        map.put(Authority.KPIWEB_ISSUE_UPDATEDEPT, "/gov/kpi/issue/updatedept");

        map.put(Authority.KPIWEB_WARNING_HANDLE, "/gov/kpi/alert/handle");
        map.put(Authority.KPIWEB_WARNING_DELETE, "/gov/kpi/alert/delete");
        map.put(Authority.KPIWEB_WARNING_IGNORE, "/gov/kpi/alert/ignore");
        map.put(Authority.KPIWEB_WARNING_UPDATEDEPT, "/gov/kpi/alert/updatedept");

        map.put(Authority.KPIWEB_SERVICE_HANDLE, "/gov/kpi/service/link/issue/handle");
        map.put(Authority.KPIWEB_SERVICE_DELETE, "/gov/kpi/service/link/issue/delete");
        map.put(Authority.KPIWEB_SERVICE_IGNORE, "/gov/kpi/service/link/issue/ignore");
        map.put(Authority.KPIWEB_SERVICE_UPDATEDEPT, "/gov/kpi/service/link/issue/updatedept");

        map.put(Authority.KPIWEB_AVAILABILITY_HANDLE, "/gov/kpi/available/issue/handle");
        map.put(Authority.KPIWEB_AVAILABILITY_DELETE, "/gov/kpi/available/issue/delete");
        map.put(Authority.KPIWEB_AVAILABILITY_IGNORE, "/gov/kpi/available/issue/ignore");
        map.put(Authority.KPIWEB_AVAILABILITY_UPDATEDEPT, "/gov/kpi/available/issue/updatedept");

        map.put(Authority.KPIWEB_INFOERROR_HANDLE, "/gov/kpi/content/issue/handle");
        map.put(Authority.KPIWEB_INFOERROR_DELETE, "/gov/kpi/content/issue/delete");
        map.put(Authority.KPIWEB_INFOERROR_IGNORE, "/gov/kpi/content/issue/ignore");
        map.put(Authority.KPIWEB_INFOERROR_UPDATEDEPT, "/gov/kpi/content/issue/updatedept");

        map.put(Authority.KPIWEB_INFOUPDATE_HANDLE, "/gov/kpi/channel/issue/handle");
        map.put(Authority.KPIWEB_INFOUPDATE_DELETE, "/gov/kpi/channel/issue/delete");
        map.put(Authority.KPIWEB_INFOUPDATE_IGNORE, "/gov/kpi/channel/issue/ignore");
        map.put(Authority.KPIWEB_INFOUPDATE_UPDATEDEPT, "/gov/kpi/channel/issue/updatedept");
        return map;
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
        if (request.getRequestURI().endsWith("/handle") || request.getRequestURI().endsWith("/delete") || request.getRequestURI().endsWith("/ignore") ||
                request.getRequestURI().endsWith("/updatedept")) {

            Integer siteId = paramCheckAndParse(request);
            checkIssueRight(request, siteId);
            checkWarningRight(request, siteId);
            checkAvailabilityRight(request, siteId);
            checkInfoerrorRight(request, siteId);
            checkInfoupdateRight(request, siteId);
            checkServiceRight(request, siteId);
        }
    }


    private void checkIssueRight(HttpServletRequest request, Integer siteId) throws RemoteException, BizException {
        if (request.getRequestURL().indexOf(urlPathMap.get(Authority.KPIWEB_ISSUE_HANDLE)) >= 0) {//待解决问题
            authorityService.checkRight(Authority.KPIWEB_ISSUE_HANDLE, siteId);
        } else if (request.getRequestURL().indexOf(urlPathMap.get(Authority.KPIWEB_ISSUE_IGNORE)) >= 0) {
            authorityService.checkRight(Authority.KPIWEB_ISSUE_IGNORE, siteId);
        } else if (request.getRequestURL().indexOf(urlPathMap.get(Authority.KPIWEB_ISSUE_DELETE)) >= 0) {
            authorityService.checkRight(Authority.KPIWEB_ISSUE_DELETE, siteId);
        } else if (request.getRequestURL().indexOf(urlPathMap.get(Authority.KPIWEB_ISSUE_UPDATEDEPT)) >= 0) {
            authorityService.checkRight(Authority.KPIWEB_ISSUE_UPDATEDEPT, siteId);
        }
    }

    private void checkWarningRight(HttpServletRequest request, Integer siteId) throws RemoteException, BizException {
        if (request.getRequestURL().indexOf(urlPathMap.get(Authority.KPIWEB_WARNING_HANDLE)) >= 0) {//待解决预警
            authorityService.checkRight(Authority.KPIWEB_WARNING_HANDLE, siteId);
        } else if (request.getRequestURL().indexOf(urlPathMap.get(Authority.KPIWEB_WARNING_IGNORE)) >= 0) {
            authorityService.checkRight(Authority.KPIWEB_WARNING_IGNORE, siteId);
        } else if (request.getRequestURL().indexOf(urlPathMap.get(Authority.KPIWEB_WARNING_DELETE)) >= 0) {
            authorityService.checkRight(Authority.KPIWEB_WARNING_DELETE, siteId);
        } else if (request.getRequestURL().indexOf(urlPathMap.get(Authority.KPIWEB_WARNING_UPDATEDEPT)) >= 0) {
            authorityService.checkRight(Authority.KPIWEB_WARNING_UPDATEDEPT, siteId);
        }
    }

    private void checkAvailabilityRight(HttpServletRequest request, Integer siteId) throws RemoteException, BizException {
        if (request.getRequestURL().indexOf(urlPathMap.get(Authority.KPIWEB_AVAILABILITY_HANDLE)) >= 0) {//链接可用性
            authorityService.checkRight(Authority.KPIWEB_AVAILABILITY_HANDLE, siteId);
        } else if (request.getRequestURL().indexOf(urlPathMap.get(Authority.KPIWEB_AVAILABILITY_IGNORE)) >= 0) {
            authorityService.checkRight(Authority.KPIWEB_AVAILABILITY_IGNORE, siteId);
        } else if (request.getRequestURL().indexOf(urlPathMap.get(Authority.KPIWEB_AVAILABILITY_DELETE)) >= 0) {
            authorityService.checkRight(Authority.KPIWEB_AVAILABILITY_DELETE, siteId);
        } else if (request.getRequestURL().indexOf(urlPathMap.get(Authority.KPIWEB_AVAILABILITY_UPDATEDEPT)) >= 0) {
            authorityService.checkRight(Authority.KPIWEB_AVAILABILITY_UPDATEDEPT, siteId);
        }
    }

    private void checkInfoerrorRight(HttpServletRequest request, Integer siteId) throws RemoteException, BizException {
        if (request.getRequestURL().indexOf(urlPathMap.get(Authority.KPIWEB_INFOERROR_HANDLE)) >= 0) {//信息错误
            authorityService.checkRight(Authority.KPIWEB_INFOERROR_HANDLE, siteId);
        } else if (request.getRequestURL().indexOf(urlPathMap.get(Authority.KPIWEB_INFOERROR_IGNORE)) >= 0) {
            authorityService.checkRight(Authority.KPIWEB_INFOERROR_IGNORE, siteId);
        } else if (request.getRequestURL().indexOf(urlPathMap.get(Authority.KPIWEB_INFOERROR_DELETE)) >= 0) {
            authorityService.checkRight(Authority.KPIWEB_INFOERROR_DELETE, siteId);
        } else if (request.getRequestURL().indexOf(urlPathMap.get(Authority.KPIWEB_INFOERROR_UPDATEDEPT)) >= 0) {
            authorityService.checkRight(Authority.KPIWEB_INFOERROR_UPDATEDEPT, siteId);
        }
    }

    private void checkInfoupdateRight(HttpServletRequest request, Integer siteId) throws RemoteException, BizException {
        if (request.getRequestURL().indexOf(urlPathMap.get(Authority.KPIWEB_INFOUPDATE_HANDLE)) >= 0) {//信息更新
            authorityService.checkRight(Authority.KPIWEB_INFOUPDATE_HANDLE, siteId);
        } else if (request.getRequestURL().indexOf(urlPathMap.get(Authority.KPIWEB_INFOUPDATE_IGNORE)) >= 0) {
            authorityService.checkRight(Authority.KPIWEB_INFOUPDATE_IGNORE, siteId);
        } else if (request.getRequestURL().indexOf(urlPathMap.get(Authority.KPIWEB_INFOUPDATE_DELETE)) >= 0) {
            authorityService.checkRight(Authority.KPIWEB_INFOUPDATE_DELETE, siteId);
        } else if (request.getRequestURL().indexOf(urlPathMap.get(Authority.KPIWEB_INFOUPDATE_UPDATEDEPT)) >= 0) {
            authorityService.checkRight(Authority.KPIWEB_INFOUPDATE_UPDATEDEPT, siteId);
        }
    }

    private void checkServiceRight(HttpServletRequest request, Integer siteId) throws RemoteException, BizException {
        if (request.getRequestURL().indexOf(urlPathMap.get(Authority.KPIWEB_SERVICE_HANDLE)) >= 0) {//服务实用
            authorityService.checkRight(Authority.KPIWEB_SERVICE_HANDLE, siteId);
        } else if (request.getRequestURL().indexOf(urlPathMap.get(Authority.KPIWEB_SERVICE_IGNORE)) >= 0) {
            authorityService.checkRight(Authority.KPIWEB_SERVICE_IGNORE, siteId);
        } else if (request.getRequestURL().indexOf(urlPathMap.get(Authority.KPIWEB_SERVICE_DELETE)) >= 0) {
            authorityService.checkRight(Authority.KPIWEB_SERVICE_DELETE, siteId);
        } else if (request.getRequestURL().indexOf(urlPathMap.get(Authority.KPIWEB_SERVICE_UPDATEDEPT)) >= 0) {
            authorityService.checkRight(Authority.KPIWEB_SERVICE_UPDATEDEPT, siteId);
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
