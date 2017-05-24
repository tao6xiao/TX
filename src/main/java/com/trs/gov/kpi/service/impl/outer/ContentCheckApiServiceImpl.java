package com.trs.gov.kpi.service.impl.outer;

import com.trs.gov.kpi.dao.MonitorSiteMapper;
import com.trs.gov.kpi.entity.Issue;
import com.trs.gov.kpi.entity.MonitorSite;
import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.gov.kpi.entity.outerapi.Channel;
import com.trs.gov.kpi.entity.outerapi.ContentCheckResult;
import com.trs.gov.kpi.service.outer.ContentCheckApiService;
import com.trs.gov.kpi.service.outer.DocumentApiService;
import com.trs.gov.kpi.service.outer.SiteApiService;
import com.trs.gov.kpi.utils.DataTypeConversion;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    public List<Issue> check(String content) throws RemoteException {
        List<Issue> issueList = new ArrayList<>();
        //get all monitorSite object
        List<MonitorSite> monitorSiteList = monitorSiteMapper.getAllMonitorSites();
        for (MonitorSite monitorSite : monitorSiteList) {
            //get all monitor siteId
            String siteIdsStr = null;
            Integer[] siteIds = DataTypeConversion.stringToIntegerArray(siteIdsStr);
            for (int i = 0; i < siteIds.length; i++) {
                if (siteIds[i] == null) {
                    continue;
                }
                // TODO: 2017/5/24  set userName
                List<Channel> channelList = siteApiService.getChildChannel(siteIds[i], 0, null);
                Set<Integer> allChnlIds = new HashSet<>();
                for (Channel channel : channelList) {
                    Integer channelId = channel.getChannelId();
                    if (channelId == null) {
                        continue;
                    }
                    allChnlIds.add(channelId);
                    // TODO: 2017/5/24  set userName
                    allChnlIds = siteApiService.getAllChildChnlIds(null, siteIds[i], channelId, allChnlIds);
                }
                for (Integer chnlId : allChnlIds) {
                    if (chnlId == null) {
                        continue;
                    }
                    // TODO: 2017/5/24  set userName
                    List<Integer> publishDocIds = documentApiService.getPublishDocIds(null, siteIds[i], chnlId, null);
                    for (Integer publishDocId : publishDocIds) {
                        if(publishDocId == null){
                           continue;
                        }
                        // TODO: 2017/5/24 get document info by id
                        Channel channel = null;
                        Issue issue = null;
                        issueList.add(issue);

                    }

                }
            }
        }
        return issueList;
    }

}
