package com.trs.gov.kpi.utils;

import com.trs.gov.kpi.entity.responsedata.Pager;

/**
 * 主要用于处理分页的一些数据，比如，分页的数目计算
 * Created by he.lang on 2017/5/15.
 */
public class PageInfoDeal {
    public static final Integer PAGE_SIZE_DEFAULT = 20;//默认分页大小
    public static final Integer PAGE_INDEX_DEFAULT = 1;//默认1，第一页

    private PageInfoDeal() {

    }

    /**
     * 分页总数计算
     *
     * @param itemCount
     * @param pageSize
     * @return
     */
    private static int getPageCount(int itemCount, int pageSize) {
        return itemCount % pageSize == 0 ? itemCount / pageSize : itemCount / pageSize + 1;
    }

    /**
     * 检查传入的分页大小是否为null，为空直接取默认
     *
     * @param pageSize
     * @return
     */
    private static int initPageSize(Integer pageSize) {
        if (pageSize == null) {
            return PAGE_SIZE_DEFAULT;
        } else {
            return pageSize;
        }
    }

    /**
     * 检查传入的页数（第几页）是否为null，为空直接取默认
     *
     * @param pageIndex
     * @return
     */
    private static int initPageIndex(Integer pageIndex) {
        if (pageIndex == null) {
            return PAGE_INDEX_DEFAULT;
        } else {
            return pageIndex;
        }
    }

    /**
     * 获取返回的ApiPageData对象（不完整，所以调用方法之后需要将List再放入ApiPageData对象）
     *
     * @param pageIndex 从1开始计数的页
     * @param pageSize
     * @param itemCount
     * @return
     */
    public static Pager buildResponsePager(Integer pageIndex, Integer pageSize, Integer itemCount) {
        int newPageSize = PageInfoDeal.initPageSize(pageSize);
        int newPageIndex = PageInfoDeal.initPageIndex(pageIndex);
        int pageCount = PageInfoDeal.getPageCount(itemCount, newPageSize);

        Pager pager = new Pager();
        pager.setCurrPage(newPageIndex);
        pager.setPageSize(newPageSize);
        pager.setPageCount(pageCount);
        pager.setItemCount(itemCount);
        return pager;
    }
}
