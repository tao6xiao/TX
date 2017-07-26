package com.trs.gov.kpi.utils;

import com.trs.gov.kpi.entity.exception.BizException;
import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.gov.kpi.entity.outerapi.User;
import com.trs.gov.kpi.ids.ContextHelper;
import com.trs.gov.kpi.service.outer.UserApiService;
import com.trs.idm.client.actor.SSOUser;
import com.trs.mlf.simplelog.LogUser;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

import static com.trs.gov.kpi.ids.ContextHelper.CONTEXT_INDEX_IP;

/**
 * trs日志工具类
 * Created by he.lang on 2017/7/26.
 */
@Slf4j
public class TRSLogUserUtil {
    private TRSLogUserUtil(){}

    public static LogUser getLogUser() throws BizException, RemoteException {
        SSOUser localUser = ContextHelper.getLoginUser();
        if(localUser == null){
            log.error("当前用户未登录");
            throw new BizException("当前用户未登录");
        }
        UserApiService userApiService = (UserApiService) SpringContextUtil.getBean(UserApiService.class);
        User user = userApiService.finUserByUserName("", localUser.getUserName());
        if(user == null){
            log.error("当前用户未在采编中心找到");
            throw new BizException("当前用户未在采编中心找到");
        }
        StringBuilder buffer = new StringBuilder();
        if(user.getGroups() != null && !user.getGroups().isEmpty()){
            for(int i= 0; i < user.getGroups().size(); i++){
                Map map = user.getGroups().get(i);
                if(map.get("GName") == null){
                    continue;
                }
                buffer.append(map.get("GName"));
                buffer.append(",");
            }
        }
        String groupNames = "";
        if(buffer.length() != 0){
            groupNames = buffer.deleteCharAt(buffer.lastIndexOf(",")).toString();
        }
        String logIp = ContextHelper.getArg(CONTEXT_INDEX_IP).toString();
        if (user.getTrueName() != null) {
            return new LogUser(user.getTrueName(), logIp, groupNames);
        }
        return new LogUser(user.getUserName(), logIp, groupNames);
    }
}
