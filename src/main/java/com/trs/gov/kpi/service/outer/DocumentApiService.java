package com.trs.gov.kpi.service.outer;

import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.gov.kpi.entity.outerapi.Document;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

/**
 * Created by linwei on 2017/5/18.
 */
public interface DocumentApiService extends OuterApiService {

    /**
     * 获取栏目下所有的发布文档的id，仅限当前栏目的更新文档，不包含子栏目的更新文档
     * @param userName
     * @param siteId
     * @param channelId
     * @param beginTime 起始时间
     * @return
     * @throws RemoteException
     */
    List<Integer> getPublishDocIds(String userName, int siteId, int channelId, String beginTime) throws RemoteException;


    /**
     * 获取文档详情
     * @param userName
     * @param channelId
     * @param docmentId
     * @return
     * @throws RemoteException
     */
    Document getDocument(String userName, int channelId, int docmentId) throws RemoteException;


    /**
     *
     * @param siteId
     * @return
     * @throws RemoteException
     * @throws ParseException
     */
    List<Document> getPublishDocuments(int siteId) throws RemoteException, ParseException;

}
