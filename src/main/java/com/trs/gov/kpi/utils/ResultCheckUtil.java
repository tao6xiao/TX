package com.trs.gov.kpi.utils;

import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.gov.kpi.entity.outerapi.Channel;
import com.trs.gov.kpi.entity.outerapi.Dept;
import com.trs.gov.kpi.entity.outerapi.Site;
import com.trs.gov.kpi.service.outer.DeptApiService;
import com.trs.gov.kpi.service.outer.SiteApiService;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by ranwei on 2017/6/12.
 */
@Slf4j
public class ResultCheckUtil {

    private ResultCheckUtil() {

    }

    public static String getChannelName(Integer channelId, SiteApiService siteApiService) {
        if (channelId == null) {
            return "";
        }
        Channel channel = null;
        try {
            channel = siteApiService.getChannelById(channelId, null);
        } catch (RemoteException e) {
            log.error("", e);
        }
        return checkChannelName(channel);
    }

    private static String checkChannelName(Channel channel) {
        if (channel == null || channel.getChnlDesc() == null) {
            return "";
        }
        return channel.getChnlDesc();
    }

    public static String getSiteName(Integer siteId, SiteApiService siteApiService) {
        if(siteId == null){
            return "";
        }
        try {
            final Site site = siteApiService.getSiteById(siteId, null);
            if (site != null) {
                return site.getSiteName();
            } else {
                return "";
            }
        } catch (RemoteException e) {
            log.error("", e);
            return "站点[id=" + siteId + "]";
        }
    }

    public static String getDeptName(Integer deptId, DeptApiService deptApiService) {
        if(deptId == null){
            return "";
        }
        try {
            Dept dept = deptApiService.findDeptById("", deptId);
            if (dept != null) {
                return dept.getGName();
            } else {
                return "";
            }
        } catch (RemoteException e) {
            log.error("", e);
            return "部门[id=" + deptId + "]";
        }
    }
}
