package com.trs.gov.kpi.utils;

import com.alibaba.fastjson.JSON;
import com.squareup.okhttp.Response;
import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.gov.kpi.entity.outerapi.ApiResult;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * Created by linwei on 2017/5/18.
 */
@Slf4j
public class OuterApiUtil {

    private OuterApiUtil() {

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


    public static ApiResult getValidResult(Response response, String errMsg) throws RemoteException,
            IOException {
        String ret = response.body().string();

        ApiResult result = null;
        try {
            result = OuterApiUtil.toResultObj(ret);
        } catch (Exception e) {
            log.error("invalid result msg: " + ret + ", response: " + response, e);
            throw new RemoteException(errMsg + "失败！");
        }
        if (result == null) {
            log.error("invalid result msg: " + ret + ", response: " + response);
            throw new RemoteException(errMsg + "失败！");
        }
        if (!result.isOk()) {
            log.error("fail result: " + result.getMsg());
            throw new RemoteException(errMsg + "失败！[" + result.getMsg() + "]");
        }
        return result;
    }

}
