package com.trs.gov.kpi.utils;

import com.trs.gov.kpi.constant.ErrorType;
import com.trs.gov.kpi.constant.OperationType;
import com.trs.gov.kpi.entity.LocalUser;
import com.trs.gov.kpi.entity.exception.BizException;
import com.trs.gov.kpi.entity.exception.BizRuntimeException;
import com.trs.gov.kpi.ids.ContextHelper;
import com.trs.gov.kpi.service.impl.outer.SiteApiServiceImpl;
import com.trs.mlf.simplelog.LogUser;
import com.trs.mlf.simplelog.SimpleLogServer;
import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;

/**
 * trs日志工具类
 * Created by he.lang on 2017/7/26.
 */
@Slf4j
public class TRSLogUserUtil {

    public static void main(String[] args) {
        LogUtil.addErrorLog(OperationType.REQUEST, ErrorType.REQUEST_FAILED, "siteId[1]", new BizException());
        LogUtil.addOperationLog(OperationType.QUERY, "查询栏目", LogUtil.getSiteNameForLog(new SiteApiServiceImpl(), 1));
    }

    private TRSLogUserUtil() {
    }

    public static LogUser getLogUser() {
        LocalUser localUser;
        try {
            localUser = ContextHelper.getLoginUser();
        } catch (BizRuntimeException e) {
            return getSystemUser();
        }

        StringBuilder buffer = new StringBuilder();
        if (localUser.getGroups() != null && !localUser.getGroups().isEmpty()) {
            for (int i = 0; i < localUser.getGroups().size(); i++) {
                Map map = localUser.getGroups().get(i);
                if (map.get("GNAME") == null) {
                    continue;
                }
                buffer.append(map.get("GNAME"));
                buffer.append(",");
            }
        }
        String groupNames = "";
        if (buffer.length() != 0) {
            groupNames = buffer.deleteCharAt(buffer.lastIndexOf(",")).toString();
        }
        if (!"".equals(groupNames)) {
            return new LogUser(localUser.getUserName(), localUser.getTrueName(), localUser.getLastLoginIP(), groupNames);
        }
        return new LogUser(localUser.getUserName(), localUser.getTrueName(), localUser.getLastLoginIP());
    }

    private static LogUser getSystemUser() {
        String ip;
        try {
            ip = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            ip = "";
        }
        return new LogUser("SYSTEM", "SYSTEM", ip);
    }
}
