package com.trs.gov.kpi.service.impl.outer;

import com.alibaba.fastjson.JSON;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.trs.gov.kpi.constant.ErrorType;
import com.trs.gov.kpi.constant.OperationType;
import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.gov.kpi.entity.outerapi.ApiResult;
import com.trs.gov.kpi.entity.outerapi.Dept;
import com.trs.gov.kpi.service.outer.DeptApiService;
import com.trs.gov.kpi.utils.LogUtil;
import com.trs.gov.kpi.utils.OuterApiServiceUtil;
import com.trs.gov.kpi.utils.OuterApiUtil;
import com.trs.gov.kpi.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.trs.gov.kpi.utils.OuterApiServiceUtil.newServiceRequestBuilder;

/**
 * Created by he.lang on 2017/6/28.
 */
@Slf4j
@Service
public class DeptApiServiceImpl implements DeptApiService {
    @Value("${service.outer.editcenter.url}")
    private String editCenterServiceUrl;

    private static final String SERVICE_NAME = "gov_group";

    @Override
    public Dept findDeptById(String userName, int groupId) throws RemoteException {
        try {
            Map<String, String> params = new HashMap<>();
            params.put("groupId", String.valueOf(groupId));
            OkHttpClient client = new OkHttpClient();
            Response response = client.newCall(
                    buildRequest("findGroupById", userName, params)).execute();

            if (response.isSuccessful()) {
                ApiResult result = OuterApiUtil.getValidResult(response, "获取部门");
                if (StringUtil.isEmpty(result.getData())) {
                    return new Dept();
                }
                if (JSON.parseObject(result.getData(), Dept.class).getGName() == null) {
                    return new Dept();
                }
                return JSON.parseObject(result.getData(), Dept.class);
            } else {
                log.error("failed to findDeptById, error: " + response);
                throw new RemoteException("通过部门id获取部门对象失败！");
            }
        } catch (IOException e) {
            log.error("", e);
            LogUtil.addErrorLog(OperationType.REQUEST, ErrorType.REQUEST_FAILED, "从采编中心获取部门", e);
            throw new RemoteException("获取部门失败！", e);
        }
    }

    @Override
    public List<Integer> queryDeptsByName(String userName, String deptName) throws RemoteException {
        try {
            Map<String, String> params = new HashMap<>();
            if (StringUtil.isEmpty(deptName)) {
                return new ArrayList<>();
            }
            params.put("GNAME", deptName);
            OkHttpClient client = new OkHttpClient();
            Response response = client.newCall(
                    buildRequest("queryAllGroupsByName", userName, params)).execute();

            if (response.isSuccessful()) {
                ApiResult result = OuterApiUtil.getValidResult(response, "获取部门集合");
                if (StringUtil.isEmpty(result.getData())) {
                    return new ArrayList<>();
                }
                List<Dept> deptList = JSON.parseArray(result.getData(), Dept.class);
                List<Integer> idList = new ArrayList<>();
                for (Dept dept : deptList) {
                    idList.add(dept.getGroupId());
                }
                return idList;
            } else {
                log.error("failed to queryDeptsByName, error: " + response);
                throw new RemoteException("通过部门名称获取部门对象集合失败！");
            }
        } catch (IOException e) {
            log.error("", e);
            LogUtil.addErrorLog(OperationType.REQUEST, ErrorType.REQUEST_FAILED, "从采编中心获取部门集合", e);
            throw new RemoteException("获取部门集合失败！", e);
        }
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
