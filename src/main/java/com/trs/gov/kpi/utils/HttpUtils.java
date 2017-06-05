package com.trs.gov.kpi.utils;

import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import org.apache.http.client.utils.URIBuilder;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Map;

/**
 * Created by wangxuan on 2017/5/15.
 */
public class HttpUtils {

    private HttpUtils() {

    }

    public static String getHttpInfo(String url, Map<String, String> params) throws URISyntaxException, IOException {

        OkHttpClient httpClient = new OkHttpClient();
        URIBuilder uriBuilder = new URIBuilder(url);
        for (Map.Entry<String, String> param : params.entrySet()) {

            uriBuilder.addParameter(param.getKey(), param.getValue());
        }

        Request request = new Request.Builder()
                .url(uriBuilder.build().toURL())
                .build();

        Response response = httpClient.newCall(request).execute();
        return response.body().string();
    }

    public static String postHttpInfo(String url, Map<String, String> params) throws IOException {

        OkHttpClient httpClient = new OkHttpClient();

        FormEncodingBuilder form = new FormEncodingBuilder();
        for(Map.Entry<String, String> param : params.entrySet()) {

            form.add(param.getKey(), param.getValue());
        }
        Request request = new Request.Builder()
                .url(url)
                .post(form.build())
                .build();
        Response response = httpClient.newCall(request).execute();
        return response.body().string();
    }
}
