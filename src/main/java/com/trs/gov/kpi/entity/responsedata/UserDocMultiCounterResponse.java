package com.trs.gov.kpi.entity.responsedata;

import lombok.Data;

/**
 * Created by linwei on 2017/6/15.
 */
@Data
public class UserDocMultiCounterResponse extends DocMultiCounterResponse {

    private Long userId;
    private String userName;

}
