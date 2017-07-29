package com.trs.gov.kpi.service.outer;

import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.gov.kpi.utils.LogUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by linwei on 2017/5/24.
 */
@Slf4j
@Component
public class SiteChannelServiceHelper {

    @Resource
    SiteApiService siteApiService;

    @Resource
    DocumentApiService documentApiService;

    /**
     * 获取所有空栏目
     *
     * @param siteId
     * @return
     * @throws RemoteException
     */
    public List<Integer> getEmptyChannel(int siteId) throws RemoteException {

        Set<Integer> allLeafChannels = new HashSet<>();
        allLeafChannels = siteApiService.getAllLeafChnlIds("", siteId, 0, allLeafChannels);

        List<Integer> emptyChannels = new ArrayList<>();
        if (allLeafChannels == null || allLeafChannels.isEmpty()) {
            return emptyChannels;
        }

        for(Integer channelId : allLeafChannels) {
            try {
                final List<Integer> publishDocIds = documentApiService.getPublishDocIds("", siteId,
                        channelId, null);
                if (publishDocIds == null || publishDocIds.isEmpty()) {
                    emptyChannels.add(channelId);
                }
            } catch (Exception e) {
                log.error("", e);
                LogUtil.addSystemLog("", e);
            }
        }

        return emptyChannels;
    }

}
