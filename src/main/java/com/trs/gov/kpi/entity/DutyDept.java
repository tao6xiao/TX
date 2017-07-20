package com.trs.gov.kpi.entity;

import com.trs.gov.kpi.annotation.DBField;
import com.trs.gov.kpi.annotation.DBTable;
import lombok.Data;

/**
 * 栏目部门关系表对应实体类
 * Created by he.lang on 2017/7/5.
 */
@DBTable
@Data
public class DutyDept {
    @DBField
    private Integer chnlId;//栏目编号

    @DBField
    private Integer siteId;//站点编号

    @DBField
    private Integer deptId;//部门编号

    @DBField
    private Byte contain;//是否包含子栏目，0：不包含，1：包含

}
