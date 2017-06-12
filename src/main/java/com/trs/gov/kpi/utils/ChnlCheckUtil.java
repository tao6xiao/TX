package com.trs.gov.kpi.utils;

import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.gov.kpi.entity.outerapi.Channel;
import com.trs.gov.kpi.service.outer.SiteApiService;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by ranwei on 2017/6/12.
 */
@Slf4j
public class ChnlCheckUtil {

    private ChnlCheckUtil() {

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
        if (channel == null || channel.getChnlName() == null) {
            return "";
        }
        return channel.getChnlName();
    }
}
