package com.trs.gov.kpi.entity.responsedata;

import lombok.Data;

import java.util.Date;

/**
 * Created by li.hao on 2017/7/14.
 */
@Data
public class WkLinkIndexPageStatus {

    private String siteIndexUrl;

    private Date checkTime;

    private Boolean status;

}
