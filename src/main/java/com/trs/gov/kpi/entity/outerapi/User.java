package com.trs.gov.kpi.entity.outerapi;

import lombok.Data;

/**
 * Created by he.lang on 2017/6/27.
 */
@Data
public class User {

    private Integer userId;//用户id

    private String userName;//用户名

    private String trueName;//用户真实姓名

    private String mobile;//手机号

//            "USERID"    : 1,                // 用户ID
//            "USERNAME"  : 1,                // 用户名
//            "TRUENAME" : 1,         // 真实姓名
//            "ADDRESS" : 103,            // 地址
//            "TEL" : 1,          // 固定电话
//            "MOBILE" : 1,           // 移动电话
//            "EMAIL" : 1            // 邮箱
}
