package com.trs.gov.kpi.config;

import com.trs.gov.kpi.entity.ResponseTemplate;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import static com.trs.gov.kpi.controller.CommonProcessor.INVOKE_SUCCESS_MESSAGE;

/**
 * Created by wangxuan on 2017/5/15.
 * 用于预处理httpMessageConverter
 */
@ControllerAdvice
public class NullResponseBodyAdvice implements ResponseBodyAdvice {

    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {

        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {

        if(returnType.hasMethodAnnotation(ExceptionHandler.class)) {

            return body;
        } else {

            return new ResponseTemplate(true, INVOKE_SUCCESS_MESSAGE, body);
        }
    }
}
