package com.trs.gov.kpi.entity.outerapi;

import lombok.Data;

/**
 * Created by linwei on 2017/5/18.
 */
@Data
public class ApiResult {
    private String isSuccess;
    private String msg;
    private String data;

    public boolean isOk() {
        return this.isSuccess != null
                && !this.isSuccess.trim().isEmpty()
                && this.isSuccess.trim().equalsIgnoreCase("true");
    }
}
