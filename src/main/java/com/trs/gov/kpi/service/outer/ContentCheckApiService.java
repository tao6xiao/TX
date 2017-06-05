package com.trs.gov.kpi.service.outer;

import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.gov.kpi.entity.outerapi.ContentCheckResult;

/**
 * Created by he.lang on 2017/5/24.
 */
public interface ContentCheckApiService extends OuterApiService {


    /**
     * 检查文本内容
     *
     * @param text
     * @param type
     * @return
     * @throws RemoteException
     */
    ContentCheckResult check(String text, String type) throws RemoteException;

}
