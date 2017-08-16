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

    // 不包含子栏目
    public static final Byte NOT_CONTAIN_CHILD = 0;

    // 包含子栏目
    public static final Byte CONTAIN_CHILD = 1;

    // 所有情况
    public static final Byte ALL_CONTAIN_COND = 2;

    @DBField
    private Integer chnlId;//栏目编号

    @DBField
    private Integer siteId;//站点编号

    @DBField
    private Integer deptId;//部门编号

    @DBField
    private Byte contain;//是否包含子栏目，0：不包含，1：包含

}
