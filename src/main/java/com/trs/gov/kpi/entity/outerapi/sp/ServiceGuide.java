package com.trs.gov.kpi.entity.outerapi.sp;

import lombok.Data;

/**
 * Created by ranwei on 2017/6/12.
 */
@Data
public class ServiceGuide {

    private String code; //事项简码

    private String itemName; //事项名称

    private String qType;//问题类型

    private String itemLink;//事项链接

    private String mTime; //监测时间
}
