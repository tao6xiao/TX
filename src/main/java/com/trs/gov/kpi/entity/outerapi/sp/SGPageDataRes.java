package com.trs.gov.kpi.entity.outerapi.sp;

import lombok.Data;

import java.util.List;

/**
 * Created by ranwei on 2017/6/12.
 */
@Data
public class SGPageDataRes {

    private SGPager pager;

    private List<ServiceGuide> data;
}
