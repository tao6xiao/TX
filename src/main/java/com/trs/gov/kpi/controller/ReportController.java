package com.trs.gov.kpi.controller;

import com.trs.gov.kpi.constant.Constants;
import com.trs.gov.kpi.entity.exception.BizException;
import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.gov.kpi.entity.requestdata.ReportRequestParam;
import com.trs.gov.kpi.entity.responsedata.ApiPageData;
import com.trs.gov.kpi.service.ReportService;
import com.trs.gov.kpi.utils.ParamCheckUtil;
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


    @RequestMapping(value = "/timenode", method = RequestMethod.GET)
    public ApiPageData selectReportByNode(@ModelAttribute ReportRequestParam param) throws RemoteException, ParseException, BizException {
        checkParam(param);
        return reportService.selectReportList(param, true);
    }

    @RequestMapping(value = "/timenode/export", method = RequestMethod.GET)
    public String exportReportByNode(@ModelAttribute ReportRequestParam param, HttpServletResponse response) throws ParseException, BizException {
        if (param.getId() == null) {
            throw new BizException(Constants.INVALID_PARAMETER);
        }
        reportService.getReportPath(param, true);

        String fileName = null;
        if (param.getId() != null) {
            fileName = "7z1604-x64.exe";
        }

        if (fileName != null) {
            download(response, fileName);
        }
        return null;
    }

    @RequestMapping(value = "/timeinterval", method = RequestMethod.GET)
    public ApiPageData selectReportByInterval(@ModelAttribute ReportRequestParam param) throws RemoteException, ParseException, BizException {
        checkParam(param);
        return reportService.selectReportList(param, false);
    }

    @RequestMapping(value = "/timeinterval/export", method = RequestMethod.GET)
    public String exportReportByInterval(@ModelAttribute ReportRequestParam param, HttpServletResponse response) throws ParseException, BizException {
        if (param.getId() == null) {
            throw new BizException(Constants.INVALID_PARAMETER);
        }
        reportService.getReportPath(param, false);

        String fileName = null;
        if (param.getId() != null) {
            fileName = "7z1604-x64.exe";
        }

        if (fileName != null) {
            download(response, fileName);
        }
        return null;
    }

    private void checkParam(ReportRequestParam param) throws BizException {
        ParamCheckUtil.pagerCheck(param.getPageIndex(), param.getPageSize());
        ParamCheckUtil.checkTime(param.getBeginDateTime());
        ParamCheckUtil.checkTime(param.getEndDateTime());
    }

    private void download(HttpServletResponse response, String fileName) {
        //TODO 文件路径待确定
        File file = new File(reportDir, fileName);
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
