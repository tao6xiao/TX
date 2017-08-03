package com.trs.gov.kpi.utils;

import com.trs.gov.kpi.constant.ErrorType;
import com.trs.gov.kpi.constant.OperationType;
import com.trs.gov.kpi.entity.exception.BizRuntimeException;
import com.trs.mlf.simplelog.LogConstant;
import com.trs.mlf.simplelog.LogUser;
import com.trs.mlf.simplelog.SimpleLogServer;
import lombok.extern.slf4j.Slf4j;

/**
 * 用于添加系统日志和获取异常信息的工具类
 * Created by he.lang on 2017/7/28.
 *
 * model:
 //记录修改设置的操作
 SimpleLogServer.operation(LogConstant.Module.EDITORCENTER.desc,LogUserUtil.getLogUser(),"修改设置","修改了设置["+config.getConfigKey()+"][设置ID="+configID+"]",systemName);
 //记录分发日志
 SimpleLogServer.distribute(LogConstant.Module.OTHERMODULE.desc,LogUserUtil.getLogUser(),"文件分发",String.format("添加或修改文件[%s]，路径[%s]",CMyFile.extractFileName(_sLocalFilePathName),sDestPathName));
 //记录安全日志（特指登录日志）
 SimpleLogServer.security(LogConstant.Module.OTHERMODULE.desc,LogUserUtil.getLogUser(),"成功", RemoteAddrUtil.getRemoteAddr(httpRequest));
 //记录系统错误日志
 SimpleLogServer.error(LogConstant.Module.OTHERMODULE.desc,LogUserUtil.getLogUser(),SystemWarn.MQMESSAGE.oper, SystemWarn.MQMESSAGE.type, "发送MQ发布消息失败,文档[ID=" + document.getId()+ "]",e);
 //记录系统警告日志
 SimpleLogServer.warn(LogConstant.Module.SYSTEMMANAGEMENT.desc,LogUserUtil.getLogUser(),SystemWarn.INITPROPERTY.oper, SystemWarn.INITPROPERTY.type,"初始化权限特殊判断对象失败",e);
 //记录系统调试日志
 SimpleLogServer.debug(LogConstant.Module.OTHERMODULE.desc,LogUserUtil.getLogUser(),SystemWarn.REQUESTHTTP.oper, "微
 */
@Slf4j
public class LogUtil {

    public static final String MODULE_NAME = LogConstant.Module.PERFORMANCE.desc;

    private LogUtil() {
    }

    /**
     * 添加错误日志
     * @param operationType
     * @param errorType
     * @param message
     * @param ex
     */
    public static void addErrorLog(String operationType, String errorType, String message, Throwable ex) {
        try {
            SimpleLogServer.error(MODULE_NAME, TRSLogUserUtil.getLogUser(), operationType, errorType, message, ex);
        } catch (BizRuntimeException e) {
//            SimpleLogServer.error(MODULE_NAME, new LogUser(), OperationType.REQUEST, ErrorType.RUN_FAILED, e.getMessage(), e);
            SimpleLogServer.error(MODULE_NAME, new LogUser(), operationType, errorType, message, ex);
        }
    }

    /**
     * 添加操作日志
     * @param operationType
     * @param logDesc
     * @param systemName
     */
    public static void addOperationLog(String operationType, String logDesc, String systemName){
        try {
            SimpleLogServer.operation(MODULE_NAME, TRSLogUserUtil.getLogUser(), operationType, logDesc, systemName);
        }catch (BizRuntimeException e) {
            SimpleLogServer.error(MODULE_NAME, new LogUser(), OperationType.REQUEST, ErrorType.RUN_FAILED, e.getMessage(), e);
        }
    }

    /**
     * 添加info日志
     * @param operationType
     * @param errorType
     * @param desc
     */
    public static void addInfoLog(String operationType, String errorType, String desc){
        SimpleLogServer.info(MODULE_NAME, new LogUser(), operationType, errorType, desc);
    }

    /**
     * 添加警告日志
     * @param operationType
     * @param errorType
     * @param desc
     */
    public static void addWarnLog(String operationType, String errorType, String desc){
        SimpleLogServer.warn(MODULE_NAME, new LogUser(), operationType, errorType, desc);
    }

    /**
     * 添加警告日志
     * @param operationType
     * @param errorType
     * @param desc
     * @param e
     */
    public static void addWarnLog(String operationType, String errorType, String desc, Throwable e){
        SimpleLogServer.warn(MODULE_NAME, new LogUser(), operationType, errorType, desc, e);
    }


}
