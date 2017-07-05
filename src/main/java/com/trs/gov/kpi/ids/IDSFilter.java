package com.trs.gov.kpi.ids;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by he.lang on 2017/6/30.
 */
@Component
@WebFilter(filterName = "IDSFilter", urlPatterns = "/*")
@Order(Integer.MAX_VALUE)
public class IDSFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest)request;
        HttpServletResponse resp = (HttpServletResponse)response;

        if (req.getSession().getAttribute(IDSActor.LOGIN_FLAG) == null) {
            if (!req.getRequestURI().endsWith("/gov/kpi/index")
                    && !req.getRequestURI().endsWith("/security/login")
                    && !req.getRequestURI().endsWith("/TRSIdSSSOProxyServlet")) {
                resp.sendRedirect("http://bj.govdev.dev3.trs.org.cn/govapp/#/login");
            }
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }
}
