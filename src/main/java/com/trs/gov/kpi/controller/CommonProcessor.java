package com.trs.gov.kpi.controller;

import com.trs.gov.kpi.constant.ErrorType;
import com.trs.gov.kpi.constant.OperationType;
import com.trs.gov.kpi.entity.ResponseTemplate;
import com.trs.gov.kpi.entity.exception.BizException;
import com.trs.gov.kpi.entity.exception.BizRuntimeException;
import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.gov.kpi.utils.LogUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by wangxuan on 2017/5/10.
 * 该类用于统一异常处理
 * TODO：日志打印规范未完成
 */
@ControllerAdvice
@Order(Ordered.LOWEST_PRECEDENCE)
@Aspect
@Slf4j
public class CommonProcessor {

    static final String BIZEXCEPTION_MESSAGE = "业务调用失败";

    static final String REMOTEEXCEPTION_MESSAGE = "远程调用失败";

    static final String SYSTEMEXCEPTION_MESSAGE = "系统错误";

    static final String BIZRUNTIMEEXCEPTION_MESSAGE = "业务运行失败";

    public static final String INVOKE_SUCCESS_MESSAGE = "操作成功";

    @ExceptionHandler(BizException.class)
    @ResponseBody
    public ResponseTemplate handBizException(Exception ex) {
        LogUtil.addErrorLog(OperationType.REQUEST, ErrorType.REQUEST_FAILED, ex.getLocalizedMessage(), ex);
        log.error("", ex);
        return new ResponseTemplate(false,
                StringUtils.isEmpty(ex.getMessage()) ? BIZEXCEPTION_MESSAGE : ex.getMessage(),
                null);
    }

    @ExceptionHandler(RemoteException.class)
    @ResponseBody
    public ResponseTemplate handRemoteException(Exception ex) {
        LogUtil.addErrorLog(OperationType.REQUEST, ErrorType.REQUEST_FAILED, ex.getLocalizedMessage(), ex);
        log.error("", ex);
        return new ResponseTemplate(false,
                StringUtils.isEmpty(ex.getMessage()) ? REMOTEEXCEPTION_MESSAGE : ex.getMessage(),
                null);
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseTemplate handCommonException(Exception ex) {
        LogUtil.addErrorLog(OperationType.REQUEST, ErrorType.REQUEST_FAILED, ex.getLocalizedMessage(), ex);
        log.error("", ex);
        return new ResponseTemplate(false, SYSTEMEXCEPTION_MESSAGE, null);
    }

    @ExceptionHandler(BizRuntimeException.class)
    @ResponseBody
    public ResponseTemplate handBizRuntimeException(Exception ex) {
        LogUtil.addErrorLog(OperationType.REQUEST, ErrorType.REQUEST_FAILED, ex.getLocalizedMessage(), ex);
        log.error("", ex);
        return new ResponseTemplate(false,
                StringUtils.isEmpty(ex.getMessage()) ? BIZRUNTIMEEXCEPTION_MESSAGE : ex.getMessage(),
                null);
    }
}
