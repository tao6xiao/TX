package com.trs.gov.kpi.config;

import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.trs.gov.kpi.entity.ResponseTemplate;
import org.springframework.boot.autoconfigure.web.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotWritableException;

import java.io.IOException;

/**
 * Created by wangxuan on 2017/5/11.
 * 该类覆盖了原有的HttpMessageConverters，只剩下一个json的处理类，采用fastJson
 */
@Configuration
public class HttpMessageConverterConfigure {

    @Bean
    public HttpMessageConverters customConverters() {

        HttpMessageConverter jsonConverter = new FastJsonHttpMessageConverter() {

            protected void writeInternal(Object obj, HttpOutputMessage outputMessage)
                    throws IOException, HttpMessageNotWritableException {

                ResponseTemplate response = null;
                if(ResponseTemplate.class.isInstance(obj)) {

                    response = ResponseTemplate.class.cast(obj);
                } else {

                    response = new ResponseTemplate(true, "操作成功", obj);
                }
                super.writeInternal(response, outputMessage);
            }
        };
        return new HttpMessageConverters(jsonConverter);
    }
}
