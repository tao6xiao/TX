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
        // 本地应用不做任何判定，完全交由ids来判定
        return false;
    }

    /**
     * 加载登录的统一用户到 Demo 应用的当前会话(Session 对象)中, 完成 Demo 应用自己的登录逻辑 (不需要再次对用户进行认证, 只需要加载).
     *
     * @see StdHttpSessionBasedActor#loadLoginUser(javax.servlet.http.HttpServletRequest,
     * com.trs.idm.client.actor.SSOUser)
     */
    public void loadLoginUser(HttpServletRequest request, SSOUser loginUser) {
        HttpSession session = request.getSession();

        // 判定是否和之前的用户相同，如果相同，直接返回
        LocalUser existUser = (LocalUser) session.getAttribute(LOGIN_FLAG);
        if (existUser != null && existUser.getUserName().equals(loginUser.getUserName())) {
            return;
        }

        LocalUser localUser = new LocalUser();
        localUser.setUserName(loginUser.getUserName());
        localUser.setLastLoginIP(RemoteAddrUtil.getRemoteAddr(request));

        //在线程中初始化用户用于记录日志
        ContextHelper.initContext(localUser);

        UserApiService userApiService = SpringContextUtil.getBean(UserApiService.class);
        User user;
        String message = "采编中心无当前用户[" + localUser.getUserName() + "]";
        try {
            user = userApiService.finUserByUserName("", loginUser.getUserName());
        } catch (RemoteException e) {
            session.setAttribute(LOGIN_FLAG, null);
            log.error(message, e);
            LogUtil.addErrorLog(OperationType.REMOTE, ErrorType.REMOTE_FAILED, message, e);
            LogUtil.addSecurityLog("登录失败");
            return;
        }
        if (user == null) {
            session.setAttribute(LOGIN_FLAG, null);
            log.error(message);
            LogUtil.addErrorLog(OperationType.REMOTE, ErrorType.REMOTE_FAILED, message, new RemoteException(message));
            LogUtil.addSecurityLog("登录失败");
            return;
        }

        localUser.setTrueName(user.getTrueName());
        localUser.setGroups((ArrayList<Map>) user.getGroups());
        session.setAttribute(LOGIN_FLAG, localUser);
        ContextHelper.initContext(localUser);

        log.info("LocalUser[" + localUser.getUserName() + "], user groups info:" + localUser.getGroups());
        LogUtil.addSecurityLog("登录成功");
    }

    /**
     * 完成 Demo 应用自己的退出登录的逻辑.
     *
     * @see StdHttpSessionBasedActor#logout(javax.servlet.http.HttpSession)
     */
    public void logout(HttpSession session) {
        try {
            log.info(" user logout : " + session.getAttribute(IDSActor.LOGIN_FLAG));
            LogUtil.addSecurityLog("退出成功");
            session.invalidate();
        } catch (IllegalStateException e) {
            // this can be ignored
        }
    }

    @Override
    public boolean userExist(SSOUser ssoUser) {
        log.warn("user exit : " + ssoUser.getUserName());
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
        log.info(" user disable : " + ssoUser.getUserName());
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
