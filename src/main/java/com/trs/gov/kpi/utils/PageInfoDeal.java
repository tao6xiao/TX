package com.trs.gov.kpi.utils;

/**
 * 主要用于处理分页的一些数据，比如，分页的数目计算
 * Created by he.lang on 2017/5/15.
 */
public class PageInfoDeal {
    public static final Integer PAGE_SIZE_DEFAULT = 20;//默认分页大小
    public static final Integer PAGE_INDEX_DEFAULT = 0;//默认0，第一页

    /**
     * 分页总数计算
     *
     * @param itemCount
     * @param pageSize
     * @return
     */
    public static int getPageCount(int itemCount, int pageSize) {
        int pageCount = itemCount % pageSize == 0 ? itemCount / pageSize : itemCount / pageSize + 1;
        return pageCount;
    }

    /**
     * 检查传入的分页大小是否为null，为空直接取默认，当然也可以自己设置
     *
     * @param pageSize
     * @param pageSizeSet
     * @return
     */
    public static int checkPageSizeIsNullOrNot(Integer pageSize, Integer pageSizeSet) {
        if (pageSize == null) {
            if (pageSizeSet == null) {
                return PAGE_SIZE_DEFAULT;
            } else {
                return pageSizeSet;
            }
        } else {
            return pageSize;
        }
    }

    /**
     * 检查传入的页数（第几页）是否为null，为空直接取默认，当然也可以自己设置
     *
     * @param pageIndex
     * @param pageIndexSet
     * @return
     */
    public static int checkPageIndexIsNullOrNot(Integer pageIndex, Integer pageIndexSet) {
        if (pageIndex == null) {
            if (pageIndexSet == null) {
                return PAGE_INDEX_DEFAULT;
            } else {
                return pageIndexSet;
            }
        } else {
            return pageIndex;
        }
    }

    /**
     * 检查当前页处理后是否为负数或者是否超出最大页数，出现对应情况需要处理
     * @param pageIndex
     * @param itemCount
     * @return
     */
    public static int dealAndcheckPageIndexIsMinusOrOutOfRang(Integer pageIndex, Integer itemCount){
        pageIndex = pageIndex - 1;//10页，最后一页为第10页
        if(pageIndex < 0){
            pageIndex = 0;
        }else if(pageIndex >= itemCount){
            pageIndex = itemCount - 1;
        }
        return pageIndex;
    }
}
