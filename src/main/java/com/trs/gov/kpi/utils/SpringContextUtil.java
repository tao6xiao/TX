package com.trs.gov.kpi.utils;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Created by linwei on 2017/6/16.
 */
@Component
public class SpringContextUtil implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    //设置上下文
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        SpringContextUtil.applicationContext = applicationContext;
    }

    //获取上下文
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    //通过名字获取上下文中的bean
    public static Object getBean(String name){
        return getApplicationContext().getBean(name);
    }

    //通过类型获取上下文中的bean
    public static Object getBean(Class requiredType){
        return getApplicationContext().getBean(requiredType);
    }

}
