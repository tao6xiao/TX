package com.trs.gov.kpi.utils;

import com.trs.gov.kpi.constant.ErrorType;
import com.trs.gov.kpi.constant.OperationType;
import com.trs.gov.kpi.entity.exception.BizException;
import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.gov.kpi.entity.outerapi.Site;
import com.trs.gov.kpi.service.outer.SiteApiService;
import com.trs.mlf.simplelog.LogConstant;
import com.trs.mlf.simplelog.LogUser;
import com.trs.mlf.simplelog.SimpleLogServer;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;

import javax.validation.constraints.NotNull;
import java.text.ParseException;
import java.util.Date;
import java.util.Map;

/**
 * 用于添加系统日志和获取异常信息的工具类
 * Created by he.lang on 2017/7/28.
 * <p>
 * model:
 * //记录修改设置的操作
 * SimpleLogServer.operation(LogConstant.Module.EDITORCENTER.desc,LogUserUtil.getLogUser(),"修改设置","修改了设置["+config.getConfigKey()+"][设置ID="+configID+"]",systemName);
 * //记录分发日志
 * SimpleLogServer.distribute(LogConstant.Module.OTHERMODULE.desc,LogUserUtil.getLogUser(),"文件分发",String.format("添加或修改文件[%s]，路径[%s]",CMyFile.extractFileName(_sLocalFilePathName),sDestPathName));
 * //记录安全日志（特指登录日志）
 * SimpleLogServer.security(LogConstant.Module.OTHERMODULE.desc,LogUserUtil.getLogUser(),"成功", RemoteAddrUtil.getRemoteAddr(httpRequest));
 * //记录系统错误日志
 * SimpleLogServer.error(LogConstant.Module.OTHERMODULE.desc,LogUserUtil.getLogUser(),SystemWarn.MQMESSAGE.oper, SystemWarn.MQMESSAGE.type, "发送MQ发布消息失败,文档[ID=" + document.getId()+ "]",e);
 * //记录系统警告日志
 * SimpleLogServer.warn(LogConstant.Module.SYSTEMMANAGEMENT.desc,LogUserUtil.getLogUser(),SystemWarn.INITPROPERTY.oper, SystemWarn.INITPROPERTY.type,"初始化权限特殊判断对象失败",e);
 * //记录系统调试日志
 * SimpleLogServer.debug(LogConstant.Module.OTHERMODULE.desc,LogUserUtil.getLogUser(),SystemWarn.REQUESTHTTP.oper, "微
 */
@Slf4j
public class LogUtil {

    public static final String MODULE_NAME = LogConstant.Module.PERFORMANCE.desc;

    private LogUtil() {
    }

    /**
     * 添加错误日志
     *
     * @param operationType
     * @param errorType
     * @param message
     * @param ex
     */
    public static void addErrorLog(String operationType, String errorType, String message, Throwable ex) {
        SimpleLogServer.error(MODULE_NAME, TRSLogUserUtil.getLogUser(), operationType, errorType, message, ex);
    }

    /**
     * 添加操作日志
     *
     * @param operationType
     * @param logDesc
     * @param systemName
     */
    public static void addOperationLog(String operationType, String logDesc, String systemName) {
        SimpleLogServer.operation(MODULE_NAME, TRSLogUserUtil.getLogUser(), operationType, logDesc, systemName);

    }

    /**
     * 添加info日志
     *
     * @param operationType
     * @param errorType
     * @param desc
     */
    public static void addInfoLog(String operationType, String errorType, String desc) {
        SimpleLogServer.info(MODULE_NAME, new LogUser(), operationType, errorType, desc);
    }

    /**
     * 添加警告日志
     *
     * @param operationType
     * @param errorType
     * @param desc
     */
    public static void addWarnLog(String operationType, String errorType, String desc) {
        SimpleLogServer.warn(MODULE_NAME, new LogUser(), operationType, errorType, desc);
    }

    /**
     * 添加警告日志
     *
     * @param operationType
     * @param errorType
     * @param desc
     * @param e
     */
    public static void addWarnLog(String operationType, String errorType, String desc, Throwable e) {
        SimpleLogServer.warn(MODULE_NAME, new LogUser(), operationType, errorType, desc, e);
    }

    /**
     * 添加性能日志
     *
     * @param operationType
     * @param desc
     * @param timeUsed
     */
    public static void addElapseLog(String operationType, String desc, int timeUsed) {
        SimpleLogServer.elapse(MODULE_NAME, new LogUser(), operationType, desc, timeUsed);
    }

    /**
     * 添加性能日志
     *
     * @param operationType
     * @param desc
     * @param timeUsed
     */
    public static void addElapseLog(String operationType, String desc, long timeUsed) {
        SimpleLogServer.elapse(MODULE_NAME, TRSLogUserUtil.getLogUser(), operationType, desc, timeUsed);
    }

    /**
     * 添加调试日志
     *
     * @param operationType
     * @param debugType
     * @param desc
     */
    public static void addDebugLog(String operationType, String debugType, String desc) {
        SimpleLogServer.debug(MODULE_NAME, new LogUser(), operationType, debugType, desc);
    }

    /**
     * 为记录日志获取站点名称
     *
     * @param service
     * @param siteId
     * @return
     */
    public static String getSiteNameForLog(SiteApiService service, Integer siteId) {
        try {
            final Site site = service.getSiteById(siteId, "");
            if (site != null) {
                return site.getSiteName();
            } else {
                return "site[" + siteId + "]";
            }
        } catch (Exception e) {
            addErrorLog(OperationType.REMOTE, ErrorType.REMOTE_FAILED, "查询站点失败", e);
            return "site[" + siteId + "]";
        }
    }

    /**
     * 构造性能日志描述
     *
     * @param service
     * @param siteId
     * @param logDesc 描述， 不能为空
     * @return
     */
    public static String buildElapseLogDesc(SiteApiService service, Integer siteId, @NotNull String logDesc) {
        StringBuilder builder = new StringBuilder();
        builder.append("相关站点：");
        builder.append(getSiteNameForLog(service, siteId));
        builder.append("，");
        builder.append(logDesc);
        return builder.toString();
    }

    /**
     * 构造失败操作日志描述
     *
     * @param logDesc
     * @return
     */
    public static String buildFailOperationLogDesc(String logDesc) {
        return "操作失败：" + logDesc;
    }

    /**
     * control处理函数Function
     * @param <R>
     */
    @FunctionalInterface
    public interface ControllorFunction<R> {
        R apply() throws RemoteException, BizException;
    }

    /**
     * Controller的操作封装函数，对于异常处理情况记录，操作失败日志
     */
    public static <R> R ControlleFunctionWrapper(ControllorFunction<R> func, String type, String desc, String systemName) throws RemoteException, BizException {
        try {
            return func.apply();
        } catch (Exception e) {
            // TODO REVIEW LINWEI DONE_he.lang 把异常描述信息加入到操作日志的描述里面去
            LogUtil.addFailOperationLog(type, LogUtil.buildFailOperationLogDesc(desc + e.getLocalizedMessage()), systemName);
            throw e;
        }
    }

    private static void addFailOperationLog(String operationType, String logDesc, String systemName) {
        SimpleLogServer.operation(MODULE_NAME, TRSLogUserUtil.getLogUser(), operationType, logDesc, systemName);
    }

    /**
     * 构造性能日志记录器
     * @param type
     * @param desc
     * @return
     */
    public static PerformanceLogRecorder newPerformanceRecorder(String type, String desc) {
        return new PerformanceLogRecorder(type, desc);
    }

    /**
     * 性能日志记录器
     */
    public static class PerformanceLogRecorder {

        private String type;
        private String desc;
        private Date startDate;

        // 超时时间
        private static final long LIMIT = 1000;

        public PerformanceLogRecorder(String type, String desc) {
            this.type = type;
            this.desc = desc;
            this.startDate = new Date();
        }

        public void record() {
            long spendTime = new Date().getTime() - startDate.getTime();
            if (spendTime > LIMIT) {
                addElapseLog(type, desc, spendTime);
            }
        }
    };

    /**
     * 构造参数在日志中的描述记录
     * @param params, 参数，以 参数名,参数值,参数名,数参值,... 的格式传入
     * @return
     */
    public static String paramsToLogString(Object ... params) {

        if (params.length % 2 != 0) {
            throw new IllegalArgumentException("参数不成对！");
        }

        StringBuilder result = new StringBuilder();
        result.append("[");
        for (int i = 0; i < params.length; i = i + 2) {
            result.append(params[i]).append("=").append(params[i+1]);
            if (i+2 < params.length) {
                result.append(",");
            }
        }
        result.append("]");
        return result.toString();
    }
}
