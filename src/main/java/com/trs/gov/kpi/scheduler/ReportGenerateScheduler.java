package com.trs.gov.kpi.scheduler;

import com.trs.gov.kpi.constant.IssueIndicator;
import com.trs.gov.kpi.constant.Types;
import com.trs.gov.kpi.dao.ReportMapper;
import com.trs.gov.kpi.entity.Report;
import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.gov.kpi.entity.outerapi.Site;
import com.trs.gov.kpi.entity.requestdata.IssueCountByTypeRequest;
import com.trs.gov.kpi.entity.requestdata.IssueCountRequest;
import com.trs.gov.kpi.entity.responsedata.*;
import com.trs.gov.kpi.service.IssueCountService;
import com.trs.gov.kpi.service.outer.SiteApiService;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by ranwei on 2017/6/19.
 */
@Slf4j
@Component
@Scope("prototype")
public class ReportGenerateScheduler implements SchedulerTask {

    @Setter
    @Getter
    private String baseUrl;

    @Setter
    @Getter
    private Integer siteId;

    @Setter
    @Getter
    private Boolean isTimeNode;

    @Value("${issue.report.dir}")
    private String reportDir;

    @Resource
    IssueCountService countService;

    @Resource
    private SiteApiService siteApiService;

    @Resource
    private ReportMapper reportMapper;

    private int rowIndex = 0;//excel行的索引
    private int cellIndex = 0;//excel列的索引

    @Override
    public void run() {
        log.info("ReportGenerateScheduler " + siteId + " start...");

        IssueCountRequest request = new IssueCountRequest();
        request.setSiteIds(Integer.toString(siteId));
        Report report = new Report();
        report.setSiteId(siteId);
        report.setReportTime(new Date());
        Site site = null;
        try {
            site = siteApiService.getSiteById(siteId, "");
        } catch (RemoteException e) {
            log.error("", e);
        }
        String title = "";
        if (site != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            title = site.getSiteDesc() + "报表" + "(" + sdf.format(new Date()) + ")";
        }
        report.setTitle(title);

        String granularity;
        if (isTimeNode) {
            granularity = "day";
            report.setTypeId(1);
        } else {
            granularity = "month";
            report.setTypeId(2);
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        String fileDir = "c:" + "/" + Integer.toString(siteId) + "/" + granularity + "/";
        String fileName = sdf.format(new Date()) + ".xlsx";
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet();// 创建工作表(Sheet)

        //各状态问题数量统计
        List<Statistics> statisticsList = countService.countSort(request);
        HSSFRow row = sheet.createRow(rowIndex);
        for (Statistics statistics : statisticsList) {
            row.createCell(cellIndex).setCellValue(statistics.getName());
            row.createCell(cellIndex + 1).setCellValue(statistics.getCount());
            row.createCell(cellIndex + 2).setCellValue("");
            cellIndex += 3;
        }
        changeIndex();

        //各状态问题数量统计的历史记录
        List list = countService.historyCountSort(request).getData();
        for (Object object : list) {
            IssueHistoryCountResponse response = (IssueHistoryCountResponse) object;
            sheet.createRow(rowIndex).createCell(cellIndex).setCellValue(response.getName());
            changeIndex();
            row = sheet.createRow(rowIndex);
            for (HistoryStatistics historyStatistics : response.getData()) {
                row.createCell(cellIndex).setCellValue(historyStatistics.getTime());
                row.createCell(cellIndex + 1).setCellValue(historyStatistics.getValue());
                row.createCell(cellIndex + 2).setCellValue("");
                cellIndex += 3;
            }
            changeIndex();
        }

        //按状态再按部门统计问题的数量
        List<DeptCountResponse> deptCountResponseList = countService.deptCountSort(request);
        for (DeptCountResponse deptCountResponse : deptCountResponseList) {
            sheet.createRow(rowIndex).createCell(cellIndex).setCellValue(deptCountResponse.getName());
            changeIndex();
            row = sheet.createRow(rowIndex);
            for (DeptCount deptCount : deptCountResponse.getCount()) {
                row.createCell(cellIndex).setCellValue(deptCount.getDept());
                row.createCell(cellIndex + 1).setCellValue(deptCount.getValue());
                row.createCell(cellIndex + 2).setCellValue("");
                cellIndex += 3;
            }
            changeIndex();
        }

        //按部门再按状态统计问题的数量
        DeptInductionResponse[] induction = countService.deptInductionSort(request);
        row = sheet.createRow(rowIndex);
        row.createCell(cellIndex).setCellValue("部门");
        row.createCell(cellIndex + 1).setCellValue("待解决问题/待解决预警/已解决问题和预警");
        changeIndex();
        row = sheet.createRow(rowIndex);
        for (int i = 0; i < induction.length; i++) {
            row.createCell(cellIndex).setCellValue(induction[i].getDept());
            String unhandleIssue = "";
            String unhandleWarning = "";
            String handled = "";
            for (Statistics statistics : induction[i].getData()) {
                if (statistics.getType() == IssueIndicator.UN_SOLVED_ISSUE.value) {
                    unhandleIssue = Integer.toString(statistics.getCount());
                } else if (statistics.getType() == IssueIndicator.WARNING.value) {
                    unhandleWarning = Integer.toString(statistics.getCount());
                } else if (statistics.getType() == IssueIndicator.SOLVED_ALL.value) {
                    handled = Integer.toString(statistics.getCount());
                }
            }
            row.createCell(cellIndex + 1).setCellValue(String.format("%s/%s/%s", unhandleIssue, unhandleWarning, handled));
            row.createCell(cellIndex + 2).setCellValue("");
            cellIndex += 3;
            //满6个就换行
            if (i % 5 == 0) {
                changeIndex();
                row = sheet.createRow(rowIndex);
            }
        }
        changeIndex();

        //按问题类型再按部门统计未处理的问题数量
        IssueCountByTypeRequest typeRequest = new IssueCountByTypeRequest();
        typeRequest.setSiteIds(Integer.toString(siteId));
        //网站可用性
        typeRequest.setTypeId(Types.IssueType.LINK_AVAILABLE_ISSUE.value);
        List<DeptCount> deptCountList = countService.getDeptCountByType(typeRequest);
        sheet.createRow(rowIndex).createCell(cellIndex).setCellValue(Types.IssueType.LINK_AVAILABLE_ISSUE.getName());
        changeIndex();
        row = sheet.createRow(rowIndex);
        int index = 0;
        writeDeptCount(deptCountList, sheet, row, index);
        changeIndex();
        //信息更新
        typeRequest.setTypeId(Types.IssueType.INFO_UPDATE_ISSUE.value);
        deptCountList = countService.getDeptCountByType(typeRequest);
        sheet.createRow(rowIndex).createCell(cellIndex).setCellValue(Types.IssueType.INFO_UPDATE_ISSUE.getName());
        changeIndex();
        row = sheet.createRow(rowIndex);
        index = 0;
        writeDeptCount(deptCountList, sheet, row, index);
        changeIndex();
        //信息错误
        typeRequest.setTypeId(Types.IssueType.INFO_ERROR_ISSUE.value);
        deptCountList = countService.getDeptCountByType(typeRequest);
        sheet.createRow(rowIndex).createCell(cellIndex).setCellValue(Types.IssueType.INFO_ERROR_ISSUE.getName());
        changeIndex();
        row = sheet.createRow(rowIndex);
        index = 0;
        writeDeptCount(deptCountList, sheet, row, index);
        changeIndex();

        //写入文件
        try (FileOutputStream out = new FileOutputStream(fileDir + fileName)) {
            File dir = new File(fileDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            workbook.write(out);
        } catch (IOException e) {
            log.error("", e);
        }
        report.setPath(fileDir + fileName);
        report.setCrTime(new Date());
        //入库
        reportMapper.insert(report);
        log.info("ReportGenerateScheduler " + siteId + " end...");
    }

    private void writeDeptCount(List<DeptCount> deptCountList, HSSFSheet sheet, HSSFRow row, int index) {
        HSSFRow tempRow = row;
        int tempIndex = index;
        for (DeptCount deptCount : deptCountList) {
            tempRow.createCell(cellIndex).setCellValue(deptCount.getDept());
            tempRow.createCell(cellIndex + 1).setCellValue(deptCount.getValue());
            tempRow.createCell(cellIndex + 2).setCellValue("");
            cellIndex += 3;
            //满6个就换行
            if (tempIndex % 5 == 0) {
                changeIndex();
                tempRow = sheet.createRow(rowIndex);
            }
            tempIndex++;
        }
    }


    //索引指向下一行第一个单元格
    private void changeIndex() {
        rowIndex++;
        cellIndex = 0;
    }


}
