package com.trs.gov.kpi.ids;

import com.trs.gov.kpi.entity.exception.BizException;
import com.trs.idm.client.actor.SSOUser;
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
public class TLFilter implements Filter{

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        if(req.getSession().getAttribute(IDSActor.LOGIN_FLAG) != null) {
            ContextHelper.initContext((SSOUser) req.getSession().getAttribute(IDSActor.LOGIN_FLAG));
            chain.doFilter(request, response);
        }else {
            log.error("Invalid user: The current user in the logout state or IDS server is stopped");
            try {
                throw new BizException("当前用户处于未登录状态或者IDS服务器已停止");
            } catch (BizException e) {
                log.error("", e);
            }
            resp.sendError(HttpServletResponse.SC_BAD_GATEWAY);
        }
    }

    @Override
    public void destroy() {

    }
}
