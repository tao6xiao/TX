package com.trs.gov.kpi.entity.requestdata;

import lombok.Data;

/**
 * Created by linwei on 2017/6/14.
 */
@Data
public class CheckTextRequest {
    String[] checkType;
    String checkContent;
}
