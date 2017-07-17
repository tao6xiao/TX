package com.trs.gov.kpi.controller;

import com.trs.gov.kpi.constant.Authority;
import com.trs.gov.kpi.constant.Constants;
import com.trs.gov.kpi.entity.exception.BizException;
import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.gov.kpi.entity.requestdata.ReportRequestParam;
import com.trs.gov.kpi.entity.responsedata.ApiPageData;
import com.trs.gov.kpi.ids.ContextHelper;
import com.trs.gov.kpi.service.ReportService;
import com.trs.gov.kpi.service.outer.AuthorityService;
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


    @RequestMapping(value = "/timenode", method = RequestMethod.GET)
    public ApiPageData selectReportByNode(@ModelAttribute ReportRequestParam param) throws RemoteException, ParseException, BizException {
        if (!authorityService.hasRight(ContextHelper.getLoginUser().getUserName(), param.getSiteId(), null, Authority.KPIWEB_REPORT_SEARCH) && !authorityService.hasRight(ContextHelper
                .getLoginUser().getUserName(), null, null, Authority.KPIWEB_REPORT_SEARCH)) {
            throw new BizException(Authority.NO_AUTHORITY);
        }
        ParamCheckUtil.pagerCheck(param.getPageIndex(), param.getPageSize());
        ParamCheckUtil.checkDayTime(param.getDay());
        return reportService.selectReportList(param, true);
    }

    @RequestMapping(value = "/timenode/export", method = RequestMethod.GET)
    public String exportReportByNode(@ModelAttribute ReportRequestParam param, HttpServletResponse response) throws ParseException, BizException, RemoteException {
        if (!authorityService.hasRight(ContextHelper.getLoginUser().getUserName(), param.getSiteId(), null, Authority.KPIWEB_REPORT_EXPORT) && !authorityService.hasRight(ContextHelper
                .getLoginUser().getUserName(), null, null, Authority.KPIWEB_REPORT_EXPORT)) {
            throw new BizException(Authority.NO_AUTHORITY);
        }
        if (param.getId() == null) {
            throw new BizException(Constants.INVALID_PARAMETER);
        }
        String path = reportService.getReportPath(param, true);
        if (StringUtil.isEmpty(path)) {
            return null;
        }
        String[] str = path.split("/");

        download(response, "/" + str[1] + "/" + str[2] + "/", str[3]);
        return null;
    }

    @RequestMapping(value = "/timeinterval", method = RequestMethod.GET)
    public ApiPageData selectReportByInterval(@ModelAttribute ReportRequestParam param) throws RemoteException, ParseException, BizException {
        if (!authorityService.hasRight(ContextHelper.getLoginUser().getUserName(), param.getSiteId(), null, Authority.KPIWEB_REPORT_SEARCH) && !authorityService.hasRight(ContextHelper
                .getLoginUser().getUserName(), null, null, Authority.KPIWEB_REPORT_SEARCH)) {
            throw new BizException(Authority.NO_AUTHORITY);
        }
        ParamCheckUtil.pagerCheck(param.getPageIndex(), param.getPageSize());
        ParamCheckUtil.checkDayTime(param.getBeginDateTime());
        ParamCheckUtil.checkDayTime(param.getEndDateTime());
        return reportService.selectReportList(param, false);
    }

    @RequestMapping(value = "/timeinterval/export", method = RequestMethod.GET)
    public String exportReportByInterval(@ModelAttribute ReportRequestParam param, HttpServletResponse response) throws ParseException, BizException, RemoteException {
        if (!authorityService.hasRight(ContextHelper.getLoginUser().getUserName(), param.getSiteId(), null, Authority.KPIWEB_REPORT_EXPORT) && !authorityService.hasRight(ContextHelper
                .getLoginUser().getUserName(), null, null, Authority.KPIWEB_REPORT_EXPORT)) {
            throw new BizException(Authority.NO_AUTHORITY);
        }
        if (param.getId() == null) {
            throw new BizException(Constants.INVALID_PARAMETER);
        }
        String path = reportService.getReportPath(param, false);
        if (StringUtil.isEmpty(path)) {
            return null;
        }
        String[] str = path.split("/");

        download(response, "/" + str[1] + "/" + str[2] + "/", str[3]);
        return null;
    }

    private void download(HttpServletResponse response, String relativePath, String fileName) {
        File file = new File(reportDir + relativePath, fileName);
        if (file.exists()) {
            response.setContentType("application/force-download");// 设置强制下载不打开
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
            }
        }
    }

}
