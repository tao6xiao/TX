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
        map.put("/gov/kpi/issue/handle", Authority.KPIWEB_ISSUE_HANDLE);
        map.put("/gov/kpi/issue/delete", Authority.KPIWEB_ISSUE_DELETE);
        map.put("/gov/kpi/issue/ignore", Authority.KPIWEB_ISSUE_IGNORE);
        map.put("/gov/kpi/issue/updatedept", Authority.KPIWEB_ISSUE_UPDATEDEPT);

        map.put("/gov/kpi/alert/handle", Authority.KPIWEB_WARNING_HANDLE);
        map.put("/gov/kpi/alert/delete", Authority.KPIWEB_WARNING_DELETE);
        map.put("/gov/kpi/alert/ignore", Authority.KPIWEB_WARNING_IGNORE);
        map.put("/gov/kpi/alert/updatedept", Authority.KPIWEB_WARNING_UPDATEDEPT);

        map.put("/gov/kpi/service/link/issue/handle", Authority.KPIWEB_SERVICE_HANDLE);
        map.put("/gov/kpi/service/link/issue/delete", Authority.KPIWEB_SERVICE_DELETE);
        map.put("/gov/kpi/service/link/issue/ignore", Authority.KPIWEB_SERVICE_IGNORE);
        map.put("/gov/kpi/service/link/issue/updatedept", Authority.KPIWEB_SERVICE_UPDATEDEPT);

        map.put("/gov/kpi/available/issue/handle", Authority.KPIWEB_AVAILABILITY_HANDLE);
        map.put("/gov/kpi/available/issue/delete", Authority.KPIWEB_AVAILABILITY_DELETE);
        map.put("/gov/kpi/available/issue/ignore", Authority.KPIWEB_AVAILABILITY_IGNORE);
        map.put("/gov/kpi/available/issue/updatedept", Authority.KPIWEB_AVAILABILITY_UPDATEDEPT);

        map.put("/gov/kpi/content/issue/handle", Authority.KPIWEB_INFOERROR_HANDLE);
        map.put("/gov/kpi/content/issue/delete", Authority.KPIWEB_INFOERROR_DELETE);
        map.put("/gov/kpi/content/issue/ignore", Authority.KPIWEB_INFOERROR_IGNORE);
        map.put("/gov/kpi/content/issue/updatedept", Authority.KPIWEB_INFOERROR_UPDATEDEPT);

        map.put("/gov/kpi/channel/issue/handle", Authority.KPIWEB_INFOUPDATE_HANDLE);
        map.put("/gov/kpi/channel/issue/delete", Authority.KPIWEB_INFOUPDATE_DELETE);
        map.put("/gov/kpi/channel/issue/ignore", Authority.KPIWEB_INFOUPDATE_IGNORE);
        map.put("/gov/kpi/channel/issue/updatedept", Authority.KPIWEB_INFOUPDATE_UPDATEDEPT);
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
            String oprKey = urlPathMap.get(request.getRequestURI());
            if (oprKey != null) {
                authorityService.checkRight(oprKey, siteId);
            }
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
