package com.trs.gov.kpi.entity.outerapi.sp;

import lombok.Data;

import java.util.Date;

/**
 * Created by ranwei on 2017/6/12.
 */
@Data
public class ServiceGuide {

    private String code;

    private String itemName;

    private String itemLink;

    private Date mTime;
}
