package com.trs.gov.kpi.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by he.lang on 2017/8/8.
 */
@Data
public class LocalUser implements Serializable {

    private String userName;//用户名

    private String lastLoginIP;//最后一次登陆ip

    private String trueName;//用户真实姓名

    private ArrayList<Map> groups;//所属组织

}

