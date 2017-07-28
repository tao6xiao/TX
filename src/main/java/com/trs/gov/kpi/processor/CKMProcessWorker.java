package com.trs.gov.kpi.processor;

import com.alibaba.fastjson.JSONObject;
import com.trs.gov.kpi.constant.IssueTableField;
import com.trs.gov.kpi.constant.Status;
import com.trs.gov.kpi.constant.Types;
import com.trs.gov.kpi.dao.CommonMapper;
import com.trs.gov.kpi.dao.IssueMapper;
import com.trs.gov.kpi.entity.InfoError;
import com.trs.gov.kpi.entity.Issue;
import com.trs.gov.kpi.entity.dao.QueryFilter;
import com.trs.gov.kpi.entity.msg.PageInfoMsg;
import com.trs.gov.kpi.entity.outerapi.ContentCheckResult;
import com.trs.gov.kpi.entity.requestdata.PageDataRequestParam;
import com.trs.gov.kpi.entity.wangkang.WkIssue;
import com.trs.gov.kpi.service.helper.QueryFilterHelper;
import com.trs.gov.kpi.service.outer.ContentCheckApiService;
import com.trs.gov.kpi.utils.CollectionUtil;
import com.trs.gov.kpi.utils.DBUtil;
import com.trs.gov.kpi.utils.StringUtil;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Created by li.hao on 2017/7/11.
 */
@Slf4j
@Component
@Scope("prototype")
public class CKMProcessWorker implements Runnable {

    public static final String LINE_SP = System.getProperty("line.separator");

    @Value("${wk.location.dir}")
    private String locationDir;

    @Resource
    private ContentCheckApiService contentCheckApiService;

    @Resource
    private IssueMapper issueMapper;

    @Resource
    private CommonMapper commonMapper;

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
                int index = 0;
                for (Map.Entry<String, Object> entry : entries) {
                    index++;
                    String errorInfo = entry.getKey();
                    final String[] infos = errorInfo.split("：");
                    if (infos == null || infos.length > 2 || infos.length < 1) {
                        continue;
                    }
                    String errorWord = infos[0];
                    String correctWord = "";
                    if (infos.length >= 2) {
                        correctWord = infos[1];
                    }

                    try {
                        final String relativeDir = getRelativeDir(content.getSiteId(), content.getCheckId(), content.getUrl(), index, 1);
                        String absoluteDir = locationDir + File.separator + relativeDir;
                        createDir(absoluteDir);

                        // 网页定位
                        String pageLocContent = generatePageLocHtmlText(subIssueType, errorWord, correctWord);
                        if (pageLocContent == null) {
                            continue;
                        }
                        createPagePosHtml(absoluteDir, pageLocContent);

                        // 源码定位
                        String srcLocContent = generateSourceLocHtmlText(subIssueType, errorWord, correctWord);
                        if (srcLocContent == null) {
                            continue;
                        }
                        createSrcPosHtml(absoluteDir, srcLocContent);

                        // 创建头部导航页面
                        createContHtml(absoluteDir, content.getUrl(), content.getParentUrl());

                        // 创建首页
                        createIndexHtml(absoluteDir);

                        WkIssue issue = new WkIssue();
                        issue.setCheckId(content.getCheckId());
                        issue.setCheckTime(new Date());
                        issue.setLocationUrl("gov/wangkang/loc/" +  relativeDir.replace(File.separator, "/") + "/" + "index.html");
                        issue.setChnlName(getChnlName(content.getUrl()));
                        issue.setDetailInfo(getDisplayErrorWord(subIssueType, errorWord, correctWord));
                        if (StringUtil.isEmpty(content.getParentUrl())) {
                            issue.setParentUrl(content.getUrl());
                        } else {
                            issue.setParentUrl(content.getParentUrl());
                        }
                        issue.setUrl(content.getUrl());
                        issue.setSiteId(content.getSiteId());
                        issue.setTypeId(Types.WkSiteCheckType.CONTENT_ERROR.value);
                        issue.setSubTypeId(subIssueType.value);

                        commonMapper.insert(DBUtil.toRow(issue));
                    } catch (IOException e) {
                        log.error("error content: " + errorContent);
                        log.error("failed to generate file of " + content.getUrl() + ", siteid[" + content.getSiteId() + "] , checkid[" + content.getCheckId() + "]", e);
                    }
                }
            }
        }
        return issueList;
    }

    private String generatePageLocHtmlText(Types.InfoErrorIssueType type, String errorWord, String correct) {

        StringBuffer sb = new StringBuffer();
        // 给网站增加base标签
        sb.append("<base href=\"" + content.getUrl() + "\" />");
        sb.append(LINE_SP);
        sb.append(content.getContent().intern());
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
        String result = sb.toString();

        int index = result.indexOf(errorWord, 0);

        if (index == -1) {
            return null;
        } else {
            String errorinfo = "<font trserrid=\"anchor\" msg=\"" + getDisplayErrorWord(type, errorWord, correct) + "\" msgtitle=\"定位\" style=\"border:2px red solid;color:red;\">" + errorWord + "</font>";
            result = result.substring(0, index) + errorinfo + result.substring(index + errorWord.length());
            index = result.indexOf(errorWord, index + errorinfo.length());
        }

        while (index != -1) {
            String errorinfo = "<font msg=\"" + getDisplayErrorWord(type, errorWord, correct) + "\" style=\"border:2px red solid;color:red;\">" + errorWord + "</font>";
            result = result.substring(0, index) + errorinfo + result.substring(index + errorWord.length());
            index = result.indexOf(errorWord, index + errorinfo.length());
        }

        return result;
    }

    private String getDisplayErrorWord(Types.InfoErrorIssueType type, String errorWord, String correctWord) {
        String result = "";
        switch (type) {
            case TYPOS:
                result = "疑似错别字：" + errorWord;
                break;
            case POLITICS:
                result = "疑似使用错误的政治术语：" + errorWord;
                break;
            case SENSITIVE_WORDS:
                result = "疑似使用错误的敏感词：" + errorWord;
                break;
            default:
                result = "疑似其他内容错误：" + errorWord;
                break;
        }

        if (!StringUtil.isEmpty(correctWord)) {
            result += "，应为：" + correctWord;
        }
        return result;
    }

    public static void createFile(String fullFilePath, String content) throws IOException {
        File file = new File(fullFilePath);
        FileUtils.writeStringToFile(file, content, "UTF-8");
    }

    public static String getRelativeDir(Integer siteId, Integer checkId, String url, int index, int type) {
        // siteId / checkId / md5(url) / type / index / index.html
        return siteId + File.separator
                + checkId + File.separator
                + DigestUtils.md5Hex(url) + File.separator
                + type + File.separator
                + index;
    }

    public static void createDir(String dir) throws IOException {
        FileUtils.forceMkdir(new File(dir));
    }

    public static void createIndexHtml(String dir) throws IOException {
        String htmlText =  "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n" +
                "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n" +
                "\t<head>\n" +
                "\t\t<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />\n" +
                "\t\t<title>TRS网站健康检查系统-定位</title>\n" +
                "\t</head>\n" +
                "\t<frameset rows=\"40,*\" frameborder=\"no\">\n" +
                "\t\t<frame src=\"cont.html\">\n" +
                "\t\t<frame src=\"pos.html\" name=\"showframe\">\n" +
                "\t</frameset>\n" +
                "\t<noframes><body>\n" +
                "\t</body></noframes>\n" +
                "</html>";
        createFile(dir+File.separator +"index.html", htmlText);
    }

    public static void createPagePosHtml(String dir, String htmlText) throws IOException {
        createFile(dir+File.separator +"pos.html", htmlText);
    }

    public static void createSrcPosHtml(String dir, String htmlText) throws IOException {
        createFile(dir+File.separator +"src.html", htmlText);
    }

    public static void createContHtml(String dir, String orignalUrl, String parentUrl) throws IOException {
        String htmlText = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n" +
                "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n" +
                "\t<head>\n" +
                "\t\t<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />\n" +
                "\t\t<title>TRS网站健康检查系统-定位</title>\n" +
                "\t\t<link href=\"../../../../../style/css.css\" rel=\"stylesheet\" type=\"text/css\">\n" +
                "\t</head>\n" +
                "\t<body>\n" +
                "\t\t<div class=\"jkd_positiontop\">\n" +
                "\t\t\t<div class=\"top_positiontop\">\n" +
                "\t\t\t\t<h1><a href =\"pos.html\" target =\"showframe\">页面定位</a></h1>\n" +
                "\t\t\t\t<h1><a href =\"src.html\" target =\"showframe\">源码定位</a></h1>\n" +
                "\t\t\t\t<h1><a href =\"" + orignalUrl + "\" target =\"showframe\">原始页面</a></h1>\n" +
                "\t\t\t\t<h1><a href =\"" + parentUrl + "\" target =\"showframe\">父页面</a></h1>\n" +
                "\t\t\t\t<h1><a href=\"javascript:window.top.close();\">关闭</a></h1>\n" +
                "\t\t\t</div>\n" +
                "\t\t</div>\n" +
                "\t</body>\n" +
                "</html>";
        createFile(dir + File.separator +"cont.html", htmlText);
    }

    private String generateSourceLocHtmlText(Types.InfoErrorIssueType type, String errorWord, String correct) {

        // 将html标签转义
        String sourceEscape = StringEscapeUtils.escapeHtml4(content.getContent());
        StringBuffer sb = new StringBuffer();
        sb.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">");
        sb.append(LINE_SP);
        sb.append("<html xmlns=\"http://www.w3.org/1999/xhtml\">");
        sb.append(LINE_SP);
        sb.append("	<head>");
        sb.append(LINE_SP);
        sb.append("		<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"/>");
        sb.append(LINE_SP);
        sb.append("		<title>源码定位</title>");
        sb.append(LINE_SP);
        sb.append("		<link href=\"http://gov.trs.cn/jsp/cis4/css/SyntaxHighlighter.css\" rel=\"stylesheet\" type=\"text/css\">");
        sb.append(LINE_SP);
        sb.append("	</head>");
        sb.append(LINE_SP);
        sb.append("	<body> ");
        sb.append(LINE_SP);
        sb.append("		<div class=\"sh_code\">");
        sb.append(LINE_SP);
        sb.append("			<ol start=\"1\">");
        sb.append(LINE_SP);
        sourceEscape = sourceEscape.replaceAll("\r", "");
        sourceEscape = sourceEscape.replaceAll("\n", LINE_SP);
        sourceEscape = sourceEscape.replaceAll(" ", "&nbsp;");
        sourceEscape = sourceEscape.replaceAll("\t", "&nbsp;&nbsp;&nbsp;&nbsp;");
        String[] sourceArr = sourceEscape.split(LINE_SP);
        for (int i = 0; i < sourceArr.length; i++) {
            String line = sourceArr[i];
            if (i % 2 == 0) {
                sb.append("				<li>" + line + "</li>");
            } else {
                sb.append("				<li class=\"alt\">" + line + "</li>");
            }
            sb.append(LINE_SP);
        }
        sb.append("			</ol>");
        sb.append(LINE_SP);
        sb.append("		</div>");
        sb.append(LINE_SP);
        sb.append("	</body>");
        sb.append(LINE_SP);
        sb.append("</html>");
        sb.append(LINE_SP);

        // 在源码中增加定位用的脚本定义
        sb.append(LINE_SP);
        sb.append("<link href=\"http://gov.trs.cn/jsp/cis4/css/jquery.qtip.min.css\" rel=\"stylesheet\" type=\"text/css\">");
        sb.append(LINE_SP);
        sb.append("<script type=\"text/javascript\" src=\"http://gov.trs.cn/jsp/cis4/js/jquery.js\"></script>");
        sb.append(LINE_SP);
        sb.append("<script type=\"text/javascript\" src=\"http://gov.trs.cn/jsp/cis4/js/jquery.qtip.min.js\"></script>");
        sb.append(LINE_SP);
        sb.append("<script type=\"text/javascript\" src=\"http://gov.trs.cn/jsp/cis4/js/trsposition.js\"></script>");

        String result = sb.toString();
        int index = result.indexOf(errorWord, 0);
        if (index == -1) {
            return null;
        } else {
            String errorinfo = "<font trserrid=\"anchor\" msg=\"" + getDisplayErrorWord(type, errorWord, correct) + "\" msgtitle=\"定位\" style=\"border:2px red solid;color:red;\">" + errorWord + "</font>";
            result = result.substring(0, index) + errorinfo + result.substring(index + errorWord.length());
            index = result.indexOf(errorWord, index + errorinfo.length());
        }

        while (index != -1) {
            String errorinfo = "<font msg=\"" + getDisplayErrorWord(type, errorWord, correct) + "\" style=\"border:2px red solid;color:red;\">" + errorWord + "</font>";
            result = result.substring(0, index) + errorinfo + result.substring(index + errorWord.length());
            index = result.indexOf(errorWord, index + errorinfo.length());
        }

        return result;
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
            if(infoErrors.isEmpty()){
                issueMapper.insert(DBUtil.toRow(issue));
            }
        }
        log.info("buildCheckContent insert error count: " + issueList.size());
    }

    public static String getChnlName(String url) {
        int index = url.indexOf("//");
        if (index == -1) {
            return "";
        }
        index = url.indexOf("/", index + "//".length());
        if (index == -1) {
            return "";
        }

        int endIndex = url.indexOf("/", index + "/".length());
        if (endIndex == -1) {
            return "";
        } else {
            return url.substring(index+"/".length(), endIndex);
        }
    }
}
