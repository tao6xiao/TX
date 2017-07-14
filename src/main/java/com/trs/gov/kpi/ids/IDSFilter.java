package com.trs.gov.kpi.ids;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by he.lang on 2017/7/10.
 */
@Component
@WebFilter(filterName = "IDSFilter", urlPatterns = "/*")
@Order(Integer.MAX_VALUE-2)
public class IDSFilter implements Filter{

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse httpResponse = (HttpServletResponse)servletResponse;
        httpResponse.addHeader("Access-Control-Allow-Origin", "*");
        httpResponse.setHeader("Access-Control-Allow-Headers", "Content-Type,X-Requested-With,accept,Origin,Access-Control-Request-Method,Access-Control-Request-Headers,formdata");
        httpResponse.setHeader("Access-Control-Allow-Methods", "GET,PUT,DELETE,POST,OPTIONS");
        HttpServletRequest httpRequest = (HttpServletRequest)servletRequest;
        httpResponse.setHeader("requestURL", httpRequest.getRequestURL() + "?" + httpRequest.getQueryString());
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
    }
}
