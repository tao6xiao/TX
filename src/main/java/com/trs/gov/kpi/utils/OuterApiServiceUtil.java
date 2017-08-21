package com.trs.gov.kpi.utils;

import com.squareup.okhttp.Request;
import com.trs.gov.kpi.entity.exception.BizException;
import com.trs.gov.kpi.entity.outerapi.Site;
import lombok.extern.slf4j.Slf4j;

import java.util.Iterator;
import java.util.Map;

/**
 * Created by linwei on 2017/5/23.
 */
@Slf4j
public class OuterApiServiceUtil {

    private OuterApiServiceUtil() {
    }

    public static ServiceRequestBuilder newServiceRequestBuilder() {
        return new ServiceRequestBuilder();
    }

    public static class ServiceRequestBuilder {

        private String serviceUrl;
        private String urlFormat;
        private String serviceName;
        private String methodName;
        private Map<String, String> params;

        protected ServiceRequestBuilder() {
        }

        public ServiceRequestBuilder setUrlFormat(String urlFormat) {
            this.urlFormat = urlFormat;
            return this;
        }

        public ServiceRequestBuilder setServiceName(String serviceName) {
            this.serviceName = serviceName;
            return this;
        }

        public ServiceRequestBuilder setMethodName(String methodName) {
            this.methodName = methodName;
            return this;
        }

        public ServiceRequestBuilder setParams(Map<String, String> params) {
            this.params = params;
            return this;
        }

        public ServiceRequestBuilder setServiceUrl(String serviceUrl) {
            this.serviceUrl = serviceUrl;
            return this;
        }

        public Request build() {
            StringBuilder url = new StringBuilder(
                    String.format(urlFormat, serviceUrl, serviceName, methodName));
            if (params != null && !params.isEmpty()) {
                Iterator<String> iter = params.keySet().iterator();
                while (iter.hasNext()) {
                    String paramKey = iter.next();
                    url.append("&").append(paramKey).append("=").append(StringUtil.encodeUrlParam(params.get(paramKey)));
                }
            }

            return new Request.Builder()
                    .url(url.toString())
                    .addHeader("Accept", "application/json; charset=utf-8")
                    .build();
        }

    }

    public static void addUserNameParam(String userName, Map<String, String> params) {
        if (StringUtil.isEmpty(userName)) {
            params.put("CurrUserName", "admin");
        } else {
            params.put("CurrUserName", userName);
        }
    }

    public static Request buildRequest(String serviceUrl, String serviceName, Map<String, String> params) {
        StringBuilder url = new StringBuilder(serviceUrl + serviceName);

        if (!params.isEmpty()) {
            url.append("?");
            for (Iterator<Map.Entry<String, String>> it = params.entrySet().iterator(); it.hasNext(); ) {
                Map.Entry<String, String> entry = it.next();
                url.append(entry.getKey()).append("=").append(entry.getValue());
                if (it.hasNext()) {
                    url.append("&");
                }
            }
        }

        return new Request.Builder().url(url.toString()).build();
    }

    public static Request buildRequest(String methodName, String userName,
                                       Map<String, String> params, String serviceName, String editCenterServiceUrl) {
        OuterApiServiceUtil.addUserNameParam(userName, params);
        return newServiceRequestBuilder()
                .setUrlFormat("%s/gov/opendata.do?serviceId=%s&methodname=%s")
                .setServiceUrl(editCenterServiceUrl)
                .setServiceName(serviceName)
                .setMethodName(methodName)
                .setParams(params).build();
    }

    /**
     * 检查站点和url是否存在，存在则返回url
     *
     * @param siteId
     * @param checkSite
     * @return
     */
    public static String checkSiteAndGetUrl(Integer siteId, Site checkSite) throws BizException {
        checkSite(siteId, checkSite);
        String baseUrl = checkSite.getWebHttp();
        if (StringUtil.isEmpty(baseUrl)) {
            log.warn("site[" + siteId + "]'s web http is empty!");
            return null;
        }
        return baseUrl;
    }


    public static void checkSite(Integer siteId, Site checkSite) throws BizException {
        if (checkSite == null) {
            String errorInfo = "site[" + siteId + "] is not exsit!";
            log.error(errorInfo);
            throw new BizException(errorInfo);
        }
    }
}
