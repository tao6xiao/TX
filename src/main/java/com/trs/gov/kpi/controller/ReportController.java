package com.trs.gov.kpi.controller;

import com.trs.gov.kpi.constant.Authority;
import com.trs.gov.kpi.constant.Constants;
import com.trs.gov.kpi.constant.ErrorType;
import com.trs.gov.kpi.constant.OperationType;
import com.trs.gov.kpi.entity.exception.BizException;
import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.gov.kpi.entity.requestdata.ReportRequestParam;
import com.trs.gov.kpi.entity.responsedata.ApiPageData;
import com.trs.gov.kpi.service.ReportService;
import com.trs.gov.kpi.service.outer.AuthorityService;
import com.trs.gov.kpi.service.outer.SiteApiService;
import com.trs.gov.kpi.utils.LogUtil;
import com.trs.gov.kpi.utils.ParamCheckUtil;
import com.trs.gov.kpi.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.ParseException;

/**
 * Created by ranwei on 2017/6/9.
 */
@Slf4j
@RestController
@RequestMapping(value = "/gov/kpi/report")
public class ReportController {

    @Value("${issue.report.dir}")
    private String reportDir;

    @Resource
    private ReportService reportService;

    @Resource
    private AuthorityService authorityService;

    @Resource
    SiteApiService siteApiService;

    /**
     * 按时间节点查询报表列表
     *
     * @param param
     * @return
     * @throws RemoteException
     * @throws ParseException
     * @throws BizException
     */
    @RequestMapping(value = "/timenode", method = RequestMethod.GET)
    public ApiPageData selectReportByNode(@ModelAttribute ReportRequestParam param) throws RemoteException, BizException {
        String logDesc = "按时间节点查询报表列表" + LogUtil.paramsToLogString(Constants.PARAM, param);
        return LogUtil.ControlleFunctionWrapper(() -> {
            ParamCheckUtil.pagerCheck(param.getPageIndex(), param.getPageSize());
            ParamCheckUtil.checkDayTime(param.getDay());
            authorityService.checkRight(Authority.KPIWEB_REPORT_SEARCH, param.getSiteId());
            ApiPageData apiPageData = null;
            try {
                apiPageData = reportService.selectReportList(param, true);
            } catch (ParseException e) {
                log.error("", e);
                throw new BizException("");
            }
            return apiPageData;
        }, OperationType.QUERY, logDesc, LogUtil.getSiteNameForLog(siteApiService, param.getSiteId()));

    }

    /**
     * 按时间节点统计报表导出下载
     *
     * @param param
     * @param response
     * @return
     * @throws ParseException
     * @throws BizException
     * @throws RemoteException
     */
    @RequestMapping(value = "/timenode/export", method = RequestMethod.GET)
    public String exportReportByNode(@ModelAttribute ReportRequestParam param, HttpServletResponse response) throws BizException, RemoteException {
        // TODO: 2017/8/9 REVIEW he.lang DO_ran.wei FIXED 站点无需判断？
        String logDesc = "按时间节点导出下载统计报表" + LogUtil.paramsToLogString(Constants.PARAM, param);
        return LogUtil.ControlleFunctionWrapper(() -> {
            if (param.getId() == null || param.getSiteId() == null) {
                throw new BizException(Constants.INVALID_PARAMETER);
            }
            authorityService.checkRight(Authority.KPIWEB_REPORT_EXPORT, param.getSiteId());
            String path = null;
            try {
                path = reportService.getReportPath(param, true);
            } catch (ParseException e) {
                log.error("", e);
                throw new BizException("");
            }
            // TODO REVEIW DO_RAN.WEI FIXED  对于文件找不到的情况，抛出一个异常
            if (StringUtil.isEmpty(path)) {
                try {
                    throw new FileNotFoundException("报表文件不存在");
                } catch (FileNotFoundException e) {
                    log.error("", e);
                    throw new BizException(e.getMessage());
                }
            }
            String[] str = path.split("/");
            download(response, "/" + str[1] + "/" + str[2] + "/", str[3]);
            return null;
        }, OperationType.DOWNLOAD, logDesc, LogUtil.getSiteNameForLog(siteApiService, param.getSiteId()));
    }

    /**
     * 按时间区间查询报表列表
     *
     * @param param
     * @return
     * @throws RemoteException
     * @throws ParseException
     * @throws BizException
     */
    @RequestMapping(value = "/timeinterval", method = RequestMethod.GET)
    public ApiPageData selectReportByInterval(@ModelAttribute ReportRequestParam param) throws RemoteException, BizException {
        String logDesc = "按时间区间查询报表列表" + LogUtil.paramsToLogString(Constants.PARAM, param);
        return LogUtil.ControlleFunctionWrapper(() -> {
            ParamCheckUtil.pagerCheck(param.getPageIndex(), param.getPageSize());
            ParamCheckUtil.checkDayTime(param.getBeginDateTime());
            ParamCheckUtil.checkDayTime(param.getEndDateTime());
            authorityService.checkRight(Authority.KPIWEB_REPORT_SEARCH, param.getSiteId());
            ApiPageData apiPageData = null;
            try {
                apiPageData = reportService.selectReportList(param, false);
            } catch (ParseException e) {
                log.error("", e);
                throw new BizException("");
            }
            return apiPageData;
        }, OperationType.QUERY, logDesc, LogUtil.getSiteNameForLog(siteApiService, param.getSiteId()));
    }

    /**
     * 按时间区间统计报表导出下载
     *
     * @param param
     * @param response
     * @return
     * @throws ParseException
     * @throws BizException
     * @throws RemoteException
     */
    @RequestMapping(value = "/timeinterval/export", method = RequestMethod.GET)
    public String exportReportByInterval(@ModelAttribute ReportRequestParam param, HttpServletResponse response) throws ParseException, BizException, RemoteException {
        String logDesc = "按时间区间导出下载统计报表" + LogUtil.paramsToLogString(Constants.PARAM, param);
        return LogUtil.ControlleFunctionWrapper(() -> {
            if (param.getId() == null) {
                throw new BizException(Constants.INVALID_PARAMETER);
            }
            authorityService.checkRight(Authority.KPIWEB_REPORT_EXPORT, param.getSiteId());
            String path;
            try {
                path = reportService.getReportPath(param, false);
            } catch (ParseException e) {
                log.error("", e);
                throw new BizException("");
            }
            if (StringUtil.isEmpty(path)) {
                return null;
            }
            String[] str = path.split("/");

            download(response, "/" + str[1] + "/" + str[2] + "/", str[3]);
            return null;
        }, OperationType.DOWNLOAD, logDesc, LogUtil.getSiteNameForLog(siteApiService, param.getSiteId()));
    }

    private void download(HttpServletResponse response, String relativePath, String fileName) {
        File file = new File(reportDir + relativePath, fileName);
        if (file.exists()) {
            response.setContentType("application/x-download");
            response.addHeader("Content-Disposition",
                    "attachment;fileName=" + fileName);// 设置文件名
            byte[] buffer = new byte[1024];
            try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file))) {
                OutputStream os = response.getOutputStream();
                int i;
                while ((i = bis.read(buffer)) != -1) {
                    os.write(buffer, 0, i);
                }
                log.info(fileName + " download success!");

            } catch (Exception e) {
                log.error(fileName + " download fail!", e);
                LogUtil.addErrorLog(OperationType.REQUEST, ErrorType.REQUEST_FAILED, fileName + " download failed!", e);
            }
        }
    }

}
