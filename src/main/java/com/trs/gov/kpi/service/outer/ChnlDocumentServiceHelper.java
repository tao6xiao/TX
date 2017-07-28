package com.trs.gov.kpi.service.outer;

import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.gov.kpi.entity.outerapi.Channel;
import com.trs.gov.kpi.entity.outerapi.Document;
import com.trs.gov.kpi.utils.SpringContextUtil;

/**
 * Created by he.lang on 2017/7/27.
 */
public class ChnlDocumentServiceHelper {

    private ChnlDocumentServiceHelper(){}

    public static Integer getChnlIdByUrl(String currUserName, String url, int siteId) throws RemoteException {
        DocumentApiService documentApiService = (DocumentApiService) SpringContextUtil.getBean(DocumentApiService.class);
        Document document = documentApiService.findDocumentByUrl(currUserName, url);
        if(document != null){
            return document.getChannelId();
        }else {
            SiteApiService siteApiService = (SiteApiService) SpringContextUtil.getBean(SiteApiService.class);
            Channel channel = siteApiService.findChannelByUrl(currUserName, url, siteId);
            if(channel != null){
                return channel.getChannelId();
            }
        }
        return null;
    }
}
