package com.trs.gov.kpi.controller;

import com.trs.gov.kpi.entity.exception.BizException;
import com.trs.gov.kpi.entity.requestdata.WkAllSiteDetailRequest;
import com.trs.gov.kpi.entity.responsedata.ApiPageData;
import com.trs.gov.kpi.entity.responsedata.WkAllSiteScoreResponsed;
import com.trs.gov.kpi.service.wangkang.WkAllSiteDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 网康---所有网站检测
 *
 * Created by li.hao on 2017/7/10.
 */
@Slf4j
@RestController
@RequestMapping(value ="gov/wangkang/all/site")
public class WkAllSiteDetailController {

    @Resource
    WkAllSiteDetailService wkAllSiteDetailService;

    /**
     * 查询所有页面总分
     * @return
     * @throws BizException
     */
    @RequestMapping(value="/score", method = RequestMethod.GET)
    @ResponseBody
    public List<WkAllSiteScoreResponsed> allWkSiteScore() throws BizException {
        return wkAllSiteDetailService.queryAllSiteScore();
    }

    /**
     * 查询所有网站首页可用性的详细信息
     *
     * @param wkAllSiteDetail
     * @return
     */
    @RequestMapping(value="/available")
    @ResponseBody
    public ApiPageData allWkSiteAvailable(WkAllSiteDetailRequest wkAllSiteDetail){
        return wkAllSiteDetailService.queryAllWkSiteAvailable(wkAllSiteDetail);
    }
}
