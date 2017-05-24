package com.trs.gov.kpi.service.impl.outer;

import com.trs.gov.kpi.constant.IsDelType;
import com.trs.gov.kpi.constant.IsResolvedType;
import com.trs.gov.kpi.constant.IssueType;
import com.trs.gov.kpi.dao.MonitorSiteMapper;
import com.trs.gov.kpi.entity.Issue;
import com.trs.gov.kpi.entity.MonitorSite;
import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.gov.kpi.entity.outerapi.Channel;
import com.trs.gov.kpi.entity.outerapi.ContentCheckResult;
import com.trs.gov.kpi.entity.outerapi.Document;
import com.trs.gov.kpi.service.outer.ContentCheckApiService;
import com.trs.gov.kpi.service.outer.DocumentApiService;
import com.trs.gov.kpi.service.outer.SiteApiService;
import com.trs.gov.kpi.utils.DataTypeConversion;
import com.trs.gov.kpi.utils.InitTime;
import org.testng.annotations.Test;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.*;

/**
 * Created by he.lang on 2017/5/24.
 */
public class ContentCheckApiServiceImpl implements ContentCheckApiService {

    @Resource
    private MonitorSiteMapper monitorSiteMapper;

    @Resource
    private SiteApiService siteApiService;

    @Resource
    private DocumentApiService documentApiService;

    @Override
    public List<Document> getPublishDocuments() throws RemoteException, ParseException {
        List<Document> documentList = new ArrayList<>();
        //get all monitorSite object
        List<MonitorSite> monitorSiteList = monitorSiteMapper.getAllMonitorSites();
        for (MonitorSite monitorSite : monitorSiteList) {
            //get siteId
            int siteId = monitorSite.getSiteId();
            // TODO: 2017/5/24  set userName
            List<Channel> channelList = siteApiService.getChildChannel(siteId, 0, null);
            Set<Integer> allChnlIds = new HashSet<>();
            for (Channel channel : channelList) {
                Integer channelId = channel.getChannelId();
                if (channelId == null) {
                    continue;
                }
                allChnlIds.add(channelId);
                // TODO: 2017/5/24  set userName
                allChnlIds = siteApiService.getAllChildChnlIds(null, siteId, channelId, allChnlIds);
            }
            for (Integer chnlId : allChnlIds) {
                if (chnlId == null) {
                    continue;
                }
                // TODO: 2017/5/24  set userName
                List<Integer> publishDocIds = documentApiService.getPublishDocIds(null, siteId, chnlId, null);
                for (Integer publishDocId : publishDocIds) {
                    if (publishDocId == null) {
                        continue;
                    }
                    // TODO: 2017/5/24  set userName
                    Document document = documentApiService.getDocument(null, chnlId, publishDocId);
                    document.setSiteId(siteId);
                    documentList.add(document);
                }
            }
        }
        return documentList;
    }

}
