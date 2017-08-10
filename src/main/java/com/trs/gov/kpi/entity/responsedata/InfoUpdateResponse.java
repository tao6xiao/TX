package com.trs.gov.kpi.entity.responsedata;

import lombok.Data;

import java.util.Date;

/**
 * 信息更新数据的返回模板
 * Created by rw103 on 2017/5/13.
 */
@Data
public class InfoUpdateResponse {

    /**
     * 问题编号
     */
    private Integer id;

    /**
     * 站点编号
     */
    private Integer siteId;

    private String chnlName = "";

    private Integer chnlId;

    private String chnlUrl = "";

    private Integer issueTypeId;

    private String issueTypeName;

    private Date checkTime;

    private String workOrderStatus;

    private String deptName;//部门名称

}
