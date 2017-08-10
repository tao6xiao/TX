package com.trs.gov.kpi.utils;

import com.alibaba.fastjson.JSON;
import com.squareup.okhttp.Response;
import com.trs.gov.kpi.constant.ErrorType;
import com.trs.gov.kpi.constant.OperationType;
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
        final String invalidResult = "invalid result msg: ";
        final String responseStr = ", response: ";
        try {
            result = OuterApiUtil.toResultObj(ret);
        } catch (Exception e) {
            log.error(invalidResult + ret + responseStr + response, e);
            LogUtil.addErrorLog(OperationType.REQUEST, ErrorType.REQUEST_FAILED, invalidResult + ret + responseStr + response, e);
            throw new RemoteException(errMsg + "失败！");
        }
        if (result == null) {
            log.error(invalidResult + ret + responseStr + response);
            throw new RemoteException(errMsg + "失败！");
        }
        if (!result.isOk()) {
            log.error("fail result: " + result.getMsg());
            throw new RemoteException(errMsg + "失败！[" + result.getMsg() + "]");
        }
        return result;
    }

}
