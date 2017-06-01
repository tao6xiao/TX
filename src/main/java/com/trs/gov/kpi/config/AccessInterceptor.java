package com.trs.gov.kpi.config;

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

        return super.preHandle(request, response, handler);
    }
}
