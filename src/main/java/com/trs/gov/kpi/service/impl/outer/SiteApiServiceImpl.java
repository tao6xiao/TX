package com.trs.gov.kpi.service.impl.outer;

import com.alibaba.fastjson.JSON;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.gov.kpi.entity.outerapi.ApiPageData;
import com.trs.gov.kpi.entity.outerapi.ApiResult;
import com.trs.gov.kpi.entity.outerapi.Channel;
import com.trs.gov.kpi.entity.outerapi.Site;
import com.trs.gov.kpi.service.outer.SiteApiService;
import com.trs.gov.kpi.utils.OuterApiServiceUtil;
import com.trs.gov.kpi.utils.OuterApiUtil;
import com.trs.gov.kpi.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

import static com.trs.gov.kpi.utils.OuterApiServiceUtil.newServiceRequestBuilder;

/**
 * Created by linwei on 2017/5/18.
 */
@Slf4j
@Service
public class SiteApiServiceImpl implements SiteApiService {

    public static final String SITE_ID = "SiteId";

    @Value("${service.outer.editcenter.url}")
    private String editCenterServiceUrl;

    private static final String SERVICE_NAME = "gov_site";

    @Override
    public Site getSiteById(int siteId, String userName) throws RemoteException {

        try {
            Map<String, String> params = new HashMap<>();
            params.put(SITE_ID, String.valueOf(siteId));

            OkHttpClient client = new OkHttpClient();
            Response response = client.newCall(
                    buildRequest("findSiteById", userName, params)).execute();

            if (response.isSuccessful()) {
                ApiResult result = OuterApiUtil.getValidResult(response, "获取站点");
                if (StringUtil.isEmpty(result.getData())) {
                    return null;
                }
                return JSON.parseObject(result.getData(), Site.class);
            } else {
                log.error("failed to getSiteById, error: " + response);
                throw new RemoteException("获取站点失败！");
            }
        } catch (IOException e) {
            log.error("failed getSiteById", e);
            throw new RemoteException("获取站点失败！", e);
        }
    }

    @Override
    public List<Channel> getChildChannel(int siteId, int parentId, String userName) throws RemoteException {

        try {
            Map<String, String> params = new HashMap<>();
            params.put(SITE_ID, String.valueOf(siteId));
            params.put("ParentChannelId", String.valueOf(parentId));
            OkHttpClient client = new OkHttpClient();
            Response response = client.newCall(
                    buildRequest("queryChildrenChannelsOnEditorCenter", userName, params)).execute();

            if (response.isSuccessful()) {
                ApiResult result = OuterApiUtil.getValidResult(response, "获取子栏目");

                if (StringUtil.isEmpty(result.getData())) {
                    return new ArrayList<>();
                }

                ApiPageData pageData = JSON.parseObject(result.getData(), ApiPageData.class);
                if (StringUtil.isEmpty(result.getData())) {
                    return new ArrayList<>();
                }

                return JSON.parseArray(pageData.getData(), Channel.class);
            } else {
                log.error("error: " + response);
                throw new RemoteException("获取子栏目失败！");
            }
        } catch (IOException e) {
            log.error("", e);
            throw new RemoteException("获取子栏目失败！", e);
        }
    }

    @Override
    public Channel getChannelById(int channelId, String userName) throws RemoteException {

        try {
            Map<String, String> params = new HashMap<>();
            params.put("ChannelId", String.valueOf(channelId));

            OkHttpClient client = new OkHttpClient();
            Response response = client.newCall(buildRequest("findChannelById", userName, params)).execute();

            if (response.isSuccessful()) {
                ApiResult result = OuterApiUtil.getValidResult(response, "获取栏目");
                if (StringUtil.isEmpty(result.getData())) {
                    return null;
                }
                return JSON.parseObject(result.getData(), Channel.class);
            } else {
                log.error("failed to getChannelById, error: " + response);
                throw new RemoteException("获取栏目失败！");
            }
        } catch (IOException e) {
            log.error("failed getChannelById", e);
            throw new RemoteException("获取栏目失败！", e);
        }
    }

    @Override
    public String getChannelPublishUrl(String userName, int siteId, int channelId) throws RemoteException {
        try {
            Map<String, String> params = new HashMap<>();
            params.put(SITE_ID, String.valueOf(siteId));
            params.put("ChannelId", String.valueOf(channelId));
            OkHttpClient client = new OkHttpClient();
            Response response = client.newCall(buildRequest("getSiteOrChannelPubUrl", userName, params)).execute();

            if (response.isSuccessful()) {
                ApiResult result = OuterApiUtil.getValidResult(response, "获取栏目发布地址");
                return result.getData();
            } else {
                log.error("failed to get channel publish url, error: " + response);
                throw new RemoteException("获取栏目发布地址失败！");
            }
        } catch (IOException e) {
            log.error("failed to get channel publish url", e);
            throw new RemoteException("获取栏目发布地址失败！", e);
        }
    }

    @Override
    public Set<Integer> getAllChildChnlIds(String userName, int siteId, int channelId, Set<Integer> chnlIdSet) throws RemoteException {

        List<Channel> channels = getChildChannel(siteId, channelId, userName);

        if (channels.isEmpty()) {
            return chnlIdSet;
        }

        for (Channel channel : channels) {
            chnlIdSet.add(channel.getChannelId());
            if (channel.isHasChildren()) {
                getAllChildChnlIds(userName, siteId, channel.getChannelId(), chnlIdSet);
            }
        }
        return chnlIdSet;
    }

    @Override
    public Set<Integer> getAllLeafChnlIds(String userName, int siteId, int channelId,
                                          Set<Integer> chnlIdSet) throws RemoteException {
        List<Channel> channels = getChildChannel(siteId, channelId, userName);

        if (channels.isEmpty()) {
            return chnlIdSet;
        }

        for (Channel channel : channels) {
            if (channel.isHasChildren()) {
                getAllChildChnlIds(userName, siteId, channel.getChannelId(), chnlIdSet);
            } else {
                chnlIdSet.add(channel.getChannelId());
            }
        }
        return chnlIdSet;
    }

    @Override
    public List<Integer> findChnlIds(String userName, int siteId, String chnlName) throws RemoteException {
        // TODO wait for edit center
        return new ArrayList<>();
    }

    @Override
    public List<Integer> findChnlIdsByDepartment(String userName, List<Integer> siteIds, String departmentName) throws RemoteException {
        // TODO wait for edit center
        return new ArrayList<>();
    }

    private Request buildRequest(String methodName, String userName, Map<String, String> params) {
        OuterApiServiceUtil.addUserNameParam(userName, params);
        return newServiceRequestBuilder()
                .setUrlFormat("%s/gov/opendata.do?serviceId=%s&methodname=%s")
                .setServiceUrl(editCenterServiceUrl)
                .setServiceName(SERVICE_NAME)
                .setMethodName(methodName)
                .setParams(params).build();
    }
}
