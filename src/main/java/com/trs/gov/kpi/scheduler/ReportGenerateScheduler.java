package com.trs.gov.kpi.scheduler;

import com.trs.gov.kpi.constant.*;
import com.trs.gov.kpi.dao.ReportMapper;
import com.trs.gov.kpi.entity.Report;
import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.gov.kpi.entity.outerapi.Site;
import com.trs.gov.kpi.entity.requestdata.IssueCountByTypeRequest;
import com.trs.gov.kpi.entity.requestdata.IssueCountRequest;
import com.trs.gov.kpi.entity.responsedata.*;
import com.trs.gov.kpi.service.IssueCountService;
import com.trs.gov.kpi.service.outer.SiteApiService;
import com.trs.gov.kpi.utils.LogUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
    public void run() throws RemoteException {
        log.info(SchedulerType.schedulerStart(SchedulerType.REPORT_GENERATE_SCHEDULER, siteId));
        LogUtil.addDebugLog(OperationType.TASK_SCHEDULE, DebugType.MONITOR_START, SchedulerType.schedulerStart(SchedulerType.REPORT_GENERATE_SCHEDULER, siteId));
        Date startTime = new Date();
        IssueCountRequest request = new IssueCountRequest();
        request.setSiteIds(Integer.toString(siteId));
        Report report = new Report();
        report.setSiteId(siteId);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.HOUR,-1);//数据对应时间往前退一小时，使数据与时间对应
        report.setReportTime(calendar.getTime());
        Site site = null;
        try {
            site = siteApiService.getSiteById(siteId, "");
        } catch (RemoteException e) {
            log.error("", e);
            LogUtil.addErrorLog(OperationType.REMOTE, ErrorType.REMOTE_FAILED, "", e);
        }
        String title = "";
        if (site != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            title = site.getSiteDesc() + "报表" + "(" + sdf.format(calendar.getTime()) + ")";
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
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");

        String fileDir = "/" + Integer.toString(siteId) + "/" + granularity + "/";
        String fileName = sdf.format(new Date()) + ".xlsx";
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("问题统计");// 创建工作表(Sheet)
        CellStyle style = workbook.createCellStyle();
        style.setFillForegroundColor(HSSFColor.GREEN.index);//设置图案颜色
        style.setFillBackgroundColor(HSSFColor.GREEN.index);//设置图案背景色
        style.setFillPattern(HSSFCellStyle.LESS_DOTS);//设置图案样式
        Font font = workbook.createFont();
        font.setFontName("Times New Roman");//设置字体名称
        font.setFontHeightInPoints((short) 17);//设置字号
        font.setColor(HSSFColor.WHITE.index);//设置字体颜色
        style.setFont(font);

        //各状态问题数量统计
        addTitle(sheet, style, "各状态的问题统计");
        changeIndex();
        List<Statistics> statisticsList = countService.countSort(request);
        Row row = sheet.createRow(rowIndex);
        for (Statistics statistics : statisticsList) {
            row.createCell(cellIndex).setCellValue(statistics.getName());
            CellRangeAddress region = new CellRangeAddress(rowIndex, rowIndex, cellIndex, cellIndex + 1);
            sheet.addMergedRegion(region);
            row.createCell(cellIndex + 2).setCellValue(statistics.getCount());
            row.createCell(cellIndex + 3).setCellValue("");
            cellIndex += 4;
        }
        changeIndex();
        sheet.createRow(rowIndex);
        changeIndex();

        //各状态问题数量统计的历史记录
        addTitle(sheet, style, "各状态问题的历史记录统计");
        changeIndex();
        List list = countService.historyCountSort(request).getData();
        for (Object object : list) {
            IssueHistoryCountResponse response = (IssueHistoryCountResponse) object;
            sheet.createRow(rowIndex).createCell(cellIndex).setCellValue(response.getName());
            CellRangeAddress region = new CellRangeAddress(rowIndex, rowIndex, cellIndex, cellIndex + 1);
            sheet.addMergedRegion(region);
            changeIndex();
            row = sheet.createRow(rowIndex);
            writeHistoryCount(response.getData(), row, cellIndex);
            changeIndex();
        }
        sheet.createRow(rowIndex);
        changeIndex();

        //按状态再按部门统计问题的数量
        addTitle(sheet, style, "各状态各部门的问题统计");
        changeIndex();
        List<DeptCountResponse> deptCountResponseList = countService.deptCountSort(request);
        for (DeptCountResponse deptCountResponse : deptCountResponseList) {
            sheet.createRow(rowIndex).createCell(cellIndex).setCellValue(deptCountResponse.getName());
            CellRangeAddress region = new CellRangeAddress(rowIndex, rowIndex, cellIndex, cellIndex + 1);
            sheet.addMergedRegion(region);
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
        sheet.createRow(rowIndex);
        changeIndex();

        //按部门再按状态统计问题的数量
        addTitle(sheet, style, "各部门各状态的问题统计");
        changeIndex();
        DeptInductionResponse[] induction = countService.deptInductionSort(request);
        row = sheet.createRow(rowIndex);
        row.createCell(cellIndex).setCellValue("部门");
        row.createCell(cellIndex + 1).setCellValue("待解决问题/待解决预警/已解决问题和预警");
        CellRangeAddress region = new CellRangeAddress(rowIndex, rowIndex, cellIndex + 1, cellIndex + 5);
        sheet.addMergedRegion(region);
        changeIndex();
        row = sheet.createRow(rowIndex);
        for (int i = 0; i < induction.length; i++) {
            row.createCell(cellIndex).setCellValue(induction[i].getDept());
            String unhandleIssue = getStringCount(induction[i].getData(), IssueIndicator.UN_SOLVED_ISSUE);
            String unhandleWarning = getStringCount(induction[i].getData(), IssueIndicator.WARNING);
            String handled = getStringCount(induction[i].getData(), IssueIndicator.SOLVED_ALL);
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
        sheet.createRow(rowIndex);
        changeIndex();

        //按问题类型再按部门统计未处理的问题数量
        addTitle(sheet, style, "问题分类统计");
        changeIndex();
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

        //创建目录
        File dir = new File(reportDir + fileDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        //写入文件
        try (FileOutputStream out = new FileOutputStream(reportDir + fileDir + fileName)) {
            workbook.write(out);
        } catch (IOException e) {
            log.error("", e);
            LogUtil.addErrorLog(OperationType.REQUEST, ErrorType.REQUEST_FAILED, "", e);
        }
        report.setPath(fileDir + fileName);
        report.setCrTime(new Date());
        //入库
        reportMapper.insert(report);
        Date endTime = new Date();
        LogUtil.addElapseLog(OperationType.TASK_SCHEDULE, SchedulerType.PERFORMANCE_SCHEDULER.intern(), endTime.getTime()-startTime.getTime());
        log.info(SchedulerType.schedulerEnd(SchedulerType.REPORT_GENERATE_SCHEDULER, siteId));
        LogUtil.addDebugLog(OperationType.TASK_SCHEDULE, DebugType.MONITOR_END, SchedulerType.schedulerStart(SchedulerType.REPORT_GENERATE_SCHEDULER, siteId));
    }

    private void addTitle(Sheet sheet, CellStyle style, String title) {
        Cell cell = sheet.createRow(rowIndex).createCell(cellIndex);
        cell.setCellValue(title);
        cell.setCellStyle(style);
        CellRangeAddress region = new CellRangeAddress(rowIndex, rowIndex, cellIndex, cellIndex + 20);
        sheet.addMergedRegion(region);
    }

    /**
     * 将问题的历史记录写入excel
     *
     * @param data
     * @param row
     * @param cellIndex
     */
    private void writeHistoryCount(List<HistoryStatistics> data, Row row, int cellIndex) {
        int tempIndex = cellIndex;
        for (HistoryStatistics historyStatistics : data) {
            row.createCell(tempIndex).setCellValue(historyStatistics.getTime());
            row.createCell(tempIndex + 1).setCellValue(historyStatistics.getValue());
            row.createCell(tempIndex + 2).setCellValue("");
            tempIndex += 3;
        }
    }

    /**
     * 获得各状态的问题统计数量
     *
     * @param data
     * @param indicator
     * @return
     */
    private String getStringCount(List<Statistics> data, IssueIndicator indicator) {
        for (Statistics statistics : data) {
            if (statistics.getType() == indicator.value) {
                return Integer.toString(statistics.getCount());
            }
        }
        return "";
    }

    /**
     * 将各部门的数据写入excel
     *
     * @param deptCountList
     * @param sheet
     * @param row
     * @param index
     */
    private void writeDeptCount(List<DeptCount> deptCountList, Sheet sheet, Row row, int index) {
        Row tempRow = row;
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


    /**
     * 索引指向下一行第一个单元格
     */
    private void changeIndex() {
        rowIndex++;
        cellIndex = 0;
    }


}
