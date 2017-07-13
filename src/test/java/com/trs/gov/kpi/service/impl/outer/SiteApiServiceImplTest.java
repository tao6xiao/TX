package com.trs.gov.kpi.service.impl.outer;

import com.squareup.okhttp.Request;
import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.gov.kpi.entity.outerapi.Channel;
import com.trs.gov.kpi.entity.outerapi.Site;
import com.trs.gov.kpi.service.outer.SiteApiService;
import org.junit.Test;

import java.util.*;

import static com.trs.gov.kpi.utils.OuterApiServiceUtil.newServiceRequestBuilder;
import static org.junit.Assert.assertEquals;

/**
 * Created by linwei on 2017/5/18.
 */
public class SiteApiServiceImplTest {

    @Test
    public void getRequestUrl() throws Exception {

        Map<String, String> params = new LinkedHashMap<>();

        params.put("CurrUserName", "testUser");
        params.put("param1", "1");
        params.put("param2", "2");

        Request req = newServiceRequestBuilder()
                .setUrlFormat("%s/gov/opendata.do?serviceId=%s&methodname=%s")
                .setServiceUrl("http://www.baidu.com")
                .setServiceName("testService")
                .setMethodName("testMethod")
//                .setUserName("testUser")
                .setParams(params).build();
        String expectedUrl = "http://www.baidu.com/gov/opendata.do?serviceId=testService&methodname=testMethod&CurrUserName=testUser&param1=1&param2=2";
        assertEquals(expectedUrl, req.urlString());
    }

    @Test
    public void findChnlIds() throws RemoteException {
        MockSiteApiService siteApiService = new MockSiteApiService();
        assertEquals(2, siteApiService.findChnlIds("",1,"新闻").size());

    }

    @Test
    public void findChannelByUrl_null_String() throws RemoteException {
        MockSiteApiService service = new MockSiteApiService();
        Channel channel = service.findChannelByUrl("", "");
        assertEquals(null, channel);
    }

    @Test
    public void findChannelByUrl() throws RemoteException{
        MockSiteApiService service = new MockSiteApiService();
        Channel channel = service.findChannelByUrl("", "www.test.com");
        assertEquals(1, channel.getChannelId());
        assertEquals("test", channel.getChnlName());
    }

    private class MockSiteApiService implements SiteApiService {

        private Channel chnl = new Channel();
        private List<Integer> ids = new ArrayList<>();

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
                chnl.setChnlName("电影");
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
            if("新闻".equals(chnlName)) {
                ids.add(1);
                ids.add(2);
            }
            return ids;
        }

        @Override
        public List<Integer> findChnlIdsByDepartment(String userName, List<Integer> siteIds, String departmentName) throws RemoteException {
            return null;
        }

        @Override
        public Channel findChannelByUrl(String userName, String url) throws RemoteException {
            if("www.test.com".equals(url)){
                Channel channel = new Channel();
                channel.setChannelId(1);
                channel.setChnlName("test");
                return channel;
            }
            return null;
        }
    }

}