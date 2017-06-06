package com.trs.gov.kpi.service.impl;

import com.trs.gov.kpi.constant.Status;
import com.trs.gov.kpi.constant.Types;
import com.trs.gov.kpi.constant.WebpageTableField;
import com.trs.gov.kpi.dao.WebPageMapper;
import com.trs.gov.kpi.entity.ReplySpeed;
import com.trs.gov.kpi.entity.dao.QueryFilter;
import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.gov.kpi.entity.outerapi.Channel;
import com.trs.gov.kpi.entity.requestdata.PageDataRequestParam;
import com.trs.gov.kpi.entity.responsedata.ApiPageData;
import com.trs.gov.kpi.entity.responsedata.Pager;
import com.trs.gov.kpi.entity.responsedata.ReplySpeedResponse;
import com.trs.gov.kpi.service.WebPageService;
import com.trs.gov.kpi.service.helper.QueryFilterHelper;
import com.trs.gov.kpi.service.outer.SiteApiService;
import com.trs.gov.kpi.utils.InitTime;
import com.trs.gov.kpi.utils.PageInfoDeal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by ranwei on 2017/6/6.
 */
@Slf4j
@Service
public class WebPageServiceImpl implements WebPageService {

    @Resource
    private SiteApiService siteApiService;

    @Resource
    private WebPageMapper webPageMapper;



//    @Override
//    public int getReplySpeedCount(PageDataRequestParam param) {
//        return 0;
//    }

    @Override
    public ApiPageData selectReplySpeed(PageDataRequestParam param) {
        param.setBeginDateTime(InitTime.initBeginDateTime(param.getBeginDateTime(), getEarliestCheckTime()));
        param.setEndDateTime(InitTime.initEndDateTime(param.getEndDateTime()));

        QueryFilter queryFilter = QueryFilterHelper.toPageFilter(param);
        queryFilter.addCond(WebpageTableField.TYPE_ID, Types.AnalysisType.REPLY_SPEED.value);
        queryFilter.addCond(WebpageTableField.IS_RESOLVED, Status.Resolve.UN_RESOLVED.value);
        queryFilter.addCond(WebpageTableField.IS_DEL, Status.Delete.UN_DELETE.value);


        int count = webPageMapper.count(queryFilter);
        Pager pager = PageInfoDeal.buildResponsePager(param.getPageIndex(), param.getPageSize(), count);
        queryFilter.setPager(pager);

        List<ReplySpeed> replySpeedList = webPageMapper.selectReplySpeed(queryFilter);
        List<ReplySpeedResponse> replySpeedResponseList = new ArrayList<>();
        for (ReplySpeed replySpeed : replySpeedList) {
            ReplySpeedResponse replySpeedResponse = new ReplySpeedResponse();
            replySpeedResponse.setId(replySpeed.getId());
            replySpeedResponse.setChnlName(getChannelName(replySpeed.getChnlId()));
            replySpeedResponse.setPageLink(replySpeed.getPageLink());
            replySpeedResponse.setReplySpeed(replySpeed.getReplySpeed());
            replySpeedResponse.setPageSpace(replySpeed.getPageSpace());
            replySpeedResponse.setCheckTime(replySpeed.getCheckTime());
            replySpeedResponseList.add(replySpeedResponse);
        }

        return new ApiPageData(pager, replySpeedResponseList);
    }

    @Override
    public Date getEarliestCheckTime() {
        return webPageMapper.getEarliestCheckTime();
    }


    private String getChannelName(Integer channelId) {
        if (channelId == null) {
            return "";
        }
        Channel channel = null;
        try {
            channel = siteApiService.getChannelById(channelId, null);
        } catch (RemoteException e) {
            log.error("", e);
        }
        if (channel == null) {
            return "";
        }
        String chnlName = channel.getChnlName();
        if (chnlName == null) {
            return "";
        }
        return chnlName;
    }
}
