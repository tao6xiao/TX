/*
 *	RemoteAddrUtil.java
 *	History				Who				What
 *	2010-1-4 		 	wenyh			Created.
 */
package com.trs.gov.kpi.utils;

import com.trs.gov.kpi.constant.ErrorType;
import com.trs.gov.kpi.constant.OperationType;

import javax.servlet.http.HttpServletRequest;

/**
 * Title: TRS 内容协作平台（TRS WCM）<BR>
 * Description: <BR>
 * 获取IP地址工具类 <BR>
 * Copyright: Copyright (c) 2004-2010 北京拓尔思信息技术股份有限公司<BR>
 * Company: 北京拓尔思信息技术股份有限公司(www.trs.com.cn)<BR>
 * 
 * @author wenyh
 * @version 1.0
 * 
 */

public class RemoteAddrUtil {

    private RemoteAddrUtil(){}

    public static String getRemoteAddr(HttpServletRequest req) {
        String addr = req.getHeader("Cdn-Src-Ip");// 首先处理CDN的情况.
        if (isIPAddress(addr)) {
            return addr;
        }

        addr = req.getHeader("X-Forwarded-For");// 反向代理.
        while (addr != null) {
            // 获取X-Forwarded-For串的第一个有效地址，但是如果用户是在单位内部使用代理服务器上网，则获得了其局域网地址
            String address = null;
            int iFirstDot = addr.indexOf(',');
            if (iFirstDot > 0) {
                address = addr.substring(0, iFirstDot).trim();
                addr = addr.substring(iFirstDot + 1);
            } else {
                address = addr;
                addr = null;
            }

            if (isIPAddress(address)) {
                return address;
            }
        }

        return req.getRemoteAddr();
    }

    public static final boolean isIPAddress(String address) {
        if (address != null) {
            String addr = address.trim();
            String pattern =
                    "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                            "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                            "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                            "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";
            return addr.matches(pattern);
        }
        return false;
    }
}
