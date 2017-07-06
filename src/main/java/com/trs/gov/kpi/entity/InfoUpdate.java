package com.trs.gov.kpi.entity;

import com.trs.gov.kpi.annotation.DBField;
import com.trs.gov.kpi.annotation.DBTable;
import lombok.Data;

import java.util.Date;

/**
 * 信息更新
 * Created by he.lang on 2017/5/26.
 */
@Data
@DBTable
public class InfoUpdate {

    @DBField(autoInc = true)
    private Integer id;

    @DBField
    private Integer siteId;

    @DBField
    private Integer typeId;

    @DBField
    private Integer subTypeId;

    @DBField("detail")
    private String chnlUrl;

    @DBField
    private Date issueTime;

    @DBField
    private Date checkTime;

    @DBField
    private Integer isResolved;

    @DBField
    private Integer isDel;

    @DBField("customer2")
    private Integer chnlId;

    @DBField
    private Integer workOrderStatus;

    @DBField
    private Integer deptId;

}
