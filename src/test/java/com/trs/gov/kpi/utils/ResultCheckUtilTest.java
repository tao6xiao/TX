package com.trs.gov.kpi.utils;

import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.gov.kpi.entity.outerapi.Channel;
import com.trs.gov.kpi.entity.outerapi.Site;
import com.trs.gov.kpi.service.outer.SiteApiService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;

/**
 * Created by ranwei on 2017/6/13.
 */
@RunWith(SpringRunner.class)
public class ResultCheckUtilTest {

    @MockBean
    private SiteApiService siteApiService;


    @Test
    public void getChannelName_ChannelId_Null() throws Exception {
        given(this.siteApiService.getChannelById(0, null)).willReturn(null);
        assertEquals("", ResultCheckUtil.getChannelName(null, this.siteApiService));
    }

    @Test
    public void getChannelName_Channel_Null() throws Exception {
        Integer channelId = 1;
        given(this.siteApiService.getChannelById(1, null)).willReturn(null);
        assertEquals("", ResultCheckUtil.getChannelName(channelId, siteApiService));
    }

    @Test
    public void getChannelName_Normal() throws Exception {
        Integer channelId = 12;
        Channel resultChannel = new Channel();
        resultChannel.setChannelId(channelId);
        resultChannel.setChnlDesc("电影");
        given(this.siteApiService.getChannelById(channelId, null)).willReturn(resultChannel);
        assertEquals(resultChannel.getChnlDesc(), ResultCheckUtil.getChannelName(channelId, siteApiService));
    }
}