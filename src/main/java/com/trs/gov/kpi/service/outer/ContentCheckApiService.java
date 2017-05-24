package com.trs.gov.kpi.service.outer;

import com.trs.gov.kpi.entity.Issue;
import com.trs.gov.kpi.entity.exception.RemoteException;

import java.util.List;

/**
 * Created by he.lang on 2017/5/24.
 */
public interface ContentCheckApiService extends OuterApiService {

    List<Issue> check(String content) throws RemoteException;

}
