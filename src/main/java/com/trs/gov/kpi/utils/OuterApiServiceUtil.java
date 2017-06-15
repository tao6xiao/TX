package com.trs.gov.kpi.utils;

import com.squareup.okhttp.Request;

import java.util.Iterator;
import java.util.Map;

/**
 * Created by linwei on 2017/5/23.
 */
public class OuterApiServiceUtil {

    private OuterApiServiceUtil(){
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
                    url.append("&").append(paramKey).append("=").append(params.get(paramKey));
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
}
