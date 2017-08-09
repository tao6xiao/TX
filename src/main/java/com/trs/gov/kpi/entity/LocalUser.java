package com.trs.gov.kpi.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by he.lang on 2017/8/8.
 */
@Data
public class LocalUser implements Serializable {
    private String userName;//用户名
    private String lastLoginIP;//最后一次登陆ip
}

