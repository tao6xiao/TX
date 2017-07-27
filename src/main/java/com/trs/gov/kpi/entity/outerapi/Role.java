package com.trs.gov.kpi.entity.outerapi;

import lombok.Data;

/**
 * Created by ranwei on 2017/7/27.
 */
@Data
public class Role {

    private String roleId;

    private String roleName;

    private String objType;//0-->平台级角色   103-->站点级角色

}
