package trs.com.cn.gov.kpi.utils;

import trs.com.cn.gov.kpi.entity.exception.BizException;

/**
 * Created by wangxuan on 2017/5/10.
 * 用于简单验证业务处理并抛出异常
 */
public class ValidateUtils {

    public static void isTrue(boolean expression, String message) throws BizException {

        if (!expression) {

            throw new BizException(message);
        }
    }
}
