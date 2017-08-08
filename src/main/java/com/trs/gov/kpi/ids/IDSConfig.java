package com.trs.gov.kpi.ids;

import com.trs.idm.client.filter.GeneralSSOFilter;
import com.trs.idm.client.listener.CoSessionListener;
import com.trs.idm.client.servlet.GetLongUrlServlet;
import com.trs.idm.client.servlet.LoginProxyServlet;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;

/**
 * Created by he.lang on 2017/6/29.
 */
//@Configuration
public class IDSConfig {
    /*FilterRegistrationBean 用来配置urlpattern 来确定哪些路径触发filter */
    @Bean
    public FilterRegistrationBean someFilterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new GeneralSSOFilter());
        registration.addUrlPatterns("/*");
        registration.setOrder(Integer.MAX_VALUE-1);
        return registration;
    }

    /**
     * 添加listener
     * @return
     */
    @Bean
    public ServletListenerRegistrationBean servletListenerRegistrationBean(){
        ServletListenerRegistrationBean servletListenerRegistrationBean = new ServletListenerRegistrationBean();
        servletListenerRegistrationBean.setListener(new CoSessionListener());
        return servletListenerRegistrationBean;
    }

    @Bean
    public ServletRegistrationBean servletRegistrationBean() {
        return new ServletRegistrationBean(new LoginProxyServlet(), "/TRSIdSSSOProxyServlet");// ServletName默认值为首字母小写，即myServlet
    }

    @Bean
    public ServletRegistrationBean servletRegistrationBean2() {
        return new ServletRegistrationBean(new GetLongUrlServlet(), "/idsAgents/GetLongUrlServlet");// ServletName默认值为首字母小写，即myServlet
    }

}
