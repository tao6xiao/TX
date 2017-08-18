package com.trs.gov.kpi.service.impl.outer;

import com.alibaba.fastjson.JSON;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Response;
import com.trs.gov.kpi.constant.ErrorType;
import com.trs.gov.kpi.constant.OperationType;
import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.gov.kpi.entity.outerapi.ApiResult;
import com.trs.gov.kpi.entity.outerapi.User;
import com.trs.gov.kpi.service.outer.UserApiService;
import com.trs.gov.kpi.utils.LogUtil;
import com.trs.gov.kpi.utils.OuterApiServiceUtil;
import com.trs.gov.kpi.utils.OuterApiUtil;
import com.trs.gov.kpi.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by he.lang on 2017/6/27.
 */
@Slf4j
@Service
public class UserApiServiceImpl implements UserApiService {

    @Value("${service.outer.editcenter.url}")
    private String editCenterServiceUrl;

    private static final String SERVICE_NAME = "gov_user";

    @Override
    public User findUserById(String currUserName, int userId) throws RemoteException {
        try {
            Map<String, String> params = new HashMap<>();
            params.put("userId", String.valueOf(userId));
            OkHttpClient client = new OkHttpClient();
            Response response = client.newCall(
                    OuterApiServiceUtil.buildRequest("findUserById", currUserName,
                            params, SERVICE_NAME, editCenterServiceUrl)).execute();

            if (response.isSuccessful()) {
                ApiResult result = OuterApiUtil.getValidResult(response, "获取用户");
                if (StringUtil.isEmpty(result.getData())) {
                    return new User();
                }
                if (JSON.parseObject(result.getData(), User.class).getTrueName() == null) {
                    return new User();
                }
                return JSON.parseObject(result.getData(), User.class);
            } else {
                log.error("failed to findUserById, [userId=" + userId + "], error: " + response);
                throw new RemoteException("通过用户id获取用户对象失败！[userId=" + userId + "]，返回：" + response);
            }
        } catch (IOException e) {
            String errorInfo = MessageFormat.format("failed get user by id from edit center, [userId={0}]", userId);
            log.error(errorInfo, e);
            LogUtil.addErrorLog(OperationType.REQUEST, ErrorType.REQUEST_FAILED, errorInfo, e);
            throw new RemoteException("获取用户失败！[userId=" + userId + "]", e);
        }
    }

    @Override
    public User finUserByUserName(String currUserName, String userName) throws RemoteException {
        try {
            Map<String, String> params = new HashMap<>();
            params.put("userName", userName);
            OkHttpClient client = new OkHttpClient();
            Response response = client.newCall(
                    OuterApiServiceUtil.buildRequest("findUserByUserName", currUserName,
                            params, SERVICE_NAME, editCenterServiceUrl)).execute();

            if (response.isSuccessful()) {
                ApiResult result = OuterApiUtil.getValidResult(response, "获取用户");
                if (StringUtil.isEmpty(result.getData())) {
                    return null;
                }
                return JSON.parseObject(result.getData(), User.class);
            } else {
                log.error("failed to finUserByUserName, [userName=" + userName + "], error: " + response);
                throw new RemoteException("通过用户账号获取用户对象失败！[userName=" + userName + "]，返回：" + response);
            }
        } catch (IOException e) {
            String errorInfo = MessageFormat.format("failed get user by userName from edit center, [userName={0}]", userName);
            log.error(errorInfo, e);
            LogUtil.addErrorLog(OperationType.REQUEST, ErrorType.REQUEST_FAILED, errorInfo, e);
            throw new RemoteException("获取用户失败！[userName=" + userName + "]", e);
        }
    }
}
