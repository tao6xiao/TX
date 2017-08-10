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
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.util.Date;

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
    public ApiPageData selectReportByNode(@ModelAttribute ReportRequestParam param) throws RemoteException, ParseException, BizException {
        Date startTime = new Date();
        ParamCheckUtil.pagerCheck(param.getPageIndex(), param.getPageSize());
        ParamCheckUtil.checkDayTime(param.getDay());
        String logDesc = "按时间节点查询报表列表";
        authorityService.checkRight(Authority.KPIWEB_REPORT_SEARCH, param.getSiteId());
        try {
            ApiPageData apiPageData = reportService.selectReportList(param, true);
            Date endTime = new Date();
            LogUtil.addOperationLog(OperationType.QUERY, logDesc, LogUtil.getSiteNameForLog(siteApiService, param.getSiteId()));
            LogUtil.addElapseLog(OperationType.QUERY, LogUtil.buildElapseLogDesc(siteApiService, param.getSiteId(), logDesc), endTime.getTime() - startTime.getTime());
            return apiPageData;
        } catch (Exception e) {
            LogUtil.addOperationLog(OperationType.QUERY, LogUtil.buildFailOperationLogDesc(logDesc), LogUtil.getSiteNameForLog(siteApiService, param.getSiteId()));
            throw e;
        }
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
    public String exportReportByNode(@ModelAttribute ReportRequestParam param, HttpServletResponse response) throws ParseException, BizException, RemoteException {
        // TODO: 2017/8/9 REVIEW he.lang 站点无需判断？
        if (param.getId() == null) {
            throw new BizException(Constants.INVALID_PARAMETER);
        }
        String logDesc = "按时间节点导出下载统计报表";
        authorityService.checkRight(Authority.KPIWEB_REPORT_EXPORT, param.getSiteId());
        String path;
        try {
            path = reportService.getReportPath(param, true);
        } catch (Exception e) {
            LogUtil.addOperationLog(OperationType.QUERY, LogUtil.buildFailOperationLogDesc(logDesc + "：获取路径"), LogUtil.getSiteNameForLog(siteApiService, param.getSiteId()));
            throw e;
        }
        if (StringUtil.isEmpty(path)) {
            return null;
        }
        try {
            String[] str = path.split("/");
            download(response, "/" + str[1] + "/" + str[2] + "/", str[3]);
            LogUtil.addOperationLog(OperationType.REQUEST, logDesc, LogUtil.getSiteNameForLog(siteApiService, param.getSiteId()));
            return null;
        } catch (Exception e) {
            LogUtil.addOperationLog(OperationType.REQUEST, LogUtil.buildFailOperationLogDesc(logDesc), LogUtil.getSiteNameForLog(siteApiService, param.getSiteId()));
            throw e;
        }
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
    public ApiPageData selectReportByInterval(@ModelAttribute ReportRequestParam param) throws RemoteException, ParseException, BizException {
        Date startTime = new Date();
        ParamCheckUtil.pagerCheck(param.getPageIndex(), param.getPageSize());
        ParamCheckUtil.checkDayTime(param.getBeginDateTime());
        ParamCheckUtil.checkDayTime(param.getEndDateTime());
        String logDesc = "按时间区间查询报表列表";
        authorityService.checkRight(Authority.KPIWEB_REPORT_SEARCH, param.getSiteId());
        try {
            ApiPageData apiPageData = reportService.selectReportList(param, false);
            Date endTime = new Date();
            LogUtil.addOperationLog(OperationType.QUERY, logDesc, LogUtil.getSiteNameForLog(siteApiService, param.getSiteId()));
            LogUtil.addElapseLog(OperationType.QUERY, LogUtil.buildElapseLogDesc(siteApiService, param.getSiteId(), logDesc), endTime.getTime() - startTime.getTime());
            return apiPageData;
        } catch (Exception e) {
            LogUtil.addOperationLog(OperationType.QUERY, LogUtil.buildFailOperationLogDesc(logDesc), LogUtil.getSiteNameForLog(siteApiService, param.getSiteId()));
            throw e;
        }
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
        if (param.getId() == null) {
            throw new BizException(Constants.INVALID_PARAMETER);
        }
        String logDesc = "按时间区间导出下载统计报表";
        authorityService.checkRight(Authority.KPIWEB_REPORT_EXPORT, param.getSiteId());
        String path;
        try {
            path = reportService.getReportPath(param, false);
        } catch (Exception e) {
            LogUtil.addOperationLog(OperationType.QUERY, LogUtil.buildFailOperationLogDesc(logDesc + "获取路径"), LogUtil.getSiteNameForLog(siteApiService, param.getSiteId()));
            throw e;
        }
        if (StringUtil.isEmpty(path)) {
            return null;
        }
        try {
            String[] str = path.split("/");

            download(response, "/" + str[1] + "/" + str[2] + "/", str[3]);
            LogUtil.addOperationLog(OperationType.REQUEST, logDesc, LogUtil.getSiteNameForLog(siteApiService, param.getSiteId()));
            return null;
        } catch (Exception e) {
            LogUtil.addOperationLog(OperationType.REQUEST, LogUtil.buildFailOperationLogDesc(logDesc + "获取路径"), LogUtil.getSiteNameForLog(siteApiService, param.getSiteId()));
            throw e;
        }
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
