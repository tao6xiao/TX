package com.trs.gov.kpi.controller;

import com.trs.gov.kpi.constant.Constants;
import com.trs.gov.kpi.dao.WkCheckTimeMapper;
import com.trs.gov.kpi.entity.exception.BizException;
import com.trs.gov.kpi.entity.requestdata.PageDataRequestParam;
import com.trs.gov.kpi.entity.responsedata.*;
import com.trs.gov.kpi.service.wangkang.WkAllStatsService;
import com.trs.gov.kpi.service.wangkang.WkOneSiteDetailService;
import com.trs.gov.kpi.service.wangkang.WkScoreService;
import com.trs.gov.kpi.utils.DateUtil;
import com.trs.gov.kpi.utils.ParamCheckUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
    private WkScoreService wkScoreService;

    @Resource
    private WkOneSiteDetailService wkOneSiteDetailService;

    @Resource
    private WkAllStatsService wkAllStatsService;

    @Resource
    private WkCheckTimeMapper wkCheckTimeMapper;

    /**
     * 根据网站编号查询网站链接总数和类型
     *
     * @param siteId
     * @return
     */
    @RequestMapping(value = "/link/count", method = RequestMethod.GET)
    @ResponseBody
    public WkLinkTypeResponse getOneSiteLinkTypeBySiteId(Integer siteId, Integer checkId) throws BizException {
        if (siteId == null) {
            log.error(Constants.SITE_ID_IS_NULL);
            throw new BizException(Constants.INVALID_PARAMETER);
        }
        return wkOneSiteDetailService.getOneSiteLinkTypeBySiteId(siteId, checkId);
    }

    /**
     * 根据网站编号查询网站综合分数 （获取最近一次检查记录）
     * @param siteId
     * @return
     * @throws BizException
     */
    @RequestMapping(value = "/score", method = RequestMethod.GET)
    @ResponseBody
    public WkOneSiteScoreResponse getOneSiteScoreBySiteId(@RequestParam("siteId") Integer siteId, Integer checkId) throws BizException {
        if (siteId == null || checkId == null) {
            log.error(Constants.SITE_ID_IS_NULL);
            throw new BizException(Constants.INVALID_PARAMETER);
        }
        return wkOneSiteDetailService.getOneSiteScoreBySiteId(siteId, checkId);
    }

    /**
     * 根据网站编号查询网站综合分数 （获取最近一次检查记录）
     * @param siteId
     * @return
     * @throws BizException
     */
    @RequestMapping(value = "/checkid", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Integer> getOneSiteScoreBySiteId(@RequestParam("siteId") Integer siteId) throws BizException {
        if (siteId == null) {
            log.error(Constants.SITE_ID_IS_NULL);
            throw new BizException(Constants.INVALID_PARAMETER);
        }

        Integer checkId = wkCheckTimeMapper.getMaxCheckId(siteId);
        if (checkId == null) {
            return null;
        }

        return Collections.singletonMap("checkId", checkId);
    }

    /**
     * 根据网站编号查询历史评分记录
     * @param siteId
     * @return
     * @throws BizException
     */
    @RequestMapping(value = "/scorelist", method = RequestMethod.GET)
    @ResponseBody
    public List<WkOneSiteScoreResponse> getOneSiteScoreListById(Integer siteId, Integer checkId) throws BizException {
        if (siteId == null) {
            log.error(Constants.SITE_ID_IS_NULL);
            throw new BizException(Constants.INVALID_PARAMETER);
        }
        return wkScoreService.getListBySiteId(siteId, checkId);
    }

    /**
     * 链接可用性---根据网站编号查询已解决和未解决问题总数
     *
     * @param siteId
     * @return
     */
    @RequestMapping(value = "invalidlink/count/bytype", method = RequestMethod.GET)
    @ResponseBody
    public WkStatsCountResponse getInvalidlinkStatsBySiteId(Integer siteId,Integer checkId) throws BizException {
        if (siteId == null) {
            log.error(Constants.SITE_ID_IS_NULL);
            throw new BizException(Constants.INVALID_PARAMETER);
        }
        return wkOneSiteDetailService.getInvalidlinkStatsBySiteId(siteId, checkId);
    }

    /**
     * 链接可用性---根据网站编号已解决和未解决问题总数历史记录
     *
     * @param siteId
     * @return
     */
    @RequestMapping(value = "invalidlink/counttrend/bytype", method = RequestMethod.GET)
    @ResponseBody
    public List<WkStatsCountResponse> getInvalidlinkHistoryStatsBySiteId(Integer siteId, Integer checkId) throws BizException {
        if (siteId == null  || checkId == null) {
            log.error(Constants.SITE_ID_IS_NULL);
            throw new BizException(Constants.INVALID_PARAMETER);
        }
        return wkOneSiteDetailService.getInvalidlinkHistoryStatsBySiteId(siteId,checkId);
    }

    /**
     * 根据网站编号查询首页链接的可用性
     *
     * @param siteId
     * @return
     */
    @RequestMapping(value = "/indexpage/status")
    public WkLinkIndexPageStatus getSiteIndexpageStatusBySiteId(Integer siteId, Integer checkId) throws BizException {
        if (siteId == null) {
            log.error(Constants.SITE_ID_IS_NULL);
            throw new BizException(Constants.INVALID_PARAMETER);
        }
        return wkOneSiteDetailService.getSiteIndexpageStatusBySiteId(siteId, checkId);
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
    public WkStatsCountResponse getContentErorStatsBySiteId(Integer siteId, Integer checkId) throws BizException {
        if (siteId == null) {
            log.error(Constants.SITE_ID_IS_NULL);
            throw new BizException(Constants.INVALID_PARAMETER);
        }
        return wkOneSiteDetailService.getContentErorStatsBySiteId(siteId,checkId);
    }

    /**
     * 内容检测---根据网站编号已解决和未解决问题总数历史记录
     *
     * @param siteId
     * @return
     */
    @RequestMapping(value = "contenterror/counttrend/bytype", method = RequestMethod.GET)
    @ResponseBody
    public List<WkStatsCountResponse> getContentErorHistoryStatsBySiteId(Integer siteId, Integer checkId) throws BizException {
        if (siteId == null) {
            log.error(Constants.SITE_ID_IS_NULL);
            throw new BizException(Constants.INVALID_PARAMETER);
        }
        return wkOneSiteDetailService.getContentErorHistoryStatsBySiteId(siteId, checkId);
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
    @RequestMapping(value = "speed/counttrend", method = RequestMethod.GET)
    @ResponseBody
    public List<WkAvgSpeedResponse> getAvgSpeedHistory(Integer siteId, Integer checkId) throws BizException {
        if (siteId == null) {
            log.error(Constants.SITE_ID_IS_NULL);
            throw new BizException(Constants.INVALID_PARAMETER);
        }
        return wkAllStatsService.getAvgSpeedHistory(siteId, checkId);
    }

    /**
     * 网站更新---查询网站每次更新数量的历史记录
     * @return
     */
    @RequestMapping(value = "update/counttrend", method = RequestMethod.GET)
    @ResponseBody
    public List<WkUpdateContentResponse> getUpdateContentHistory(Integer siteId, Integer checkId) throws BizException {
        if (siteId == null) {
            log.error(Constants.SITE_ID_IS_NULL);
            throw new BizException(Constants.INVALID_PARAMETER);
        }
        return wkAllStatsService.getUpdateContentHistory(siteId, checkId);
    }

    /**
     * 网站更新---查询网站每次更新数量的历史记录
     * @return
     */
    @RequestMapping(value = "report/export", method = RequestMethod.GET)
    @ResponseBody
    public String exportReport(Integer siteId, Integer checkId, HttpServletResponse response) throws BizException {
        if (siteId == null || checkId == null) {
            log.error(Constants.SITE_ID_IS_NULL);
            throw new BizException(Constants.INVALID_PARAMETER);
        }
        String fileName = wkOneSiteDetailService.generateReport(siteId, checkId);
        download(response, siteId, fileName);
        return null;
    }

    private void download(HttpServletResponse response, Integer siteId, String reportFileName) throws BizException {
        File file = new File(reportFileName);
        if (file.exists()) {
            response.setContentType("application/force-download");// 设置强制下载不打开
            response.addHeader("Content-Disposition",
                    "attachment;fileName=" + "report_"+siteId+"_" + DateUtil.toString(new Date()) + ".xlsx");// 设置文件名
            byte[] buffer = new byte[1024];
            try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file))) {
                OutputStream os = response.getOutputStream();
                int i;
                while ((i = bis.read(buffer)) != -1) {
                    os.write(buffer, 0, i);
                }
                log.info(reportFileName + " download success!");

            } catch (Exception e) {
                log.error(reportFileName + " download fail!", e);
                throw new BizException("下载导出报表文件失败！");
            }
        }
    }

}
