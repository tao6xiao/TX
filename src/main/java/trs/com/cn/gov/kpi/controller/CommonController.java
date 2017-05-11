package trs.com.cn.gov.kpi.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import trs.com.cn.gov.kpi.entity.ResponseTemplate;
import trs.com.cn.gov.kpi.entity.exception.BizException;
import trs.com.cn.gov.kpi.entity.exception.RemoteException;

/**
 * Created by wangxuan on 2017/5/10.
 * 该类用于统一异常处理
 * TODO：日志打印规范未完成
 */
@ControllerAdvice
@Slf4j
public class CommonController {

    static final String BIZEXCEPTION_MESSAGE = "业务调用失败";

    static final String REMOTEEXCEPTION_MESSAGE = "远程调用失败";

    static final String SYSTEMEXCEPTION_MESSAGE = "系统错误";

    @ExceptionHandler(BizException.class)
    @ResponseBody
    public ResponseTemplate handBizException(Exception ex) {

        log.error("", ex);
        return new ResponseTemplate(false,
                StringUtils.isEmpty(ex.getMessage()) ? BIZEXCEPTION_MESSAGE : ex.getMessage(),
                null);
    }

    @ExceptionHandler(RemoteException.class)
    @ResponseBody
    public ResponseTemplate handRemoteException(Exception ex) {

        log.error("", ex);
        return new ResponseTemplate(false,
                StringUtils.isEmpty(ex.getMessage()) ? REMOTEEXCEPTION_MESSAGE : ex.getMessage(),
                null);
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseTemplate handCommonException(Exception ex) {

        log.error("", ex);
        return new ResponseTemplate(false,
                StringUtils.isEmpty(ex.getMessage()) ? SYSTEMEXCEPTION_MESSAGE : ex.getMessage(),
                null);
    }
}
