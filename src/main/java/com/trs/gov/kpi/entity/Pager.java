package com.trs.gov.kpi.entity;

import lombok.Data;

/**
 * Created by rw103 on 2017/5/13.
 */
@Data
public class Pager
{
    private Integer currPage;

    private Integer pageSize;

    private Integer pageCount;

    private Integer itemCount;

}
