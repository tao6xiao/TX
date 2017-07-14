package com.trs.gov.kpi.processor;

import com.alibaba.fastjson.JSONObject;
import com.trs.gov.kpi.constant.IssueTableField;
import com.trs.gov.kpi.constant.Status;
import com.trs.gov.kpi.constant.Types;
import com.trs.gov.kpi.dao.IssueMapper;
import com.trs.gov.kpi.entity.InfoError;
import com.trs.gov.kpi.entity.Issue;
import com.trs.gov.kpi.entity.dao.QueryFilter;
import com.trs.gov.kpi.entity.msg.PageInfoMsg;
import com.trs.gov.kpi.entity.outerapi.ContentCheckResult;
import com.trs.gov.kpi.entity.outerapi.Document;
import com.trs.gov.kpi.entity.requestdata.PageDataRequestParam;
import com.trs.gov.kpi.service.helper.QueryFilterHelper;
import com.trs.gov.kpi.service.outer.ContentCheckApiService;
import com.trs.gov.kpi.utils.CollectionUtil;
import com.trs.gov.kpi.utils.DBUtil;
import com.trs.gov.kpi.utils.StringUtil;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by li.hao on 2017/7/11.
 */
@Slf4j
@Component
@Scope("prototype")
public class CKMProcessWorker implements Runnable {

    private static final String LINE_SP = System.getProperty("line.separator");

    @Resource
    private ContentCheckApiService contentCheckApiService;

    @Resource
    private IssueMapper issueMapper;

    @Setter
    private PageInfoMsg content;

    @Override
    public void run() {
        buildList();
    }

    private List<Issue> buildList() {
        List<Issue> issueList = new ArrayList<>();
        List<String> checkTypeList = Types.InfoErrorIssueType.getAllCheckTypes();

        String cleanText = Jsoup.clean(content.getContent(), Whitelist.none());
        cleanText = cleanText.replaceAll("&nbsp", " ");

        ContentCheckResult result = null;
        try {
            result = contentCheckApiService.check(cleanText, CollectionUtil.join(checkTypeList, ";"));
        } catch (Exception e) {
            log.error("failed to check content of url [" + content.getUrl() + "]", e);
            return issueList;
        }

        if (!result.isOk()) {
            log.error("return error: " + result.getMessage() + ", url [" + content.getUrl() + "]");
            return issueList;
        }

        if (result.getResult() != null) {
            issueList = toIssueList(checkTypeList, result);
        }
        return issueList;
    }

    private List<Issue> toIssueList(List<String> checkTypeList, ContentCheckResult result) {
        List<Issue> issueList = new ArrayList<>();
        for (String checkType : checkTypeList) {
            Types.InfoErrorIssueType subIssueType = Types.InfoErrorIssueType.valueOfCheckType(checkType);
            String errorContent = result.getResultOfType(subIssueType);
            if (StringUtil.isEmpty(errorContent)) {
                continue;
            } else {
                final Set<Map.Entry<String, Object>> entries = JSONObject.parseObject(errorContent).entrySet();
                for (Map.Entry<String, Object> entry : entries) {
                    String errorInfo = entry.getKey();
                    final String[] infos = errorInfo.split(":");
                    String word = infos[0];
                    String correct = infos[1];





                }
            }
        }
        return issueList;
    }

    private String generateLocationPage(Types.InfoErrorIssueType type, String errorWord, String correct) {

        String result = "";
        StringBuffer sb = new StringBuffer();
        // 给网站增加base标签
        sb.append("<base href=\"" + content.getUrl() + "\" />");
        sb.append(LINE_SP);
        sb.append(content.getContent());
        // 在源码中增加定位用的脚本定义
        sb.append(LINE_SP);
        sb.append("<link href=\"http://gov.trs.cn/jsp/cis4/css/jquery.qtip.min.css\" rel=\"stylesheet\" type=\"text/css\">");
        sb.append(LINE_SP);
        sb.append("<script type=\"text/javascript\" src=\"http://gov.trs.cn/jsp/cis4/js/jquery.js\"></script>");
        sb.append(LINE_SP);
        sb.append("<script type=\"text/javascript\" src=\"http://gov.trs.cn/jsp/cis4/js/jquery.qtip.min.js\"></script>");
        sb.append(LINE_SP);
        sb.append("<script type=\"text/javascript\" src=\"http://gov.trs.cn/jsp/cis4/js/trsposition.js\"></script>");
        // 解析源码，找到断链标签，加上标记信息。
        result = sb.toString();

        int index = result.indexOf(errorWord, 0);
        while (index != -1) {
            String errorinfo = "<font trserrid=\"anchor\" msg=\"" + getDisplayErrorWord(type, errorWord, correct) + "\" msgtitle=\"定位\" style=\"border:2px red solid;color:red;\">" + errorWord + "</font>";
            result = result.substring(0, index) + errorinfo + result.substring(index + errorWord.length());
            index = result.indexOf(errorWord, index + errorinfo.length());
        }

        result += "<div id=\"qtip-0\" class=\"qtip qtip-default  qtip-focus qtip-pos-br\" tracking=\"false\" role=\"alert\" aria-live=\"polite\" aria-atomic=\"false\" aria-describedby=\"qtip-0-content\" aria-hidden=\"false\" data-qtip-id=\"0\" style=\"z-index: 15001; top: 2705px; left: 430.453px; display: block;\">\n" +
                "\t<div class=\"qtip-tip\" style=\"background-color: transparent !important; border: 0px !important; width: 8px; height: 8px; line-height: 8px; right: -1px; bottom: -8px;\">\n" +
                "\t\t<canvas width=\"8\" height=\"8\" style=\"background-color: transparent !important; border: 0px !important; width: 8px; height: 8px;\"></canvas>\n" +
                "\t</div>\n" +
                "\t<div class=\"qtip-titlebar\">\n" +
                "\t\t<div id=\"qtip-0-title\" class=\"qtip-title\" aria-atomic=\"true\">定位</div>\n" +
                "\t\t<a class=\"qtip-close qtip-icon\" title=\"close\" aria-label=\"close\" role=\"button\">\n" +
                "\t\t\t<span class=\"ui-icon ui-icon-close\">×</span>\n" +
                "\t\t</a>\n" +
                "\t</div>\n" +
                "\t<div class=\"qtip-content\" id=\"qtip-0-content\" aria-atomic=\"true\">" + getDisplayErrorWord(type, errorWord, correct) + "</div>\n" +
                "</div>";

        return result;
    }

    private String getDisplayErrorWord(Types.InfoErrorIssueType type, String errorWord, String correctWord) {
        String result = "疑似使用错误的关键字：" + errorWord;
        if (!StringUtil.isEmpty(correctWord)) {
            result += "，应为：" + errorWord;
        }
        return result;
    }

    private void createFile(String fullFilePath, String content) {
//        String fileName = "C://11.txt";
//        File file = new File(fileName);
//        String fileContent = "";
//        try {
//            fileContent = org.apache.commons.io.FileUtils.readFileToString(file, "GBK");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        fileContent +="Helloworld";
//        try {
//            org.apache.commons.io.FileUtils.writeStringToFile(file, fileContent, "GBK");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    public void insert(List<Issue> issueList) {
        //插入监测出的信息错误数据
        for (Issue issue : issueList) {

            PageDataRequestParam param = new PageDataRequestParam();
            param.setSiteId(content.getSiteId());
            QueryFilter queryFilter = QueryFilterHelper.toFilter(param);
            queryFilter.addCond(IssueTableField.TYPE_ID, Types.IssueType.INFO_ERROR_ISSUE.value);
            queryFilter.addCond(IssueTableField.IS_RESOLVED, Status.Resolve.UN_RESOLVED.value);
            queryFilter.addCond(IssueTableField.IS_DEL, Status.Delete.UN_DELETE.value);
            queryFilter.addCond(IssueTableField.CUSTOMER1, issue.getCustomer1());
            queryFilter.addCond(IssueTableField.SUBTYPE_ID, issue.getSubTypeId());
            queryFilter.addCond(IssueTableField.DETAIL, issue.getDetail());

            List<InfoError> infoErrors = issueMapper.selectInfoError(queryFilter);
            if(infoErrors.size() == 0){
                issueMapper.insert(DBUtil.toRow(issue));
            }
        }
        log.info("buildCheckContent insert error count: " + issueList.size());
    }
}
