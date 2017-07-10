package com.trs.gov.kpi.service.outer;

import com.trs.gov.kpi.entity.exception.RemoteException;

/**
 * Created by ranwei on 2017/7/4.
 */
public interface AuthorityService {

    /**
     * 根据oprkeys判断权限
     *
     * @param siteId
     * @param channelId
     * @param oprkeys
     * @return
     */
    boolean hasRight(Integer siteId, Integer channelId, String oprkeys) throws RemoteException;

    /**
     * 根据当前登录用户查询角色
     *
     * @return
     */
    String getRoleByUser() throws RemoteException;
}
