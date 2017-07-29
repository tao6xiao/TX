package com.trs.gov.kpi.service.impl.outer;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.gov.kpi.entity.outerapi.ApiResult;
import com.trs.gov.kpi.service.outer.ReportApiService;
import com.trs.gov.kpi.utils.LogUtil;
import com.trs.gov.kpi.utils.OuterApiUtil;
import com.trs.gov.kpi.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.trs.gov.kpi.utils.OuterApiServiceUtil.newServiceRequestBuilder;

/**
 * Created by linwei on 2017/6/15.
 */
@Slf4j
@Service
public class ReportApiServiceImpl implements ReportApiService {

    // 统计项元数据名
    private static final String PARAM_REPORT_NAME = "reportName";

    // 开始时间
    private static final String PARAM_BEGIN_DATE = "beginDate";

    // 结束时间
    private static final String PARAM_END_DATE = "endDate";

    // 维度
    private static final String PARAM_FIELDS = "fields";

    // 时间粒度
    private static final String PARAM_GRANULARITY = "granularity";

    @Value("${service.outer.report.url}")
    private String reportServiceUrl;

    @Override
    public String getReport(ReportApiParam param) throws RemoteException {

        try {
            Map<String, String> params = new HashMap<>();
            params.put(PARAM_REPORT_NAME, param.getReportName());
            params.put(PARAM_FIELDS, param.getFields());
            if (!StringUtil.isEmpty(param.getBeginDate())) {
                params.put(PARAM_BEGIN_DATE, param.getBeginDate());
            }
            if (!StringUtil.isEmpty(param.getEndDate())) {
                params.put(PARAM_END_DATE, param.getEndDate());
            }

            if (!StringUtil.isEmpty(param.getGranularity())) {
                params.put(PARAM_GRANULARITY, param.getGranularity());
            }
            params.putAll(param.getOtherParams());

            OkHttpClient client = new OkHttpClient();
            Response response = client.newCall(
                    buildRequest("", params)).execute();

            if (response.isSuccessful()) {
                ApiResult result = OuterApiUtil.getValidResult(response, "获取统计数据");
                if (!result.isOk()) {
                    throw new RemoteException(result.getMsg());
                }
                return result.getData();
            } else {
                log.error("failed to get report, error: " + response);
                throw new RemoteException("获取统计数据失败！");
            }
        } catch (IOException e) {
            log.error("failed get report", e);
            LogUtil.addSystemLog("failed get report", e);
            throw new RemoteException("获取统计数据失败！", e);
        }
    }

    private Request buildRequest(String methodName, Map<String, String> params) {
        return newServiceRequestBuilder()
                .setUrlFormat("%s?%s%s")
                .setServiceUrl(reportServiceUrl)
                .setServiceName("")
                .setMethodName(methodName)
                .setParams(params).build();
    }

}
