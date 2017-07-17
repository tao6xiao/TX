package com.trs.gov.kpi.entity.requestdata;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * Created by linwei on 2017/5/22.
 */
@Data
public class PageDataRequestParam extends DateRequest {

    /**
     * 问题编号
     */
    private String id;

    @NotNull
    private Integer siteId;

    /**
     * 检查编号
     */
    private Integer checkId;

}
