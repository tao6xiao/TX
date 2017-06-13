package com.trs.gov.kpi.utils;

import com.trs.gov.kpi.entity.responsedata.Pager;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by ranwei on 2017/6/13.
 */
public class PageInfoDealTest {

    @Test
    public void buildResponsePager_Null_Not_Divisible() throws Exception {
        Integer pageIndex = null;
        Integer pageSize = null;
        Integer itemCount = 12;
        Pager pager = PageInfoDeal.buildResponsePager(pageIndex, pageSize, itemCount);
        assertEquals(new Integer(1), pager.getCurrPage());
        assertEquals(new Integer(20), pager.getPageSize());
        assertEquals(new Integer(1), pager.getPageCount());
        assertEquals(new Integer(12), pager.getItemCount());
    }

    @Test
    public void buildResponsePager_Divisible() throws Exception {
        Integer pageIndex = 2;
        Integer pageSize = 5;
        Integer itemCount = 20;
        Pager pager = PageInfoDeal.buildResponsePager(pageIndex, pageSize, itemCount);
        assertEquals(new Integer(2), pager.getCurrPage());
        assertEquals(new Integer(5), pager.getPageSize());
        assertEquals(new Integer(4), pager.getPageCount());
        assertEquals(new Integer(20), pager.getItemCount());
    }
}