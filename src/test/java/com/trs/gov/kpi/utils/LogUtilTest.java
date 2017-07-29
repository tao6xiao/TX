package com.trs.gov.kpi.utils;

import com.trs.gov.kpi.entity.exception.BizException;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by he.lang on 2017/7/29.
 */
public class LogUtilTest {
    @Test
    public void addSystemLog_Exception_Exception_null_Message_null() throws Exception {
        LogUtil.addSystemLog(null, null);
    }

    @Test
    public void addSystemLog_Exception_Message_null() throws Exception {
        LogUtil.addSystemLog(null, new BizException());
        LogUtil.addSystemLog(null, new BizException("信息错误"));
    }

    @Test
    public void addSystemLog_Exception_Exception_null() throws Exception {
        LogUtil.addSystemLog("信息错误", null);
    }

    @Test
    public void addSystemLog_Exception() throws Exception {
        LogUtil.addSystemLog("信息错误123", new BizException("信息错误456"));
    }

    @Test
    public void addSystemLog_Message_null() throws Exception {
        LogUtil.addSystemLog(null);
    }

    @Test
    public void addSystemLog_Message_String_empty() throws Exception {
        LogUtil.addSystemLog("");
        LogUtil.addSystemLog("  ");
    }

    @Test
    public void addSystemLog_Message() throws Exception {
        LogUtil.addSystemLog("信息错误");
    }

}