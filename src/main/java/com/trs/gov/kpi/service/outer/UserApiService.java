package com.trs.gov.kpi.service.outer;

import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.gov.kpi.entity.outerapi.User;

/**
 * Created by he.lang on 2017/6/27.
 */
public interface UserApiService {
    /**
     * 通过用户id查询用户
     * @param currUserName
     * @param userId
     * @return
     */
    User findUserById(String currUserName,int userId) throws RemoteException;
}
