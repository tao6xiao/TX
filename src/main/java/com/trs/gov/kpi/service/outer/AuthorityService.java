package com.trs.gov.kpi.service.outer;

import com.trs.gov.kpi.entity.exception.RemoteException;

/**
 * Created by ranwei on 2017/7/4.
 */
public interface AuthorityService {

    /**
     * 根据oprkeys判断权限
     *
     * @param currUserName
     * @param siteId
     * @param channelId
     * @param oprkeys
     * @return
     * @throws RemoteException
     */
    boolean hasRight(String currUserName, Integer siteId, Integer channelId, String oprkeys) throws RemoteException;

}
