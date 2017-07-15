package com.trs.gov.kpi.entity.responsedata;

import lombok.Data;

import java.util.Date;

/**
 * 返回前端的网页更新数
 *
 * Created by li.hao on 2017/7/12.
 */
@Data
public class WkUpdateContentResponse {

    private Integer updateContent;//网站更新数
    private Date checkTime;//入库时间

}
