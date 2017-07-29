package com.trs.gov.kpi.utils;

import com.trs.gov.kpi.constant.OperationType;
import com.trs.gov.kpi.entity.exception.BizException;
import com.trs.gov.kpi.entity.exception.BizRuntimeException;
import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.mlf.simplelog.LogUser;
import com.trs.mlf.simplelog.SimpleLogServer;
import lombok.extern.slf4j.Slf4j;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * 用于添加系统日志和获取异常信息的工具类
 * Created by he.lang on 2017/7/28.
 */
@Slf4j
public class LogUtil {
    private static final String ERROR_PROMPT = "Error information: ";

    private LogUtil() {
    }

    /**
     * 获取Throwable堆栈信息
     *
     * @param t
     * @return
     */
    private static String getStackTrace(Throwable t) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);

        try {
            t.printStackTrace(pw);
            return sw.toString();
        } finally {
            pw.close();
        }
    }

    /**
     * 添加系统日志
     *
     * @param message
     * @param ex
     */
    public static void addSystemLog(String message, Throwable ex) {
        String desc = "";
        StringBuilder builder = new StringBuilder();
        if((message != null && !"".equals(message.trim())) || ex != null){
            builder.append(ERROR_PROMPT);
        }
        if (message != null && !"".equals(message.trim())) {
            builder.append(message);
            builder.append("\n");
        }
        if (ex != null) {
            builder.append(getStackTrace(ex));
        }
        if (builder.length() != 0) {
            desc = builder.toString();
        }
        addLog(desc);
    }

    private static void addLog(String desc) {
        try {
            SimpleLogServer.getInstance(TRSLogUserUtil.getLogUser()).system(OperationType.REQUEST, "请求失败", desc).error();
        } catch (BizException | RemoteException | BizRuntimeException e) {
            log.error("", e);
            SimpleLogServer.getInstance(new LogUser()).system(OperationType.REQUEST, "请求失败", desc).error();
            if (e.getMessage() != null && !"".equals(e.getMessage().trim())) {
                SimpleLogServer.getInstance(new LogUser()).system(OperationType.REQUEST, "请求失败", ERROR_PROMPT + e.getMessage() + getStackTrace(e)).error();
            } else {
                SimpleLogServer.getInstance(new LogUser()).system(OperationType.REQUEST, "请求失败", getStackTrace(e)).error();
            }
        }
    }

    /**
     * 添加系统日志，无异常日志
     *
     * @param message
     */
    public static void addSystemLog(String message) {
        String desc = "";
        if (message != null && !"".equals(message.trim())) {
            desc = ERROR_PROMPT + message + "\n";
        }
        addLog(desc);
    }
}
