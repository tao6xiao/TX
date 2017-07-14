package com.trs.gov.kpi.config;

import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by wangxuan on 2017/5/11.
 * 该类覆盖了原有的HttpMessageConverters，只剩下一个json的处理类，采用fastJson
 */
@Configuration
public class WebConfigure extends WebMvcConfigurerAdapter {

    @Value("${wk.location.dir}")
    private String locDir;

    @Bean
    public HttpMessageConverters customConverters() {
        FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();
        FastJsonConfig config = converter.getFastJsonConfig();
        Feature[] features = config.getFeatures();
        List<Feature> featuresList = Arrays.asList(features);
        // 禁用循环引用
        List<Feature> lists = new ArrayList<>();
        lists.addAll(featuresList);
        lists.add(Feature.DisableCircularReferenceDetect);
        config.setFeatures(lists.toArray(new Feature[0]));
        return new HttpMessageConverters(converter);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AccessInterceptor()).addPathPatterns("/**");
        super.addInterceptors(registry);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/gov/wangkang/loc/**").addResourceLocations("file:" + locDir + File.separator);
        super.addResourceHandlers(registry);
    }
}
