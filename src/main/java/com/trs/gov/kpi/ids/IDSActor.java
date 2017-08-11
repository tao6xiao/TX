package com.trs.gov.kpi.ids;

import com.trs.gov.kpi.constant.ErrorType;
import com.trs.gov.kpi.constant.OperationType;
import com.trs.gov.kpi.entity.LocalUser;
import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.gov.kpi.entity.outerapi.User;
import com.trs.gov.kpi.service.outer.UserApiService;
import com.trs.gov.kpi.utils.LogUtil;
import com.trs.gov.kpi.utils.RemoteAddrUtil;
import com.trs.gov.kpi.utils.SpringContextUtil;
import com.trs.idm.client.actor.SSOUser;
import com.trs.idm.client.actor.StdHttpSessionBasedActor;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by he.lang on 2017/6/29.
 */
@Slf4j
public class IDSActor extends StdHttpSessionBasedActor {
    /**
     * Demo 应用中 Session 的登录标记.
     */
    public static final String LOGIN_FLAG = "loginUser";

    /**
     * 判断当前 Session 是否登录.
     * <p>
     * TRS 身份服务器系统
     * 14
     * V5.0 协作应用集成手册
     *
     * @see StdHttpSessionBasedActor#checkLocalLogin(javax.servlet.http.HttpSession)
     */
    public boolean checkLocalLogin(HttpSession session) {
        try {
            return session.getAttribute(LOGIN_FLAG) != null;
        } catch (IllegalStateException e) {
            LogUtil.addErrorLog(OperationType.REQUEST, ErrorType.REQUEST_FAILED, "", e);
            return false;
        }
    }

    /**
     * 加载登录的统一用户到 Demo 应用的当前会话(Session 对象)中, 完成 Demo 应用自己的登录逻辑 (不需要再次对用户进行认证, 只需要加载).
     *
     * @see StdHttpSessionBasedActor#loadLoginUser(javax.servlet.http.HttpServletRequest,
     * com.trs.idm.client.actor.SSOUser)
     */
    public void loadLoginUser(HttpServletRequest request, SSOUser loginUser) {
        HttpSession session = request.getSession();

        UserApiService userApiService = (UserApiService) SpringContextUtil.getBean(UserApiService.class);
        User user;
        String message = "采编中心无当前用户";
        try {
            user = userApiService.finUserByUserName("", loginUser.getUserName());
        } catch (RemoteException e) {
            log.error(message, e);
            LogUtil.addErrorLog(OperationType.REMOTE, ErrorType.REMOTE_FAILED, message, e);
            LogUtil.addSecurityLog("登录失败");
            return;
        }
        if (user == null) {
            log.error(message);
            LogUtil.addErrorLog(OperationType.REMOTE, ErrorType.REMOTE_FAILED, message, new RemoteException(message));
            LogUtil.addSecurityLog("登录失败");
            return;
        }

        LocalUser localUser = new LocalUser();
        localUser.setUserName(loginUser.getUserName());
        localUser.setLastLoginIP(RemoteAddrUtil.getRemoteAddr(request));
        localUser.setTrueName(user.getTrueName());
        localUser.setGroups((ArrayList<Map>) user.getGroups());
        session.setAttribute(LOGIN_FLAG, localUser);

        log.info("loadLoginUser[" + loginUser.getUserName() + "], user groups info:" + loginUser.getSSOGroups());
        LogUtil.addSecurityLog("登录成功");
    }

    /**
     * 完成 Demo 应用自己的退出登录的逻辑.
     *
     * @see StdHttpSessionBasedActor#logout(javax.servlet.http.HttpSession)
     */
    public void logout(HttpSession session) {
        try {
            session.invalidate();
        } catch (IllegalStateException e) {
            // this can be ignored
        }
    }

    @Override
    public boolean userExist(SSOUser ssoUser) {
        return false;
    }

    @Override
    public boolean removeUser(SSOUser ssoUser, HttpServletRequest httpServletRequest) {
        return false;
    }

    @Override
    public boolean updateUser(SSOUser ssoUser, HttpServletRequest httpServletRequest) {
        return false;
    }

    @Override
    public boolean addUser(SSOUser ssoUser, HttpServletRequest httpServletRequest) {
        return false;
    }

    @Override
    public boolean enableUser(SSOUser ssoUser) {
        return false;
    }

    @Override
    public boolean disableUser(SSOUser ssoUser) {
        return false;
    }

    @Override
    public String extractUserPwd(HttpServletRequest httpServletRequest) {
        return null;
    }

    @Override
    public String extractUserName(HttpServletRequest httpServletRequest) {
        return null;
    }

}
