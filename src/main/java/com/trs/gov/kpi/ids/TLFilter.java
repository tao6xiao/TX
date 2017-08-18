package com.trs.gov.kpi.ids;

import com.trs.gov.kpi.constant.ErrorType;
import com.trs.gov.kpi.constant.OperationType;
import com.trs.gov.kpi.entity.LocalUser;
import com.trs.gov.kpi.entity.exception.BizException;
import com.trs.gov.kpi.utils.LogUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by he.lang on 2017/7/14.
 */
@Component
@WebFilter(filterName = "TLFilter", urlPatterns = "/*")
@Order(Integer.MAX_VALUE)
@Slf4j
public class TLFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("init TLFilter!");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        final LocalUser localUser = (LocalUser)req.getSession().getAttribute(IDSActor.LOGIN_FLAG);
        if (localUser != null) {
            ContextHelper.initContext(localUser);
            chain.doFilter(request, response);
        } else {
            //给其他模块提供的接口需要放行
            if (req.getRequestURI().startsWith("/gov/kpi/opendata")) {
                chain.doFilter(request, response);
            }

            final BizException ex = new BizException("当前用户处于未登录状态或者IDS服务器已停止");
            log.error("", ex);
            LogUtil.addErrorLog(OperationType.REQUEST, ErrorType.BIZ_EXCEPTION, ex.getMessage(), ex);
            HttpServletResponse resp = (HttpServletResponse) response;
            resp.sendError(HttpServletResponse.SC_FORBIDDEN);
        }
    }

    @Override
    public void destroy() {
        log.info("destroy TLFilter!");
    }
}
