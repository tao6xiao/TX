package com.trs.gov.kpi.utils;

import com.trs.gov.kpi.constant.ErrorType;
import com.trs.gov.kpi.constant.OperationType;
import com.trs.gov.kpi.entity.exception.BizException;
import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.gov.kpi.entity.outerapi.User;
import com.trs.gov.kpi.ids.ContextHelper;
import com.trs.gov.kpi.service.outer.UserApiService;
import com.trs.idm.client.actor.SSOUser;
import com.trs.mlf.simplelog.LogUser;
import com.trs.mlf.simplelog.SimpleLogServer;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

import static com.trs.gov.kpi.ids.ContextHelper.CONTEXT_INDEX_IP;

/**
 * trs日志工具类
 * Created by he.lang on 2017/7/26.
 */
@Slf4j
public class TRSLogUserUtil {

    public static void main(String[] args){
        LogUtil.addErrorLog(OperationType.REQUEST, ErrorType.REQUEST_FAILED, "信息错误", new BizException());
    }

    private TRSLogUserUtil(){}

    public static LogUser getLogUser() {
        SSOUser localUser = ContextHelper.getLoginUser();
        UserApiService userApiService = (UserApiService) SpringContextUtil.getBean(UserApiService.class);
        User user = null;
        try {
            user = userApiService.finUserByUserName("", localUser.getUserName());
        } catch (RemoteException e) {
            log.error("", e);
            SimpleLogServer.error(LogUtil.MODULE_NAME, new LogUser(), OperationType.QUERY, ErrorType.REMOTE_FAILED, e.getMessage(), e);
            return new LogUser();
        }
        if(user == null){
            log.error("当前用户未在采编中心找到");
            SimpleLogServer.error(LogUtil.MODULE_NAME, new LogUser(), OperationType.QUERY, ErrorType.REMOTE_FAILED, "", new BizException("当前用户未在采编中心找到"));
            return new LogUser(localUser.getUserName(), "", "");
        }
        StringBuilder buffer = new StringBuilder();
        if(user.getGroups() != null && !user.getGroups().isEmpty()){
            for(int i= 0; i < user.getGroups().size(); i++){
                Map map = user.getGroups().get(i);
                if(map.get("GNAME") == null){
                    continue;
                }
                buffer.append(map.get("GNAME"));
                buffer.append(",");
            }
        }
        String groupNames = "";
        if(buffer.length() != 0){
            groupNames = buffer.deleteCharAt(buffer.lastIndexOf(",")).toString();
        }
        String logIp = ContextHelper.getArg(CONTEXT_INDEX_IP).toString();
        if (!"".equals(groupNames)) {
            return new LogUser(user.getUserName(), user.getTrueName(), logIp, groupNames);
        }
        return new LogUser(user.getUserName(), user.getTrueName(), logIp);
    }
}
