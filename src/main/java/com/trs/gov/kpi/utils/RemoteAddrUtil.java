/*
 *	RemoteAddrUtil.java
 *	History				Who				What
 *	2010-1-4 		 	wenyh			Created.
 */
package com.trs.gov.kpi.utils;

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

    public final static boolean isIPAddress(String s) {
        if (s != null) {
            s = s.trim();
            int dot1 = s.indexOf('.');
            if (dot1 <= 0) {
                return false;
            }
            int temp;
            try {
                temp = Integer.parseInt(s.substring(0, dot1++));
                if (temp < 0 || temp > 255) {
                    return false;
                }
            } catch (Exception ex) {
                LogUtil.addSystemLog("", ex);
                return false;
            }

            int dot2 = s.indexOf('.', dot1);
            if (dot2 <= 0) {
                return false;
            }
            try {
                temp = Integer.parseInt(s.substring(dot1, dot2++));
                if (temp < 0 || temp > 255) {
                    return false;
                }
            } catch (Exception ex) {
                LogUtil.addSystemLog("", ex);
                return false;
            }

            int dot3 = s.indexOf('.', dot2);
            if (dot3 <= 0) {
                return false;
            }
            try {
                temp = Integer.parseInt(s.substring(dot2, dot3++));
                if (temp < 0 || temp > 255) {
                    return false;
                }
            } catch (Exception ex) {
                LogUtil.addSystemLog("", ex);
                return false;
            }

            try {
                temp = Integer.parseInt(s.substring(dot3));
                if (temp < 0 || temp > 255) {
                    return false;
                }
            } catch (Exception ex) {
                LogUtil.addSystemLog("", ex);
                return false;
            }

            return true;
        }
        return false;
    }
}
