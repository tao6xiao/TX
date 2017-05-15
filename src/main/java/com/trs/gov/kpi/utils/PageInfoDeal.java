package com.trs.gov.kpi.utils;

import com.trs.gov.kpi.entity.ApiPageData;
import com.trs.gov.kpi.entity.Pager;

import java.util.List;

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

//    public static int checkPageParameterIsNotNullOrNot(Integer pageParameter){
//        if (pageParameter == null) {
//            if(pageParameter){
//
//            }
//                return PAGE_SIZE_DEFAULT;
//        } else {
//            return pageParameter;
//        }
//    }

    /**
     * 检查传入的分页大小是否为null，为空直接取默认
     *
     * @param pageSize
     * @return
     */
    public static int checkPageSizeIsNullOrNot(Integer pageSize) {
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
    public static int checkPageIndexIsNullOrNot(Integer pageIndex) {
        if (pageIndex == null) {
            return PAGE_INDEX_DEFAULT;
        } else {
            return pageIndex;
        }
    }

    /**
     * 检查当前页处理后是否为负数或者是否超出最大页数，出现对应情况需要处理
     *
     * @param pageIndex
     * @param itemCount
     * @return
     */
    public static int dealAndcheckPageIndexIsMinusOrOutOfRang(Integer pageIndex, Integer itemCount) {
        pageIndex = pageIndex - 1;//10页，最后一页为第10页
        if (pageIndex < 0) {
            pageIndex = 0;
        } else if (pageIndex >= itemCount) {
            pageIndex = itemCount - 1;
        }
        return pageIndex;
    }

    /**
     * 获取返回的ApiPageData对象（不完整，所以调用方法之后需要将List再放入ApiPageData对象）
     * @param pageIndex
     * @param pageSize
     * @param itemCount
     * @return
     */
    public static ApiPageData getApiPageData(Integer pageIndex, Integer pageSize, Integer itemCount){
        pageSize = PageInfoDeal.checkPageSizeIsNullOrNot(pageSize);
        pageIndex = PageInfoDeal.checkPageIndexIsNullOrNot(pageIndex);
        int pageCount = PageInfoDeal.getPageCount(itemCount, pageSize);
        ApiPageData apiPageData = new ApiPageData();
        Pager pager = new Pager();
        pager.setCurrPage(pageIndex + 1);
        pager.setPageSize(pageSize);
        pager.setPageCount(pageCount);
        pager.setItemCount(itemCount);
        apiPageData.setPager(pager);
        return  apiPageData;
    }
}
