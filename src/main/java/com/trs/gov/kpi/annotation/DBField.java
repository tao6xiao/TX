package com.trs.gov.kpi.annotation;

import java.lang.annotation.*;

/**
 * Created by linwei on 2017/6/7.
 *
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DBField {
    /**
     * 字段名
     * @return
     */
    String name() default "";

    /**
     * 自增长字段
     *
     * @return
     */
    boolean autoInc() default false;
}
