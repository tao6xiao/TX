package com.trs.gov.kpi.config;

import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.trs.gov.kpi.service.outer.AuthorityService;
import org.springframework.boot.autoconfigure.web.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.annotation.Resource;

/**
 * Created by wangxuan on 2017/5/11.
 * 该类覆盖了原有的HttpMessageConverters，只剩下一个json的处理类，采用fastJson
 */
@Configuration
public class WebConfigure extends WebMvcConfigurerAdapter {

    @Resource
    private AuthorityService authorityService;

    @Bean
    public HttpMessageConverters customConverters() {

        return new HttpMessageConverters(new FastJsonHttpMessageConverter());
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AccessInterceptor(authorityService)).addPathPatterns("/**");
        super.addInterceptors(registry);
    }
}
