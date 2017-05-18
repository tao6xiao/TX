package com.trs.gov.kpi.controller;

import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.gov.kpi.entity.outerapi.Channel;
import com.trs.gov.kpi.entity.outerapi.Site;
import com.trs.gov.kpi.service.outer.SiteApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by linwei on 2017/5/18.
 */
@RestController
@RequestMapping("/gov/kpi/test")
public class TestController {

    @Autowired
    private SiteApiService siteService;

    @RequestMapping(value = "/site",method = RequestMethod.GET)
    @ResponseBody
    public Site getSite(Integer siteId) throws RemoteException {
        return siteService.getSiteById(siteId, "");
    }

    @RequestMapping(value = "/site/channel",method = RequestMethod.GET)
    @ResponseBody
    public Channel getChannel(Integer channelId) throws RemoteException {
        return siteService.getChannelById(channelId, "");
    }

    @RequestMapping(value = "/site/channels",method = RequestMethod.GET)
    @ResponseBody
    public List<Channel> getChannels(Integer siteId, Integer parentId) throws RemoteException {
        return siteService.getChildChannel(siteId, parentId, "");
    }
}
