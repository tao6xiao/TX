package com.trs.gov.kpi.config;

import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.trs.gov.kpi.service.outer.AuthorityService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.annotation.Resource;
import java.io.File;

/**
 * Created by wangxuan on 2017/5/11.
 * 该类覆盖了原有的HttpMessageConverters，只剩下一个json的处理类，采用fastJson
 */
@Configuration
public class WebConfigure extends WebMvcConfigurerAdapter {

    @Value("${issue.location.dir}")
    private String locationDir;

    @Resource
    private AuthorityService authorityService;

    @Bean
    public HttpMessageConverters customConverters() {

        return new HttpMessageConverters(new FastJsonHttpMessageConverter());
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AccessInterceptor(authorityService)).addPathPatterns("/**");
        registry.addInterceptor(new PerformanceInterceptor()).addPathPatterns("/**");
        super.addInterceptors(registry);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/gov/kpi/loc/**").addResourceLocations("file:" + locationDir + File.separator);
        super.addResourceHandlers(registry);
    }
}
