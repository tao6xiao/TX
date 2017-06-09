package com.trs.gov.kpi.entity.outerapi.nbhd;

import lombok.Data;

/**
 * Created by ranwei on 2017/6/9.
 */
@Data
public class NBHDRequestParam {

    private int siteId;

    private String userName;

    private Integer pageIndex;

    private Integer pageSize;

    private String searchField;

    private String searchText;

    private Integer solveStatus;

    private Integer isDeadLine;

    private String beginDateTime;

    private String endDateTime;

    private Integer granularity;
}
