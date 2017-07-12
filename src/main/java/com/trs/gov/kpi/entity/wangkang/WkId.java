package com.trs.gov.kpi.entity.wangkang;

import com.trs.gov.kpi.annotation.DBField;
import com.trs.gov.kpi.annotation.DBTable;
import lombok.Data;

/**
 * 生成检测编号
 *
 * Created by li.hao on 2017/7/12.
 */
@Data
@DBTable("wkid")
public class WkId {

    @DBField
    private Integer checkId;//检测编号

    @DBField
    private Integer siteId;//网站编号

}
