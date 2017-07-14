package com.trs.gov.kpi.ids;

import com.trs.idm.client.actor.SSOUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
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
        if(req.getSession().getAttribute(IDSActor.LOGIN_FLAG) != null) {//暂时为了防止IDS服务器未启动时也开启线程
            ContextHelper.initContext((SSOUser) req.getSession().getAttribute(IDSActor.LOGIN_FLAG));
        }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}
