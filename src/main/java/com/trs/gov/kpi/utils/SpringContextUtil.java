package com.trs.gov.kpi.utils;

import org.springframework.context.ApplicationContext;

/**
 * Created by linwei on 2017/6/16.
 */
public class SpringContextUtil {

    private static ApplicationContext applicationContext;

    private SpringContextUtil(){
    }


    //获取上下文
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    //设置上下文
    public static void setApplicationContext(ApplicationContext applicationContext) {
        SpringContextUtil.applicationContext = applicationContext;
    }

    //通过名字获取上下文中的bean
    public static Object getBean(String name){
        return applicationContext.getBean(name);
    }

    //通过类型获取上下文中的bean
    public static Object getBean(Class requiredType){
        return applicationContext.getBean(requiredType);
    }

}
