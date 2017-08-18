package com.trs.gov.kpi.service.impl.outer;

import com.alibaba.fastjson.JSON;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Response;
import com.trs.gov.kpi.constant.ErrorType;
import com.trs.gov.kpi.constant.OperationType;
import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.gov.kpi.entity.outerapi.ApiPageData;
import com.trs.gov.kpi.entity.outerapi.ApiResult;
import com.trs.gov.kpi.entity.outerapi.Channel;
import com.trs.gov.kpi.entity.outerapi.Site;
import com.trs.gov.kpi.service.outer.SiteApiService;
import com.trs.gov.kpi.utils.LogUtil;
import com.trs.gov.kpi.utils.OuterApiServiceUtil;
import com.trs.gov.kpi.utils.OuterApiUtil;
import com.trs.gov.kpi.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

/**
 * Created by linwei on 2017/5/18.
 */
@Slf4j
@Service
public class SiteApiServiceImpl implements SiteApiService {

    public static final String SITE_ID = "SiteId";

    @Value("${service.outer.editcenter.url}")
    private String editCenterServiceUrl;

    private static final String SERVICE_NAME_SITE = "gov_site";

    private static final String SERVICE_NAME_CHANNEL = "gov_channel";

    private static final String FAIL_GET_SITE = "获取站点失败！";

    @Override
    public Site getSiteById(int siteId, String userName) throws RemoteException {

        try {
            Map<String, String> params = new HashMap<>();
            params.put(SITE_ID, String.valueOf(siteId));

            OkHttpClient client = new OkHttpClient();
            Response response = client.newCall(
                    OuterApiServiceUtil.buildRequest("findSiteById", userName,
                            params, SERVICE_NAME_SITE, editCenterServiceUrl)).execute();

            if (response.isSuccessful()) {
                ApiResult result = OuterApiUtil.getValidResult(response, "获取站点");
                if (StringUtil.isEmpty(result.getData())) {
                    log.error("site[id=" + siteId + "] result is empty, response: " + response);
                    throw new RemoteException(FAIL_GET_SITE + "，结果不存在！");
                }
                return JSON.parseObject(result.getData(), Site.class);
            } else {
                log.error("failed to get site[id=" + siteId + "], error: " + response);
                throw new RemoteException(FAIL_GET_SITE + "，返回：" + response);
            }
        } catch (IOException e) {
            log.error("failed get site[id=" + siteId + "]", e);
            LogUtil.addErrorLog(OperationType.REQUEST, ErrorType.REQUEST_FAILED, "failed get site[id=" + siteId + "]", e);
            throw new RemoteException(FAIL_GET_SITE, e);
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
                    OuterApiServiceUtil.buildRequest("queryChildrenChannelsOnEditorCenter", userName,
                            params, SERVICE_NAME_SITE, editCenterServiceUrl)).execute();

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
                throw new RemoteException("获取子栏目失败！返回：" + response);
            }
        } catch (IOException e) {
            log.error("", e);
            LogUtil.addErrorLog(OperationType.REQUEST, ErrorType.REQUEST_FAILED, "find channel child failed", e);
            throw new RemoteException("获取子栏目失败！", e);
        }
    }

    @Override
    public Channel getChannelById(int channelId, String userName) throws RemoteException {

        try {
            Map<String, String> params = new HashMap<>();
            params.put("ChannelId", String.valueOf(channelId));

            OkHttpClient client = new OkHttpClient();
            Response response = client.newCall(OuterApiServiceUtil.buildRequest("findChannelById", userName,
                    params, SERVICE_NAME_SITE, editCenterServiceUrl)).execute();

            return responseManager("findChannelById", response);
        } catch (IOException e) {
            log.error("failed getChannelById", e);
            LogUtil.addErrorLog(OperationType.REQUEST, ErrorType.REQUEST_FAILED, "failed getChannelById", e);
            throw new RemoteException("获取栏目失败！", e);
        }
    }

    @Override
    public List<Integer> findChannelPath(int channelId, String userName) throws RemoteException {
        try {
            Map<String, String> params = new HashMap<>();
            params.put("CHANNELID", String.valueOf(channelId));

            OkHttpClient client = new OkHttpClient();
            Response response = client.newCall(OuterApiServiceUtil.buildRequest("findChannelPath", userName,
                    params, SERVICE_NAME_CHANNEL, editCenterServiceUrl)).execute();

            if (response.isSuccessful()) {
                ApiResult result = OuterApiUtil.getValidResult(response, "获取栏目路径");
                if (StringUtil.isEmpty(result.getData())) {
                    return new ArrayList<>();
                }

                List<Integer> ids = new ArrayList<>();
                String[] strIds = result.getData().split(",");
                for (String id : strIds) {
                    ids.add(Integer.valueOf(id));
                }
                return ids;
            } else {
                log.error("failed to findChannelPath, chnnelId=" + channelId + ", error: " + response);
                throw new RemoteException("获取栏目路径失败！[chnnelId=" + channelId + "]");
            }
        } catch (IOException e) {
            String errorInfo = "failed findChannelPath! [chnnelId=" + channelId + "]";
            log.error(errorInfo, e);
            LogUtil.addErrorLog(OperationType.REQUEST, ErrorType.REQUEST_FAILED, errorInfo, e);
            throw new RemoteException("获取栏目路径失败！[chnnelId=" + channelId + "]", e);
        }
    }

    @Override
    public String getChannelPublishUrl(String userName, int siteId, int channelId) throws RemoteException {
        try {
            Map<String, String> params = new HashMap<>();
            params.put(SITE_ID, String.valueOf(siteId));
            params.put("ChannelId", String.valueOf(channelId));
            OkHttpClient client = new OkHttpClient();
            Response response = client.newCall(OuterApiServiceUtil.buildRequest("getSiteOrChannelPubUrl", userName,
                    params, SERVICE_NAME_SITE, editCenterServiceUrl)).execute();

            if (response.isSuccessful()) {
                ApiResult result = OuterApiUtil.getValidResult(response, "获取栏目发布地址");
                return result.getData();
            } else {
                log.error("failed to get channel publish url, error: " + response);
                throw new RemoteException("获取栏目发布地址失败！");
            }
        } catch (IOException e) {
            log.error("failed to get channel publish url", e);
            LogUtil.addErrorLog(OperationType.REQUEST, ErrorType.REQUEST_FAILED, "failed to get channel publish url", e);
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
        try {
            Map<String, String> params = new HashMap<>();
            params.put(SITE_ID, String.valueOf(siteId));
            params.put("NAMEORDESC", chnlName);

            OkHttpClient client = new OkHttpClient();
            Response response = client.newCall(OuterApiServiceUtil.buildRequest("queryChannelIdsByNameOrDesc", userName,
                    params, SERVICE_NAME_CHANNEL, editCenterServiceUrl)).execute();

            if (response.isSuccessful()) {
                ApiResult result = OuterApiUtil.getValidResult(response, "获取栏目ids");
                if (StringUtil.isEmpty(result.getData())) {
                    return new ArrayList<>();
                }
                return JSON.parseArray(result.getData(), Integer.class);
            } else {
                log.error("failed to findChnlIds, error: " + response);
                throw new RemoteException("通过栏目名称获取栏目编号失败！");
            }
        } catch (IOException e) {
            log.error("failed findChnlIds", e);
            LogUtil.addErrorLog(OperationType.REQUEST, ErrorType.REQUEST_FAILED, "failed findChnlIds", e);
            throw new RemoteException("通过栏目名称获取栏目编号失败！", e);
        }
    }

    @Override
    public List<Integer> findChnlIdsByDepartment(String userName, List<Integer> siteIds, String departmentName) throws RemoteException {
        // TODO 该方法未实现， 需要确认一下是否使用了
        return new ArrayList<>();
    }

    @Override
    public Channel findChannelByUrl(String userName, String url, int siteId) throws RemoteException {
        try {
            Map<String, String> params = new HashMap<>();
            params.put(SITE_ID, String.valueOf(siteId));
            params.put("URL", url);

            OkHttpClient client = new OkHttpClient();
            Response response = client.newCall(OuterApiServiceUtil.buildRequest("findChannelByUrl", userName,
                    params, SERVICE_NAME_SITE, editCenterServiceUrl)).execute();

            return responseManager("findChannelByUrl", response);
        } catch (IOException e) {
            log.error("failed findChannelByUrl", e);
            LogUtil.addErrorLog(OperationType.REQUEST, ErrorType.REQUEST_FAILED, "failed findChannelByUrl", e);
            throw new RemoteException("通过url获取栏目！", e);
        }
    }

    private Channel responseManager(String method, Response response) throws IOException, RemoteException {
        if (response.isSuccessful()) {
            ApiResult result = OuterApiUtil.getValidResult(response, "获取栏目");
            if (StringUtil.isEmpty(result.getData())) {
                return null;
            }
            return JSON.parseObject(result.getData(), Channel.class);
        } else {
            log.error("failed to " + method + ", error: " + response);
            throw new RemoteException("通过url获取栏目！");
        }
    }
}
