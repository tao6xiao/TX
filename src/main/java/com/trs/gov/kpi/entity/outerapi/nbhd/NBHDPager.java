package com.trs.gov.kpi.entity.outerapi.nbhd;

import lombok.Data;

/**
 * Created by ranwei on 2017/6/9.
 */
@Data
public class NBHDPager {
    private int pageSize;
    private int pageIndex;
    private int totalCount;
    private int totalPage;
}
