package com.trs.gov.kpi.service.impl.wangkang;

import com.trs.gov.kpi.constant.Status;
import com.trs.gov.kpi.constant.Types;
import com.trs.gov.kpi.constant.WkIssueTableField;
import com.trs.gov.kpi.dao.*;
import com.trs.gov.kpi.entity.dao.QueryFilter;
import com.trs.gov.kpi.entity.dao.Table;
import com.trs.gov.kpi.entity.exception.BizException;
import com.trs.gov.kpi.entity.requestdata.PageDataRequestParam;
import com.trs.gov.kpi.entity.responsedata.*;
import com.trs.gov.kpi.entity.wangkang.*;
import com.trs.gov.kpi.service.helper.QueryFilterHelper;
import com.trs.gov.kpi.service.wangkang.WkAllStatsService;
import com.trs.gov.kpi.service.wangkang.WkOneSiteDetailService;
import com.trs.gov.kpi.service.wangkang.WkScoreService;
import com.trs.gov.kpi.service.wangkang.WkSiteManagementService;
import com.trs.gov.kpi.utils.*;
import com.trs.gov.kpi.utils.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by li.hao on 2017/7/12.
 */
@Service
@Slf4j
class WkOneSiteDetailServiceImpl implements WkOneSiteDetailService {

    @Resource
    private WkScoreService wkScoreService;

    @Resource
    private WkSiteManagementService wkSiteManagementService;

    @Resource
    private WkAllStatsService wkAllStatsService;

    @Resource
    WkSiteDetailMapper wkSiteDetailMapper;

    @Resource
    WkIssueCountMapper wkIssueCountMapper;

    @Resource
    WkIssueMapper wkIssueMapper;

    @Resource
    WkLinkTypeMapper wkLinkTypeMapper;

    @Resource
    WkSiteManagementMapper wkSiteManagementMapper;

    @Resource
    private WkCheckTimeMapper wkCheckTimeMapper;

    @Resource
    private CommonMapper commonMapper;

    @Value("${wk.report.dir}")
    private String wkReportDir;

    @Override
    public WkLinkTypeResponse getOneSiteLinkTypeBySiteId(Integer siteId) {
        WkLinkType wkLinkType = wkLinkTypeMapper.getOneSiteLinkTypeBySiteId(siteId);
        WkLinkTypeResponse wkLinkTypeResponse = new WkLinkTypeResponse();
        if (wkLinkType != null){
            wkLinkTypeResponse.setAllLink(wkLinkType.getAllLink());
            wkLinkTypeResponse.setWebLink(wkLinkType.getWebLink());
            wkLinkTypeResponse.setImageLink(wkLinkType.getImageLink());
            wkLinkTypeResponse.setVideoLink(wkLinkType.getVideoLink());
            wkLinkTypeResponse.setEnclosuLink(wkLinkType.getEnclosuLink());
        }
        return wkLinkTypeResponse;
    }

    @Override
    public WkOneSiteScoreResponse getOneSiteScoreBySiteId(Integer siteId) {
        WkScore wkScore = wkSiteDetailMapper.getOneSiteScoreBySiteId(siteId);
        WkOneSiteScoreResponse wkOneSiteScore = new WkOneSiteScoreResponse();
        if(wkScore != null){
            wkOneSiteScore.setTotal(wkScore.getTotal());
            wkOneSiteScore.setContentError(wkScore.getContentError());
            wkOneSiteScore.setInvalidLink(wkScore.getInvalidLink());
            wkOneSiteScore.setOverSpeed(wkScore.getOverSpeed());
            wkOneSiteScore.setUpdateContent(wkScore.getUpdateContent());
        }
        return wkOneSiteScore;
    }

    /*---链接可用性---*/
    @Override
    public WkStatsCountResponse getInvalidlinkStatsBySiteId(Integer siteId) {
        Integer typeId = Types.WkSiteCheckType.INVALID_LINK.value;

        WkIssueCount wkIssueCount = wkIssueCountMapper.getlinkAndContentStatsBySiteId(siteId,typeId);
        WkStatsCountResponse wkStatsCountResponse = new WkStatsCountResponse();
        if(wkIssueCount != null){
            wkStatsCountResponse.setUnhandleIssue(wkIssueCount.getUnResolved());
            wkStatsCountResponse.setHandleIssue(wkIssueCount.getIsResolved());
        }
        return wkStatsCountResponse;
    }

    @Override
    public List<WkStatsCountResponse> getInvalidlinkHistoryStatsBySiteId(Integer siteId) {
        Integer typeId = Types.WkSiteCheckType.INVALID_LINK.value;

        List<WkIssueCount> wkIssueCountList = wkIssueCountMapper.getlinkAndContentHistoryStatsBySiteId(siteId, typeId);
        List<WkStatsCountResponse> wkStatsCountResponseList = new ArrayList<>();

        if(!wkIssueCountList.isEmpty()){
            for (WkIssueCount wkIssueCount : wkIssueCountList) {
                WkStatsCountResponse wkStatsCountResponse = new WkStatsCountResponse();
                wkStatsCountResponse.setHandleIssue(wkIssueCount.getIsResolved());
                wkStatsCountResponse.setUnhandleIssue(wkIssueCount.getUnResolved());
                wkStatsCountResponse.setCheckTime(wkIssueCount.getCheckTime());

                wkStatsCountResponseList.add(wkStatsCountResponse);
            }
        }
        return wkStatsCountResponseList;
    }

    @Override
    public WkLinkIndexPageStatus getSiteIndexpageStatusBySiteId(Integer siteId) {
        Integer isDel = Status.Delete.UN_DELETE.value;
        SiteManagement siteManagement = wkSiteManagementMapper.getSiteIndexpageStatusBySiteId(siteId, isDel);
        WkLinkIndexPageStatus wkLinkIndexPageStatus = new WkLinkIndexPageStatus();
        if(siteManagement != null){
            wkLinkIndexPageStatus.setCheckTime(siteManagement.getCheckTime());
            wkLinkIndexPageStatus.setSiteIndexUrl(siteManagement.getSiteIndexUrl());
        }
        return wkLinkIndexPageStatus;
    }

    @Override
    public ApiPageData getInvalidLinkUnhandledList(PageDataRequestParam param) {
        if(!StringUtil.isEmpty(param.getSearchText())){
            param.setSearchText(StringUtil.escape(param.getSearchText()));
        }

        Integer siteId = param.getSiteId();
        final Integer maxCheckId = wkCheckTimeMapper.getMaxCheckId(siteId);
        if (maxCheckId == null) {
            Pager pager = PageInfoDeal.buildResponsePager(param.getPageIndex(), param.getPageSize(), 0);
            return new ApiPageData(pager, Collections.emptyList());
        }

        QueryFilter queryFilter = QueryFilterHelper.toWkIssueFilter(param, Types.WkSiteCheckType.INVALID_LINK);
        queryFilter.addCond(WkIssueTableField.TYPE_ID, Types.WkSiteCheckType.INVALID_LINK.value);
        queryFilter.addCond(WkIssueTableField.IS_RESOLVED, Status.Resolve.UN_RESOLVED.value);
        queryFilter.addCond(WkIssueTableField.CHECK_ID, maxCheckId);
        int itemCount = commonMapper.count(queryFilter);

        Pager pager = PageInfoDeal.buildResponsePager(param.getPageIndex(), param.getPageSize(), itemCount);
        queryFilter.setPager(pager);
        List<WkIssue> wkIssueList = wkIssueMapper.select(queryFilter);

        return new ApiPageData(pager, toWkLinkIssueResponseByWkIssueList(wkIssueList));
    }

    private List<WkIssueResponse> toWkLinkIssueResponseByWkIssueList(List<WkIssue> wkIssueList){
        List<WkIssueResponse> wkIssueResponseList = new ArrayList<>();

        for (WkIssue wkIssue: wkIssueList) {
            WkIssueResponse wkIssueResponse = new WkIssueResponse();

            wkIssueResponse.setId(wkIssue.getId());
            wkIssueResponse.setChnlName(wkIssue.getChnlName());
            wkIssueResponse.setSubTypeId(wkIssue.getSubTypeId());
            wkIssueResponse.setSubTypeName(Types.WkLinkIssueType.valueOf(wkIssue.getSubTypeId()).getName());
            wkIssueResponse.setUrl(wkIssue.getUrl());
            wkIssueResponse.setParentUrl(wkIssue.getParentUrl());
            wkIssueResponse.setLocationUrl(wkIssue.getLocationUrl());

            wkIssueResponseList.add(wkIssueResponse);
        }
        return wkIssueResponseList;
    }

    /*---内容检测---*/
    @Override
    public WkStatsCountResponse getContentErorStatsBySiteId(Integer siteId) {
        Integer typeId = Types.WkSiteCheckType.CONTENT_ERROR.value;

        WkIssueCount wkIssueCount = wkIssueCountMapper.getlinkAndContentStatsBySiteId(siteId, typeId);
        WkStatsCountResponse wkStatsCountResponse = new WkStatsCountResponse();
        if(wkIssueCount != null){
            wkStatsCountResponse.setUnhandleIssue(wkIssueCount.getUnResolved());
            wkStatsCountResponse.setHandleIssue(wkIssueCount.getIsResolved());
        }
        return wkStatsCountResponse;
    }

    @Override
    public List<WkStatsCountResponse> getContentErorHistoryStatsBySiteId(Integer siteId) {
        Integer typeId = Types.WkSiteCheckType.CONTENT_ERROR.value;

        List<WkIssueCount> wkIssueCountList = wkIssueCountMapper.getlinkAndContentHistoryStatsBySiteId(siteId, typeId);
        List<WkStatsCountResponse> wkStatsCountResponseList = new ArrayList<>();

        if(!wkIssueCountList.isEmpty()){
            for (WkIssueCount wkIssueCount : wkIssueCountList) {
                WkStatsCountResponse wkStatsCountResponse = new WkStatsCountResponse();
                wkStatsCountResponse.setHandleIssue(wkIssueCount.getIsResolved());
                wkStatsCountResponse.setUnhandleIssue(wkIssueCount.getUnResolved());
                wkStatsCountResponse.setCheckTime(wkIssueCount.getCheckTime());

                wkStatsCountResponseList.add(wkStatsCountResponse);
            }
        }
        return wkStatsCountResponseList;
    }

    @Override
    public ApiPageData getContentErrorUnhandledList(PageDataRequestParam param) {
        if(!StringUtil.isEmpty(param.getSearchText())){
            param.setSearchText(StringUtil.escape(param.getSearchText()));
        }

        Integer siteId = param.getSiteId();
        final Integer maxCheckId = wkCheckTimeMapper.getMaxCheckId(siteId);
        if (maxCheckId == null) {
            Pager pager = PageInfoDeal.buildResponsePager(param.getPageIndex(), param.getPageSize(), 0);
            return new ApiPageData(pager, Collections.emptyList());
        }

        QueryFilter queryFilter = QueryFilterHelper.toWkIssueFilter(param, Types.WkSiteCheckType.CONTENT_ERROR);
        queryFilter.addCond(WkIssueTableField.TYPE_ID, Types.WkSiteCheckType.CONTENT_ERROR.value);
        queryFilter.addCond(WkIssueTableField.IS_RESOLVED, Status.Resolve.UN_RESOLVED.value);
        queryFilter.addCond(WkIssueTableField.CHECK_ID, maxCheckId);
        int itemCount = commonMapper.count(queryFilter);

        Pager pager = PageInfoDeal.buildResponsePager(param.getPageIndex(), param.getPageSize(), itemCount);
        queryFilter.setPager(pager);
        List<WkIssue> wkIssueList = wkIssueMapper.select(queryFilter);

        return new ApiPageData(pager, toWkContentIssueResponseByWkIssueList(wkIssueList));
    }

    private List<WkIssueResponse> getAllContentErrorList(Integer siteId) {
        final Integer maxCheckId = wkCheckTimeMapper.getMaxCheckId(siteId);
        if (maxCheckId == null) {
            return Collections.emptyList();
        }

        QueryFilter queryFilter = new QueryFilter(Table.WK_ISSUE);
        queryFilter.addCond(WkIssueTableField.TYPE_ID, Types.WkSiteCheckType.CONTENT_ERROR.value);
        queryFilter.addCond(WkIssueTableField.IS_RESOLVED, Status.Resolve.UN_RESOLVED.value);
        queryFilter.addCond(WkIssueTableField.CHECK_ID, maxCheckId);
        queryFilter.addSortField(WkIssueTableField.ID);
        List<WkIssue> wkIssueList = wkIssueMapper.select(queryFilter);

        return toWkContentIssueResponseByWkIssueList(wkIssueList);
    }

    private List<WkIssueResponse> getAllInvalidLinkList(Integer siteId) {
        final Integer maxCheckId = wkCheckTimeMapper.getMaxCheckId(siteId);
        if (maxCheckId == null) {
            return Collections.emptyList();
        }

        QueryFilter queryFilter = new QueryFilter(Table.WK_ISSUE);
        queryFilter.addCond(WkIssueTableField.TYPE_ID, Types.WkSiteCheckType.INVALID_LINK.value);
        queryFilter.addCond(WkIssueTableField.IS_RESOLVED, Status.Resolve.UN_RESOLVED.value);
        queryFilter.addCond(WkIssueTableField.CHECK_ID, maxCheckId);
        queryFilter.addSortField(WkIssueTableField.ID);
        List<WkIssue> wkIssueList = wkIssueMapper.select(queryFilter);

        return toWkContentIssueResponseByWkIssueList(wkIssueList);
    }

    @Override
    public String generateReport(Integer siteId) throws BizException {

        // 5个标签页:综合评分，网站断链检测，网站内容检测，访问速度检测，网站更新检测

        try {
            FileUtils.forceMkdir(new File(wkReportDir + File.separator + siteId));
        } catch (IOException e) {
            log.error("", e);
            throw new BizException("创建报表文件失败！");
        }

        String fileName = new Date().getTime() + ".xlsx";
        Workbook workbook = new XSSFWorkbook();
        CellStyle style = createStyle(workbook);

        // 综合评分
        Sheet sheet = workbook.createSheet("综合评分");
        createScoresReport(sheet, style, siteId);

        // 网站断链检测
        Sheet linkSheet = workbook.createSheet("网站断链检测");
        createInvalidLinkReport(linkSheet, style, siteId);

        // 网站内容检测
        sheet = workbook.createSheet("网站内容检测");
        createErrorWordsReport(sheet, style, siteId);

        // 访问速度检测
        sheet = workbook.createSheet("访问速度检测");
        createAvgSpeedReport(sheet, style, siteId);

        // 网站更新检测
        sheet = workbook.createSheet("网站更新检测");
        createUpdatePageReport(sheet, style, siteId);

        //写入文件
        String fullReportFilePath = wkReportDir + File.separator + siteId + File.separator + fileName;
        try (FileOutputStream out = new FileOutputStream(fullReportFilePath)) {
            workbook.write(out);
        } catch (IOException e) {
            log.error("", e);
            throw new BizException("创建报表文件失败！");
        }

        return fullReportFilePath;
    }

    private void createUpdatePageReport(Sheet sheet, CellStyle style, Integer siteId) {
        int beginRow = 0;
        addTitle(sheet, style, "网站更新走势", beginRow, beginRow, 0, 1);
        beginRow++;

        addRow(sheet, beginRow, "检测时间", "更新数量");
        beginRow++;

        List<WkUpdateContentResponse> updateHistory = wkAllStatsService.getUpdateContentHistory(siteId);
        for (WkUpdateContentResponse update : updateHistory) {
            addRow(sheet, beginRow, DateUtil.toString(update.getCheckTime()), update.getUpdateContent());
            beginRow++;
        }
    }

    private void createAvgSpeedReport(Sheet sheet, CellStyle style, Integer siteId) {
        int beginRow = 0;
        addTitle(sheet, style, "访问速度走势", beginRow, beginRow, 0, 1);
        beginRow++;

        addRow(sheet, beginRow, "检测时间", "平均访问速度(ms)");
        beginRow++;

        List<WkAvgSpeedResponse> avgSpeedHistory = wkAllStatsService.getAvgSpeedHistory(siteId);
        for (WkAvgSpeedResponse speed : avgSpeedHistory) {
            addRow(sheet, beginRow, DateUtil.toString(speed.getCheckTime()), speed.getAvgSpeed());
            beginRow++;
        }
    }

    private void createErrorWordsReport(Sheet sheet, CellStyle style, Integer siteId) {
//        序号	错误类型	所属栏目	标题	疑似错误详情	地址	父链接地址
//        1	疑似错别字	其它	国务院办公厅关于印发推行行政执法公示制度执法全过程记录制度重大执法决定法制审核制度试点工作方案的通知	关键词：佩带,应为：佩戴	http://www.nxww.gov.cn/news.jsp?id=1316&soncatalog_id=9	http://www.nxww.gov.cn/lanmu_zc.jsp

        int beginRow = 0;
        addTitle(sheet, style, "问题统计数", beginRow, beginRow, 0, 5);
        beginRow++;

        WkStatsCountResponse errorWordsStats = getContentErorStatsBySiteId(siteId);
        addRow(sheet, beginRow, "已解决问题", errorWordsStats.getHandleIssue());
        beginRow++;
        addRow(sheet, beginRow, "未解决问题", errorWordsStats.getUnhandleIssue());
        beginRow++;

        // 空一行
        beginRow++;
        addTitle(sheet, style, "问题走势", beginRow, beginRow, 0, 5);
        beginRow++;
        addRow(sheet, beginRow, "检测时间", "已解决问题数", "未解决问题数");
        beginRow++;
        List<WkStatsCountResponse> errorWordsHistoryStats = getContentErorHistoryStatsBySiteId(siteId);
        for (WkStatsCountResponse count : errorWordsHistoryStats) {
            addRow(sheet, beginRow, DateUtil.toString(count.getCheckTime()), count.getHandleIssue(), count.getUnhandleIssue());
            beginRow++;
        }

        // 空一行
        beginRow++;
        addTitle(sheet, style, "问题列表", beginRow, beginRow, 0, 5);
        beginRow++;
        addRow(sheet, beginRow, "序号", "所属栏目", "错误类型", "疑似错误详情", "地址", "父链接地址", "定位地址");
        beginRow++;
        List<WkIssueResponse> allErrorWordsList = getAllContentErrorList(siteId);
        for (int index = 0; index < allErrorWordsList.size(); index++) {
            WkIssueResponse issue = allErrorWordsList.get(index);
            addRow(sheet, beginRow, index+1, issue.getChnlName(), issue.getSubTypeName(), issue.getErrorInfo(),
                    issue.getUrl(), issue.getParentUrl(), issue.getLocationUrl());
            beginRow++;
        }
    }

    private void createInvalidLinkReport(Sheet sheet, CellStyle style, Integer siteId) {
//        序号	错误类型	所属栏目	标题	地址	父链接地址
//        1	外部断链	其它	www.cqnx.cc	http://www.cqnx.cc	http://www.nxww.gov.cn/news.jsp?id=462&soncatalog_id=1

        int beginRow = 0;
        addTitle(sheet, style, "问题统计数", beginRow, beginRow, 0, 5);
        beginRow++;

        WkStatsCountResponse invalidlinkStats = getInvalidlinkStatsBySiteId(siteId);
        addRow(sheet, beginRow, "已解决问题", invalidlinkStats.getHandleIssue());
        beginRow++;
        addRow(sheet, beginRow, "未解决问题", invalidlinkStats.getUnhandleIssue());
        beginRow++;

        // 空一行
        beginRow++;
        addTitle(sheet, style, "问题走势", beginRow, beginRow, 0, 5);
        beginRow++;
        addRow(sheet, beginRow, "检测时间", "已解决问题数", "未解决问题数");
        beginRow++;
        List<WkStatsCountResponse> invalidlinkHistoryStats = getInvalidlinkHistoryStatsBySiteId(siteId);
        for (WkStatsCountResponse count : invalidlinkHistoryStats) {
            addRow(sheet, beginRow, DateUtil.toString(count.getCheckTime()), count.getHandleIssue(), count.getUnhandleIssue());
            beginRow++;
        }

        // 空一行
        beginRow++;
        addTitle(sheet, style, "问题列表", beginRow, beginRow, 0, 5);
        beginRow++;
        addRow(sheet, beginRow, "序号", "所属栏目", "类型", "地址", "父链接地址", "定位地址");
        beginRow++;
        List<WkIssueResponse> allInvalidLinkList = getAllInvalidLinkList(siteId);
        for (int index = 0; index < allInvalidLinkList.size(); index++) {
            WkIssueResponse issue = allInvalidLinkList.get(index);
            addRow(sheet, beginRow, index+1, issue.getChnlName(), issue.getSubTypeName(), issue.getUrl(),
                    issue.getParentUrl(), issue.getLocationUrl());
            beginRow++;
        }
    }

    private CellStyle createStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setFillForegroundColor(HSSFColor.GREEN.index);//设置图案颜色
        style.setFillBackgroundColor(HSSFColor.GREEN.index);//设置图案背景色
        style.setFillPattern(HSSFCellStyle.LESS_DOTS);//设置图案样式
        Font font = workbook.createFont();
        font.setFontName("Times New Roman");//设置字体名称
        font.setFontHeightInPoints((short) 17);//设置字号
        font.setColor(HSSFColor.WHITE.index);//设置字体颜色
        style.setFont(font);
        return style;
    }



    private void createScoresReport(Sheet sheet, CellStyle style, Integer siteId) {
        int beginRow = 0;
        addTitle(sheet, style, "综合评分", beginRow, beginRow, 0, 1);
        beginRow++;

//        评分项目	分值
//        综合评分（满分100分）	87
//
//        网站链接（单项满分100分）	98
//        内容监控（单项满分100分）	96
//        访问速度（单项满分100分）	89
//        网站更新（单项满分100分）	56
//
//        网站ID	5256
//        网站名	宁夏回族自治区无线电管理委员会办公室
//        URL地址	http://www.nxww.gov.cn
//        导出时间	2017-07-04 11:11:07

        addRow(sheet, beginRow, "评分项目", "分值");
        beginRow++;

        WkOneSiteScoreResponse oneSiteScore = getOneSiteScoreBySiteId(siteId);
        addRow(sheet, beginRow, "综合评分（满分100分）", oneSiteScore.getTotal());
        beginRow++;
        // 空行
        beginRow++;

        addRow(sheet, beginRow, "网站链接（单项满分100分）", oneSiteScore.getInvalidLink());
        beginRow++;

        addRow(sheet, beginRow, "内容监控（单项满分100分）", oneSiteScore.getContentError());
        beginRow++;

        addRow(sheet, beginRow, "访问速度（单项满分100分）", oneSiteScore.getOverSpeed());
        beginRow++;

        addRow(sheet, beginRow, "网站更新（单项满分100分）", oneSiteScore.getUpdateContent());
        beginRow++;

        // 空行
        beginRow++;
        SiteManagement site = wkSiteManagementService.getSiteManagementBySiteId(siteId);
        addRow(sheet, beginRow, "网站名", site.getSiteName());
        beginRow++;
        addRow(sheet, beginRow, "URL地址", site.getSiteIndexUrl());
        beginRow++;
        addRow(sheet, beginRow, "导出时间", com.trs.gov.kpi.utils.DateUtil.toString(new Date()));
        beginRow++;

        // 空行
        beginRow++;
        List<WkOneSiteScoreResponse> scores = wkScoreService.getListBySiteId(siteId);
        addTitle(sheet, style, "综合评分走势", beginRow, beginRow, 0, 5);
        beginRow++;

        addRow(sheet, beginRow, "检测时间", "综合评分", "网站链接", "内容监控", "访问速度", "网站更新");
        beginRow++;

        for (WkOneSiteScoreResponse score: scores) {
            addRow(sheet, beginRow, DateUtil.toString(score.getCheckTime()), score.getTotal(), score.getInvalidLink(),
                    score.getContentError(), score.getOverSpeed(), score.getUpdateContent());
            beginRow++;
        }
    }

    private void addRow(Sheet sheet, int rowIndex, Object... values) {
        Row row = sheet.createRow(rowIndex);
        for (int index = 0; index < values.length; index++) {
            row.createCell(index).setCellValue(values[index].toString());
        }
    }

    private void addTitle(Sheet sheet, CellStyle style, String title, int rowIndex, int endRow, int cellIndex, int endCell) {
        Cell cell = sheet.createRow(rowIndex).createCell(cellIndex);
        cell.setCellValue(title);
        cell.setCellStyle(style);
        CellRangeAddress region = new CellRangeAddress(rowIndex, endRow, cellIndex, endCell);
        sheet.addMergedRegion(region);
    }

    private List<WkIssueResponse> toWkContentIssueResponseByWkIssueList(List<WkIssue> wkIssueList){
        List<WkIssueResponse> wkIssueResponseList = new ArrayList<>();

        for (WkIssue wkIssue: wkIssueList) {
            WkIssueResponse wkIssueResponse = new WkIssueResponse();

            wkIssueResponse.setId(wkIssue.getId());
            wkIssueResponse.setChnlName(wkIssue.getChnlName());
            wkIssueResponse.setSubTypeId(wkIssue.getSubTypeId());

            // TODO 需要修改一下界面显示的名称和错误信息
            wkIssueResponse.setSubTypeName(Types.InfoErrorIssueType.valueOf(wkIssue.getSubTypeId()).getDisplayName());
            wkIssueResponse.setErrorInfo(wkIssue.getDetailInfo());

            wkIssueResponse.setUrl(wkIssue.getUrl());
            wkIssueResponse.setParentUrl(wkIssue.getParentUrl());
            wkIssueResponse.setLocationUrl(wkIssue.getLocationUrl());

            wkIssueResponseList.add(wkIssueResponse);
        }
        return wkIssueResponseList;
    }

}
