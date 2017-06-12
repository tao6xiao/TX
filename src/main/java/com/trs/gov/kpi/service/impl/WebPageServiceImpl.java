package com.trs.gov.kpi.service.impl;

import com.trs.gov.kpi.constant.Status;
import com.trs.gov.kpi.constant.Types;
import com.trs.gov.kpi.constant.WebpageTableField;
import com.trs.gov.kpi.dao.WebPageMapper;
import com.trs.gov.kpi.entity.*;
import com.trs.gov.kpi.entity.dao.QueryFilter;
import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.gov.kpi.entity.requestdata.PageDataRequestParam;
import com.trs.gov.kpi.entity.responsedata.*;
import com.trs.gov.kpi.service.WebPageService;
import com.trs.gov.kpi.service.helper.QueryFilterHelper;
import com.trs.gov.kpi.service.outer.SiteApiService;
import com.trs.gov.kpi.utils.ChnlCheckUtil;
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


    @Override
    public ApiPageData selectReplySpeed(PageDataRequestParam param) throws RemoteException {
        param.setBeginDateTime(InitTime.initBeginDateTime(param.getBeginDateTime(), getEarliestCheckTime()));
        param.setEndDateTime(InitTime.initEndDateTime(param.getEndDateTime()));

        QueryFilter queryFilter = QueryFilterHelper.toPageFilter(param, siteApiService);
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
            replySpeedResponse.setChnlName(ChnlCheckUtil.getChannelName(replySpeed.getChnlId(), siteApiService));
            replySpeedResponse.setPageLink(replySpeed.getPageLink());
            replySpeedResponse.setReplySpeed(replySpeed.getSpeed());
            replySpeedResponse.setPageSpace(replySpeed.getSpeed());
            replySpeedResponse.setCheckTime(replySpeed.getCheckTime());
            replySpeedResponseList.add(replySpeedResponse);
        }

        return new ApiPageData(pager, replySpeedResponseList);
    }

    @Override
    public ApiPageData selectPageSpace(PageDataRequestParam param) throws RemoteException {
        param.setBeginDateTime(InitTime.initBeginDateTime(param.getBeginDateTime(), getEarliestCheckTime()));
        param.setEndDateTime(InitTime.initEndDateTime(param.getEndDateTime()));

        QueryFilter queryFilter = QueryFilterHelper.toPageFilter(param, siteApiService);
        queryFilter.addCond(WebpageTableField.TYPE_ID, Types.AnalysisType.OVERSIZE_PAGE.value);
        queryFilter.addCond(WebpageTableField.IS_RESOLVED, Status.Resolve.UN_RESOLVED.value);
        queryFilter.addCond(WebpageTableField.IS_DEL, Status.Delete.UN_DELETE.value);


        int count = webPageMapper.count(queryFilter);
        Pager pager = PageInfoDeal.buildResponsePager(param.getPageIndex(), param.getPageSize(), count);
        queryFilter.setPager(pager);

        List<PageSpace> pageSpaceList = webPageMapper.selectPageSpace(queryFilter);
        List<PageSpaceResponse> pageSpaceResponseList = new ArrayList<>();
        for (PageSpace pageSpace : pageSpaceList) {
            PageSpaceResponse pageSpaceResponse = new PageSpaceResponse();
            pageSpaceResponse.setId(pageSpace.getId());
            pageSpaceResponse.setChnlName(ChnlCheckUtil.getChannelName(pageSpace.getChnlId(), siteApiService));
            pageSpaceResponse.setPageLink(pageSpace.getPageLink());
            pageSpaceResponse.setReplySpeed(pageSpace.getSpeed());
            pageSpaceResponse.setPageSpace(pageSpace.getSpace());
            pageSpaceResponse.setCheckTime(pageSpace.getCheckTime());
            pageSpaceResponseList.add(pageSpaceResponse);
        }

        return new ApiPageData(pager, pageSpaceResponseList);
    }

    @Override
    public int selectPageSpaceCount(PageDataRequestParam param) throws RemoteException {
        param.setBeginDateTime(InitTime.initBeginDateTime(param.getBeginDateTime(), getEarliestCheckTime()));
        param.setEndDateTime(InitTime.initEndDateTime(param.getEndDateTime()));

        QueryFilter queryFilter = QueryFilterHelper.toPageFilter(param, siteApiService);
        queryFilter.addCond(WebpageTableField.TYPE_ID, Types.AnalysisType.OVERSIZE_PAGE.value);
        queryFilter.addCond(WebpageTableField.IS_RESOLVED, Status.Resolve.UN_RESOLVED.value);
        queryFilter.addCond(WebpageTableField.IS_DEL, Status.Delete.UN_DELETE.value);


        return webPageMapper.count(queryFilter);

    }

    @Override
    public ApiPageData selectPageDepth(PageDataRequestParam param) throws RemoteException {
        param.setBeginDateTime(InitTime.initBeginDateTime(param.getBeginDateTime(), getEarliestCheckTime()));
        param.setEndDateTime(InitTime.initEndDateTime(param.getEndDateTime()));

        QueryFilter queryFilter = QueryFilterHelper.toPageFilter(param, siteApiService);
        queryFilter.addCond(WebpageTableField.TYPE_ID, Types.AnalysisType.OVER_DEEP_PAGE.value);
        queryFilter.addCond(WebpageTableField.IS_RESOLVED, Status.Resolve.UN_RESOLVED.value);
        queryFilter.addCond(WebpageTableField.IS_DEL, Status.Delete.UN_DELETE.value);


        int count = webPageMapper.count(queryFilter);
        Pager pager = PageInfoDeal.buildResponsePager(param.getPageIndex(), param.getPageSize(), count);
        queryFilter.setPager(pager);

        List<PageDepth> pageDepthList = webPageMapper.selectPageDepth(queryFilter);
        List<PageDepthResponse> pageDepthResponseList = new ArrayList<>();
        for (PageDepth pageDepth : pageDepthList) {
            PageDepthResponse pageDepthResponse = new PageDepthResponse();
            pageDepthResponse.setId(pageDepth.getId());
            pageDepthResponse.setChnlName(ChnlCheckUtil.getChannelName(pageDepth.getChnlId(), siteApiService));
            pageDepthResponse.setPageLink(pageDepth.getPageLink());
            pageDepthResponse.setPageDepth(pageDepth.getDepth());
            pageDepthResponse.setPageSpace(pageDepth.getSpace());
            pageDepthResponse.setCheckTime(pageDepth.getCheckTime());
            pageDepthResponseList.add(pageDepthResponse);
        }

        return new ApiPageData(pager, pageDepthResponseList);
    }

    @Override
    public int selectPageDepthCount(PageDataRequestParam param) throws RemoteException {
        param.setBeginDateTime(InitTime.initBeginDateTime(param.getBeginDateTime(), getEarliestCheckTime()));
        param.setEndDateTime(InitTime.initEndDateTime(param.getEndDateTime()));

        QueryFilter queryFilter = QueryFilterHelper.toPageFilter(param, siteApiService);
        queryFilter.addCond(WebpageTableField.TYPE_ID, Types.AnalysisType.OVER_DEEP_PAGE.value);
        queryFilter.addCond(WebpageTableField.IS_RESOLVED, Status.Resolve.UN_RESOLVED.value);
        queryFilter.addCond(WebpageTableField.IS_DEL, Status.Delete.UN_DELETE.value);


        return webPageMapper.count(queryFilter);

    }

    @Override
    public ApiPageData selectRepeatCode(PageDataRequestParam param) throws RemoteException {
        param.setBeginDateTime(InitTime.initBeginDateTime(param.getBeginDateTime(), getEarliestCheckTime()));
        param.setEndDateTime(InitTime.initEndDateTime(param.getEndDateTime()));

        QueryFilter queryFilter = QueryFilterHelper.toPageFilter(param, siteApiService);
        queryFilter.addCond(WebpageTableField.TYPE_ID, Types.AnalysisType.REPEAT_CODE.value);
        queryFilter.addCond(WebpageTableField.IS_RESOLVED, Status.Resolve.UN_RESOLVED.value);
        queryFilter.addCond(WebpageTableField.IS_DEL, Status.Delete.UN_DELETE.value);


        int count = webPageMapper.count(queryFilter);
        Pager pager = PageInfoDeal.buildResponsePager(param.getPageIndex(), param.getPageSize(), count);
        queryFilter.setPager(pager);

        List<RepeatCode> repeatCodeList = webPageMapper.selectRepeatCode(queryFilter);
        List<RepeatCodeResponse> repeatCodeResponseList = new ArrayList<>();
        for (RepeatCode repeatCode : repeatCodeList) {
            RepeatCodeResponse repeatCodeResponse = new RepeatCodeResponse();
            repeatCodeResponse.setId(repeatCode.getId());
            repeatCodeResponse.setChnlName(ChnlCheckUtil.getChannelName(repeatCode.getChnlId(), siteApiService));
            repeatCodeResponse.setRepeatPlace(repeatCode.getRepeatPlace());
            repeatCodeResponse.setRepeatDegree(repeatCode.getRepeatDegree());
            repeatCodeResponse.setUpdateTime(repeatCode.getUpdateTime());
            repeatCodeResponse.setCheckTime(repeatCode.getCheckTime());
            repeatCodeResponseList.add(repeatCodeResponse);
        }

        return new ApiPageData(pager, repeatCodeResponseList);
    }

    @Override
    public int selectRepeatCodeCount(PageDataRequestParam param) throws RemoteException {
        param.setBeginDateTime(InitTime.initBeginDateTime(param.getBeginDateTime(), getEarliestCheckTime()));
        param.setEndDateTime(InitTime.initEndDateTime(param.getEndDateTime()));

        QueryFilter queryFilter = QueryFilterHelper.toPageFilter(param, siteApiService);
        queryFilter.addCond(WebpageTableField.TYPE_ID, Types.AnalysisType.REPEAT_CODE.value);
        queryFilter.addCond(WebpageTableField.IS_RESOLVED, Status.Resolve.UN_RESOLVED.value);
        queryFilter.addCond(WebpageTableField.IS_DEL, Status.Delete.UN_DELETE.value);


        return webPageMapper.count(queryFilter);

    }

    @Override
    public ApiPageData selectUrlLength(PageDataRequestParam param) throws RemoteException {
        param.setBeginDateTime(InitTime.initBeginDateTime(param.getBeginDateTime(), getEarliestCheckTime()));
        param.setEndDateTime(InitTime.initEndDateTime(param.getEndDateTime()));

        QueryFilter queryFilter = QueryFilterHelper.toPageFilter(param, siteApiService);
        queryFilter.addCond(WebpageTableField.TYPE_ID, Types.AnalysisType.TOO_LONG_URL.value);
        queryFilter.addCond(WebpageTableField.IS_RESOLVED, Status.Resolve.UN_RESOLVED.value);
        queryFilter.addCond(WebpageTableField.IS_DEL, Status.Delete.UN_DELETE.value);


        int count = webPageMapper.count(queryFilter);
        Pager pager = PageInfoDeal.buildResponsePager(param.getPageIndex(), param.getPageSize(), count);
        queryFilter.setPager(pager);

        List<UrlLength> urlLengthList = webPageMapper.selectUrlLength(queryFilter);
        List<UrlLengthResponse> urlLengthResponseList = new ArrayList<>();
        for (UrlLength urlLength : urlLengthList) {
            UrlLengthResponse urlLengthResponse = new UrlLengthResponse();
            urlLengthResponse.setId(urlLength.getId());
            urlLengthResponse.setChnlName(ChnlCheckUtil.getChannelName(urlLength.getChnlId(), siteApiService));
            urlLengthResponse.setPageLink(urlLength.getPageLink());
            urlLengthResponse.setUrlLength(urlLength.getLength());
            urlLengthResponse.setPageSpace(urlLength.getSpace());
            urlLengthResponse.setCheckTime(urlLength.getCheckTime());
            urlLengthResponseList.add(urlLengthResponse);
        }

        return new ApiPageData(pager, urlLengthResponseList);
    }

    @Override
    public int selectUrlLengthCount(PageDataRequestParam param) throws RemoteException {
        param.setBeginDateTime(InitTime.initBeginDateTime(param.getBeginDateTime(), getEarliestCheckTime()));
        param.setEndDateTime(InitTime.initEndDateTime(param.getEndDateTime()));

        QueryFilter queryFilter = QueryFilterHelper.toPageFilter(param, siteApiService);
        queryFilter.addCond(WebpageTableField.TYPE_ID, Types.AnalysisType.TOO_LONG_URL.value);
        queryFilter.addCond(WebpageTableField.IS_RESOLVED, Status.Resolve.UN_RESOLVED.value);
        queryFilter.addCond(WebpageTableField.IS_DEL, Status.Delete.UN_DELETE.value);


        return webPageMapper.count(queryFilter);

    }

    @Override
    public Date getEarliestCheckTime() {
        return webPageMapper.getEarliestCheckTime();
    }


    @Override
    public void handlePageByIds(int siteId, List<Integer> ids) {
        webPageMapper.handlePageByIds(siteId, ids);
    }

    @Override
    public void ignorePageByIds(int siteId, List<Integer> ids) {
        webPageMapper.ignorePageByIds(siteId, ids);
    }

    @Override
    public void delPageByIds(int siteId, List<Integer> ids) {
        webPageMapper.delPageByIds(siteId, ids);
    }
}
