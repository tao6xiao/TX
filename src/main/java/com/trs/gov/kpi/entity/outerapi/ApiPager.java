package com.trs.gov.kpi.entity.outerapi;

import lombok.Data;

/**
 * Created by linwei on 2017/5/18.
 */
@Data
public class ApiPager {
    // 当前分页
    private int currPage;
    // 分页大小
    private  int pageSize;
    // 分页数目
    private  int pageCount;
    // 记录总数
    private int itemCount;
}
