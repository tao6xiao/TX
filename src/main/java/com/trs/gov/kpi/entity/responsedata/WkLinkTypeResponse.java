package com.trs.gov.kpi.entity.responsedata;

import lombok.Data;

/**
 * Created by li.hao on 2017/7/14.
 */
@Data
public class WkLinkTypeResponse {

    private Integer allLink;//链接总数

    private Integer webLink;//网页个数

    private Integer imageLink;//图片个数

    private Integer videoLink;//视频个数

    private Integer enclosuLink;//附件个数

}
