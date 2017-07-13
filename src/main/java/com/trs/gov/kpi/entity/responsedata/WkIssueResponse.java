package com.trs.gov.kpi.entity.responsedata;

import lombok.Data;

/**
 * Created by li.hao on 2017/7/12.
 */
@Data
public class WkIssueResponse {

    private Integer id;

    private String  chnlName;//栏目名称

    private Integer subTypeId;//问题子类型编号

    private String subTypeName;//问题类型名称

    private String url;//当前错误的URL

    private String errorInfo;//错误的详细信息

    private String parentUrl;//父页面地址

    private String locationUrl;//错误定位URL

}
