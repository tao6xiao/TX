package com.trs.gov.kpi.entity.outerapi.sp;

import lombok.Data;

/**
 * Created by ranwei on 2017/6/12.
 */
@Data
public class SGPager {

    private int currPage;

    private int pageSize;

    private int pageCount;

    private int itemCount;

}
