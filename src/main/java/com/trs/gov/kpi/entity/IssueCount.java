package com.trs.gov.kpi.entity;

import lombok.Data;

/**
 * Created by rw103 on 2017/5/12.
 */
@Data
public class IssueCount {

    /**
     * 0--> 未解决，1-->已解决
     */
    private int type;

    private int count;

}
