package com.trs.gov.kpi.utils;

import com.alibaba.fastjson.JSON;
import com.trs.gov.kpi.entity.outerapi.ApiResult;

/**
 * Created by linwei on 2017/5/18.
 */
public class OuterApiUtil {

    private OuterApiUtil() {

    }

    /**
     * 判定结果是否成功
     *
     * @param jsonResult
     * @return
     */
    public static boolean isResultSuccess(String jsonResult) {
        if (StringUtil.isEmpty(jsonResult)) {
            return false;
        }

        ApiResult result = JSON.parseObject(jsonResult, ApiResult.class);
        return result.isOk();
    }

    /**
     * 转换JSON为对象
     *
     * @param jsonResult
     * @return
     */
    public static ApiResult toResultObj(String jsonResult) {
        if (StringUtil.isEmpty(jsonResult)) {
            return null;
        }
        return JSON.parseObject(jsonResult, ApiResult.class);
    }


}
