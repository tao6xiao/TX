package com.trs.gov.kpi.entity.outerapi;

import lombok.Data;

/**
 * 部门类
 * Created by he.lang on 2017/6/28.
 */
@Data
public class Dept {

    private Integer groupId;//部门id

    private String gName;//部门名称

//    "GROUPID"   : 1,                // 组织ID
//            "GNAME"     : "四川省文化厅",             // 组织名称
//            "GDESC"     : "省文化厅",               // 组织简称
//            "ADMINID" : 2,                  // 组织管理员ID
//            "GADMINNAME" : "张",             // 组织管理员用户名
//            "PARENTID" : 0,                 // 父组织ID
//            "GROUPORDER" : 0,               // 组织顺序
}
