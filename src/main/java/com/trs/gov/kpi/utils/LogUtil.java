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
 * 用于获取异常信息的工具类
 * Created by he.lang on 2017/7/28.
 */
@Slf4j
public class LogUtil {
    private LogUtil() {
    }

    /**
     * 获取日志堆栈信息
     *
     * @param t
     * @return
     */
    public static String getStackTrace(Throwable t) {
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
     * @param ex
     */
    public static void addSystemLog(String message, Throwable ex) {
        String desc = "Error information: " + message + "\n" + LogUtil.getStackTrace(ex);
        try {
            SimpleLogServer.getInstance(TRSLogUserUtil.getLogUser()).system(OperationType.REQUEST, "请求失败", desc).error();
        } catch (BizException | RemoteException | BizRuntimeException e) {
            log.error("", e);
            SimpleLogServer.getInstance(new LogUser()).system(OperationType.REQUEST, "请求失败", desc).error();
            SimpleLogServer.getInstance(new LogUser()).system(OperationType.REQUEST, "请求失败", "Error information: " + message + "\n" + LogUtil.getStackTrace(e)).error();
        }
    }
}
