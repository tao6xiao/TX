package com.trs.gov.kpi.service.outer;

import com.trs.gov.kpi.entity.exception.BizException;
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
     * @param oprkeys
     * @return
     * @throws RemoteException
     */
    boolean hasRight(String currUserName, Integer siteId, String oprkeys) throws RemoteException;

    /**
     * 根据oprkeys判断是否有平台权限或站点权限
     *
     * @param right
     * @param siteId
     * @return
     * @throws RemoteException
     */
    void checkRight(String right, Integer siteId) throws RemoteException, BizException;

}
