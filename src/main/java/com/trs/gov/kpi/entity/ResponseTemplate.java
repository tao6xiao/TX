package com.trs.gov.kpi.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by wangxuan on 2017/5/11.
 */
@Data
@AllArgsConstructor
public class ResponseTemplate {

    Boolean isSuccess;

    String msg;

    Object data;
}
