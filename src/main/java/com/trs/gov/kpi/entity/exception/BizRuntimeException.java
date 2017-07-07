package com.trs.gov.kpi.entity.exception;

/**
 * 用于处理各种业务时的运行时异常
 * Created by he.lang on 2017/7/7.
 */
public class BizRuntimeException extends RuntimeException {

    public BizRuntimeException(String error) {
        super(error);
    }
}
