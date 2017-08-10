package com.trs.gov.kpi.config;

import com.trs.gov.kpi.constant.OperationType;
import com.trs.gov.kpi.utils.LogUtil;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by linwei on 2017/8/10.
 */
public class PerformanceInterceptor extends HandlerInterceptorAdapter {

    private Map<HttpServletRequest, LogUtil.PerformanceLogRecorder> recorderMap = new HashMap<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        LogUtil.PerformanceLogRecorder performanceLogRecorder = LogUtil.newPerformanceRecorder(getOperationType(request), request.getRequestURI());
        recorderMap.put(request, performanceLogRecorder);
        return super.preHandle(request, response, handler);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        super.afterCompletion(request, response, handler, ex);

        // 记录性能日志
        LogUtil.PerformanceLogRecorder performanceLogRecorder = recorderMap.get(request);
        if (performanceLogRecorder != null) {
            performanceLogRecorder.record();
        }
        recorderMap.remove(request);
    }

    private String getOperationType(HttpServletRequest request) {
        if (request.getMethod().equalsIgnoreCase("GET")) {
            return OperationType.QUERY;
        } else if (request.getMethod().equalsIgnoreCase("PUT")) {
            return OperationType.UPDATE;
        } else if (request.getMethod().equalsIgnoreCase("DELETE")) {
            return OperationType.DELETE;
        } else if (request.getMethod().equalsIgnoreCase("POST")) {
            return OperationType.ADD;
        } else {
            return OperationType.QUERY;
        }
    }
}
