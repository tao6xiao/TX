package com.trs.gov.kpi.config;

import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.springframework.boot.autoconfigure.web.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by wangxuan on 2017/5/11.
 * 该类覆盖了原有的HttpMessageConverters，只剩下一个json的处理类，采用fastJson
 */
@Configuration
public class HttpMessageConverterConfigure {

    @Bean
    public HttpMessageConverters customConverters() {

        return new HttpMessageConverters(new FastJsonHttpMessageConverter());
    }
}
