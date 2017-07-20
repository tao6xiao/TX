package com.trs.gov.kpi.utils;

import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.gov.kpi.entity.outerapi.Channel;
import com.trs.gov.kpi.entity.outerapi.Site;
import com.trs.gov.kpi.service.outer.SiteApiService;
import org.junit.Test;

import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;

/**
 * Created by ranwei on 2017/6/13.
 */
public class ResultCheckUtilTest {

    @Test
    public void getChannelName_ChannelId_Null() throws Exception {
        MockSiteApiService siteApiService = new MockSiteApiService();
        Integer channelId = null;
        assertEquals("", ResultCheckUtil.getChannelName(channelId, siteApiService));
    }

    @Test
    public void getChannelName_Channel_Null() throws Exception {
        MockSiteApiService siteApiService = new MockSiteApiService();
        Integer channelId = 1;
        assertEquals("", ResultCheckUtil.getChannelName(channelId, siteApiService));
    }

    @Test
    public void getChannelName_Normal() throws Exception {
        MockSiteApiService siteApiService = new MockSiteApiService();
        Integer channelId = 12;
        assertEquals("电影", ResultCheckUtil.getChannelName(channelId, siteApiService));
    }


    private class MockSiteApiService implements SiteApiService {

        private Channel chnl = new Channel();

        @Override
        public Site getSiteById(int siteId, String userName) throws RemoteException {
            return null;
        }

        @Override
        public List<Channel> getChildChannel(int siteId, int parentId, String userName) throws RemoteException {
            return null;
        }

        @Override
        public Channel getChannelById(int channelId, String userName) throws RemoteException {
            if (channelId == 12) {
                chnl.setChnlDesc("电影");
            }
            return chnl;
        }

        @Override
        public String getChannelPublishUrl(String userName, int siteId, int channelId) throws RemoteException {
            return null;
        }

        @Override
        public Set<Integer> getAllChildChnlIds(String userName, int siteId, int channelId, Set<Integer> chnlIdSet) throws RemoteException {
            return null;
        }

        @Override
        public Set<Integer> getAllLeafChnlIds(String userName, int siteId, int channelId, Set<Integer> chnlIdSet) throws RemoteException {
            return null;
        }

        @Override
        public List<Integer> findChnlIds(String userName, int siteId, String chnlName) throws RemoteException {
            return null;
        }

        @Override
        public List<Integer> findChnlIdsByDepartment(String userName, List<Integer> siteIds, String departmentName) throws RemoteException {
            return null;
        }

        @Override
        public Channel findChannelByUrl(String userName, String url, int siteId) throws RemoteException {
            return null;
        }
    }

}