package com.trs.gov.kpi.entity;

import lombok.Data;

import java.util.Date;

/**
 * Created by rw103 on 2017/5/13.
 */
@Data
public class IssueBase {

    /**
     * 问题记录起止时间
     */
    public String beginDateTime;

    public String endDateTime;

    /**
     * 用于模糊查询
     */
    public String search;
}
