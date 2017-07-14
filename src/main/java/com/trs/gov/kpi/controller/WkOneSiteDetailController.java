package com.trs.gov.kpi.controller;

import com.trs.gov.kpi.constant.Constants;
import com.trs.gov.kpi.entity.exception.BizException;
import com.trs.gov.kpi.entity.requestdata.PageDataRequestParam;
import com.trs.gov.kpi.entity.responsedata.*;
import com.trs.gov.kpi.service.wangkang.WkAllStatsService;
import com.trs.gov.kpi.service.wangkang.WkOneSiteDetailService;
import com.trs.gov.kpi.utils.ParamCheckUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 单个网站信息查询
 *
 * Created by li.hao on 2017/7/12.
 */
@Slf4j
@RestController
@RequestMapping("gov/wangkang/one/site")
public class WkOneSiteDetailController {

    @Resource
    WkOneSiteDetailService wkOneSiteDetailService;

    @Resource
    WkAllStatsService wkAllStatsService;

    /**
     * 根据网站编号查询网站链接总数和类型
     *
     * @param siteId
     * @return
     */
    @RequestMapping(value = "/link/count", method = RequestMethod.GET)
    @ResponseBody
    public WkLinkTypeResponse getOneSiteLinkTypeBySiteId(Integer siteId) throws BizException {
        if (siteId == null) {
            log.error(Constants.SITE_ID_IS_NULL);
            throw new BizException(Constants.INVALID_PARAMETER);
        }
        return wkOneSiteDetailService.getOneSiteLinkTypeBySiteId(siteId);
    }

    /**
     * 根据网站编号查询网站综合分数 （获取最近一次检查记录）
     * @param siteId
     * @return
     * @throws BizException
     */
    @RequestMapping(value = "/score", method = RequestMethod.GET)
    @ResponseBody
    public WkOneSiteScoreResponse getOneSiteScoreBySiteId(@RequestParam("siteId") Integer siteId) throws BizException {
        if (siteId == null) {
            log.error(Constants.SITE_ID_IS_NULL);
            throw new BizException(Constants.INVALID_PARAMETER);
        }
        return wkOneSiteDetailService.getOneSiteScoreBySiteId(siteId);
    }

    /**
     * 根据网站编号查询历史评分记录
     * @param siteId
     * @return
     * @throws BizException
     */
    @RequestMapping(value = "/scorelist", method = RequestMethod.GET)
    @ResponseBody
    public List<WkOneSiteScoreResponse> getOneSiteScoreListById(Integer siteId) throws BizException {
        if (siteId == null) {
            log.error(Constants.SITE_ID_IS_NULL);
            throw new BizException(Constants.INVALID_PARAMETER);
        }
        return wkOneSiteDetailService.getOneSiteScoreListBySiteId(siteId);
    }

    /**
     * 链接可用性---根据网站编号查询已解决和未解决问题总数
     *
     * @param siteId
     * @return
     */
    @RequestMapping(value = "invalidlink/count/bytype", method = RequestMethod.GET)
    @ResponseBody
    public WkStatsCountResponse getInvalidlinkStatsBySiteId(Integer siteId) throws BizException {
        if (siteId == null) {
            log.error(Constants.SITE_ID_IS_NULL);
            throw new BizException(Constants.INVALID_PARAMETER);
        }
        return wkOneSiteDetailService.getInvalidlinkStatsBySiteId(siteId);
    }

    /**
     * 链接可用性---根据网站编号已解决和未解决问题总数历史记录
     *
     * @param siteId
     * @return
     */
    @RequestMapping(value = "invalidlink/counttrend/bytype", method = RequestMethod.GET)
    @ResponseBody
    public List<WkStatsCountResponse> getInvalidlinkHistoryStatsBySiteId(Integer siteId) throws BizException {
        if (siteId == null) {
            log.error(Constants.SITE_ID_IS_NULL);
            throw new BizException(Constants.INVALID_PARAMETER);
        }
        return wkOneSiteDetailService.getInvalidlinkHistoryStatsBySiteId(siteId);
    }

    /**
     * 根据网站编号查询首页链接的可用性
     *
     * @param siteId
     * @return
     */
    @RequestMapping(value = "/indexpage/status")
    public WkLinkIndexPageStatus getSiteIndexpageStatusBySiteId(Integer siteId) throws BizException {
        if (siteId == null) {
            log.error(Constants.SITE_ID_IS_NULL);
            throw new BizException(Constants.INVALID_PARAMETER);
        }
        return wkOneSiteDetailService.getSiteIndexpageStatusBySiteId(siteId);
    }

    /**
     * 链接可用性---查询未处理的链接的可用性
     *
     * @param param
     * @return
     * @throws BizException
     */
    @RequestMapping(value = "invalidlink/unhandled", method = RequestMethod.GET)
    public ApiPageData getInvalidLinkUnhandledBySiteId(PageDataRequestParam param) throws BizException {

        ParamCheckUtil.pagerCheck(param.getPageIndex(), param.getPageSize());
        return wkOneSiteDetailService.getInvalidLinkUnhandledList(param);
    }

    /**
     * 内容检测---根据网站编号查询已解决和未解决问题总数
     *
     * @param siteId
     * @return
     */
    @RequestMapping(value = "contenterror/count/bytype", method = RequestMethod.GET)
    @ResponseBody
    public WkStatsCountResponse getContentErorStatsBySiteId(Integer siteId) throws BizException {
        if (siteId == null) {
            log.error(Constants.SITE_ID_IS_NULL);
            throw new BizException(Constants.INVALID_PARAMETER);
        }
        return wkOneSiteDetailService.getContentErorStatsBySiteId(siteId);
    }

    /**
     * 内容检测---根据网站编号已解决和未解决问题总数历史记录
     *
     * @param siteId
     * @return
     */
    @RequestMapping(value = "contenterror/counttrend/bytype", method = RequestMethod.GET)
    @ResponseBody
    public List<WkStatsCountResponse> getContentErorHistoryStatsBySiteId(Integer siteId) throws BizException {
        if (siteId == null) {
            log.error(Constants.SITE_ID_IS_NULL);
            throw new BizException(Constants.INVALID_PARAMETER);
        }
        return wkOneSiteDetailService.getContentErorHistoryStatsBySiteId(siteId);
    }

    /**
     * 内容检测---查询未处理的链接的可用性
     *
     * @param param
     * @return
     * @throws BizException
     */
    @RequestMapping(value = "contenterror/unhandled", method = RequestMethod.GET)
    public ApiPageData getContentErrorUnhandledBySiteId(PageDataRequestParam param) throws BizException {

        ParamCheckUtil.pagerCheck(param.getPageIndex(), param.getPageSize());
        return wkOneSiteDetailService.getContentErrorUnhandledList(param);
    }

    /**
     * 访问速度---查询网站平均访问速度历史记录
     * @return
     */
    @RequestMapping(value = "speed/counttrend/bytype/avg", method = RequestMethod.GET)
    @ResponseBody
    public List<WkAvgSpeedAndUpdateContentResponse> getAvgSpeedHistory(){
        return wkAllStatsService.getAvgSpeedHistory();
    }

    /**
     * 网站更新---查询网站每次更新数量的历史记录
     * @return
     */
    @RequestMapping(value = "update/counttrend/bytype/history", method = RequestMethod.GET)
    @ResponseBody
    public List<WkAvgSpeedAndUpdateContentResponse> getUpdateContentHistory(){
        return wkAllStatsService.getUpdateContentHistory();
    }

}
