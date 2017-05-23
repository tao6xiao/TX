package com.trs.gov.kpi.utils;

import com.trs.gov.kpi.entity.exception.BizException;

/**
 * 分页参数校验工具
 */
public class PagerCheckUtil {

    public static void check(Integer pageIndex, Integer pageSize) throws BizException{
        if (pageIndex != null && pageIndex < 1) {
            throw new BizException("参数不合法！");
        }

        if (pageSize != null && pageSize < 1) {
            throw new BizException("参数不合法！");
        }
    }
}
