package com.trs.gov.kpi.entity.wangkang;

import com.trs.gov.kpi.annotation.DBField;
import com.trs.gov.kpi.annotation.DBTable;
import lombok.Data;

import java.util.Date;

/**
 * Created by li.hao on 2017/7/7.
 */
@Data
@DBTable("wkissue")
public class WkIssue {

    @DBField
    private Integer id;

    @DBField
    private Integer checkId;//检查编号

    @DBField
    private Integer siteId;//网站编号

    @DBField
    private Integer typeId;//问题类型编号

    @DBField
    private Integer subTypeId;//问题子类型编号

    @DBField
    private String  chnlName;//栏目名称

    @DBField
    private String url;//当前错误的URL

    @DBField
    private String detailInfo;//错误的详细信息

    @DBField
    private String parentUrl;//父页面地址

    @DBField
    private Date checkTime;//检测时间

    @DBField
    private Integer isResolved;//问题是否处理

    @DBField
    private Date resolveTime;//处理时间

    @DBField
    private Integer isDel;//是否删除

    @DBField
    private String locationUrl;//错误定位URL
}
