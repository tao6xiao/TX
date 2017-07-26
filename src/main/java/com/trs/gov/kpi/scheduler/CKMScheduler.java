package com.trs.gov.kpi.scheduler;

import com.trs.gov.kpi.constant.IssueTableField;
import com.trs.gov.kpi.constant.Status;
import com.trs.gov.kpi.constant.Types;
import com.trs.gov.kpi.dao.IssueMapper;
import com.trs.gov.kpi.entity.InfoError;
import com.trs.gov.kpi.entity.Issue;
import com.trs.gov.kpi.entity.dao.QueryFilter;
import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.gov.kpi.entity.outerapi.Channel;
import com.trs.gov.kpi.entity.outerapi.ContentCheckResult;
import com.trs.gov.kpi.entity.requestdata.PageDataRequestParam;
import com.trs.gov.kpi.service.helper.QueryFilterHelper;
import com.trs.gov.kpi.service.outer.ContentCheckApiService;
import com.trs.gov.kpi.service.outer.SiteApiService;
import com.trs.gov.kpi.utils.CollectionUtil;
import com.trs.gov.kpi.utils.DBUtil;
import com.trs.gov.kpi.utils.PageCKMSpiderUtil;
import com.trs.gov.kpi.utils.StringUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Created by he.lang on 2017/5/24.
 */
@Slf4j
@Component
@Scope("prototype")
public class CKMScheduler implements SchedulerTask {

    public static final String LINE_SP = System.getProperty("line.separator");

    @Setter
    @Getter
    private String baseUrl;

    @Value("${issue.location.dir}")
    private String locationDir;

    @Setter
    @Getter
    private Integer siteId;

    @Setter
    @Getter
    private Boolean isTimeNode;

    @Resource
    private ContentCheckApiService contentCheckApiService;

    @Resource
    private IssueMapper issueMapper;

    @Resource
    PageCKMSpiderUtil spider;

    @Resource
    SiteApiService siteApiService;

    @Override
    public void run() throws RemoteException {
        log.info("CKMScheduler " + siteId + " start...");
        List<String> checkTypeList = Types.InfoErrorIssueType.getAllCheckTypes();
        Set<PageCKMSpiderUtil.CKMPage> ckmPages = spider.fetchPages(5, baseUrl);//"http://www.55zxx.net/#jzl_kwd=20988652540&jzl_ctv=7035658676&jzl_mtt=2&jzl_adt=clg1"
        for (PageCKMSpiderUtil.CKMPage page : ckmPages) {
            insert(buildList(page, checkTypeList));
        }
        log.info("CKMScheduler " + siteId + " end...");
    }

    private List<Issue> buildList(PageCKMSpiderUtil.CKMPage page, List<String> checkTypeList) throws RemoteException {
        List<Issue> issueList = new ArrayList<>();

        String checkContent = page.getContent();
        if (checkContent.length() <= 0) {
            return issueList;
        }

        ContentCheckResult result = null;
        try {
            result = contentCheckApiService.check(checkContent, CollectionUtil.join(checkTypeList, ";"));
        } catch (Exception e) {
            log.error("failed to check content " + checkContent, e);
            return issueList;
        }

        if (!result.isOk()) {
            log.error("return error: " + result.getMessage());
            return issueList;
        }

        if (result.getResult() != null) {
            issueList = toIssueList(page, checkTypeList, result);
        }
        return issueList;
    }

    private List<Issue> toIssueList(PageCKMSpiderUtil.CKMPage page, List<String> checkTypeList, ContentCheckResult result) throws RemoteException {
        List<Issue> issueList = new ArrayList<>();
        for (String checkType : checkTypeList) {
            Types.InfoErrorIssueType subIssueType = Types.InfoErrorIssueType.valueOfCheckType(checkType);
            String errorContent = result.getResultOfType(subIssueType);
            if (StringUtil.isEmpty(errorContent)) {
                continue;
            }
            final String relativeDir = getRelativeDir(siteId, Types.IssueType.INFO_ERROR_ISSUE.value, subIssueType.value, page.getUrl());
            String absoluteDir = locationDir + File.separator + relativeDir;
            try {
                createDir(absoluteDir);

                // 网页定位
                String pageLocContent = generatePageLocHtmlText(page, subIssueType, errorContent);
                if (pageLocContent == null) {
                    continue;
                }
                createPagePosHtml(absoluteDir, pageLocContent);

                // 源码定位
                String srcLocContent = generateSourceLocHtmlText(page, subIssueType, errorContent);
                if (srcLocContent == null) {
                    continue;
                }
                createSrcPosHtml(absoluteDir, srcLocContent);

                // 创建头部导航页面
                createContHtml(absoluteDir, page.getUrl(), baseUrl);

                // 创建首页
                createIndexHtml(absoluteDir);

            } catch (IOException e) {
                log.error("error content: " + errorContent);
                log.error("failed to generate file of " + page.getUrl() + ", siteid[" + siteId + "] ", e);
            }

            Issue issue = new Issue();
            issue.setSiteId(siteId);
            Channel channel = siteApiService.findChannelByUrl("", page.getUrl(), siteId);
            if (channel != null) {
                issue.setCustomer2(String.valueOf(channel.getChannelId()));
            }
            issue.setTypeId(Types.IssueType.INFO_ERROR_ISSUE.value);
            issue.setSubTypeId(subIssueType.value);
            issue.setDetail(absoluteDir + File.separator + "cont.html");
            Date nowTime = new Date();
            issue.setIssueTime(nowTime);
            issue.setCheckTime(nowTime);
            issue.setCustomer1(errorContent);
            issueList.add(issue);
        }
        return issueList;
    }

    public static void createIndexHtml(String dir) throws IOException {
        String htmlText = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n" +
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
        createFile(dir + File.separator + "index.html", htmlText);
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
        createFile(dir + File.separator + "cont.html", htmlText);
    }


    public static void createFile(String fullFilePath, String content) throws IOException {
        File file = new File(fullFilePath);
        FileUtils.writeStringToFile(file, content, "UTF-8");
    }

    public static void createPagePosHtml(String dir, String htmlText) throws IOException {
        createFile(dir + File.separator + "pos.html", htmlText);
    }

    public static void createSrcPosHtml(String dir, String htmlText) throws IOException {
        createFile(dir + File.separator + "src.html", htmlText);
    }

    private String generatePageLocHtmlText(PageCKMSpiderUtil.CKMPage page, Types.InfoErrorIssueType subIssueType, String errorInfo) {

        StringBuffer sb = new StringBuffer();
        // 给网站增加base标签
        sb.append("<base href=\"" + page.getUrl() + "\" />");
        sb.append(LINE_SP);
        sb.append(page.getContent().intern());
        // 在源码中增加定位用的脚本定义
        sb.append(LINE_SP);
        sb.append("<link href=\"http://gov.trs.cn/jsp/cis4/css/jquery.qtip.min.css\" rel=\"stylesheet\" type=\"text/css\">");
        sb.append(LINE_SP);
        sb.append("<script type=\"text/javascript\" src=\"http://gov.trs.cn/jsp/cis4/js/jquery.js\"></script>");
        sb.append(LINE_SP);
        sb.append("<script type=\"text/javascript\" src=\"http://gov.trs.cn/jsp/cis4/js/jquery.qtip.min.js\"></script>");
        sb.append(LINE_SP);
        sb.append("<script type=\"text/javascript\" src=\"http://gov.trs.cn/jsp/cis4/js/trsposition.js\"></script>");
        // 解析源码，找到错误位置，加上标记信息。
        String result = sb.toString();

        return addTips(errorInfo, subIssueType, result);
    }

    private String addTips(String errorInfo, Types.InfoErrorIssueType subIssueType, String result) {
        String errorWord;
        String correctWord = "";
        String tipsResult = "";
        String[] errorInfos = errorInfo.replaceAll("[\\{\\}\"]", "").split(",");
        for (String error : errorInfos) {
            String[] info = error.split("：");
            errorWord = info[0];
            if (info.length > 1) {
                String[] correntInfo = info[1].split(":");
                correctWord = correntInfo[0];
            }
            int left = 0;
            int index;
            int right;
            while (true) {
                right = result.indexOf('>', left);
                left = result.indexOf('<', right);
                if (left == -1) {
                    return null;
                }
                String subString = result.substring(right, left);
                if (subString.contains(errorWord)) {
                    index = right + subString.indexOf(errorWord);
                    break;
                }
            }

            String errorTip = "<font trserrid=\"anchor\" msg=\"" + getDisplayErrorWord(subIssueType, errorWord, correctWord) + "\" msgtitle=\"定位\" style=\"border:2px red solid;" +
                    "color:red;" +
                    "\">" + errorWord + "</font>";
            tipsResult = result.substring(0, index) + errorTip + result.substring(index + errorWord.length());

            left = index + errorTip.length();
            while (true) {
                right = result.indexOf('>', left);
                left = result.indexOf('<', right);
                if (left == -1) {
                    break;
                }
                String subString = result.substring(right, left);
                if (subString.contains(errorWord)) {
                    index = right + subString.indexOf(errorWord);
                    errorTip = "<font msg=\"" + getDisplayErrorWord(subIssueType, errorWord, correctWord) + "\" style=\"border:2px red solid;color:red;\">" + errorWord + "</font>";
                    tipsResult = tipsResult.substring(0, index) + errorTip + result.substring(index + errorWord.length());
                }
            }

        }
        return tipsResult;
    }


    private String getDisplayErrorWord(Types.InfoErrorIssueType subIssueType, String errorWord, String correctWord) {
        String result = "";
        switch (subIssueType) {
            case TYPOS:
                result = "疑似错别字：" + errorWord;
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

    public static void createDir(String dir) throws IOException {
        FileUtils.forceMkdir(new File(dir));
    }

    //相对路径：站点编号/问题类型编号/问题子类型编号/url加密
    public static String getRelativeDir(Integer siteId, Integer issueTypeId, Integer subIssueTypeId, String url) {
        return siteId + File.separator + issueTypeId + File.separator + subIssueTypeId + File.separator + DigestUtils.md5Hex(url);
    }

    private String generateSourceLocHtmlText(PageCKMSpiderUtil.CKMPage page, Types.InfoErrorIssueType subIssueType, String errorInfo) {
        // 将html标签转义
        String sourceEscape = StringEscapeUtils.escapeHtml4(page.getContent());
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

        return addTips(errorInfo, subIssueType, result);
    }


    private void insert(List<Issue> issueList) throws RemoteException {
        //插入监测出的信息错误数据
        for (Issue issue : issueList) {

            PageDataRequestParam param = new PageDataRequestParam();
            param.setSiteId(siteId);
            QueryFilter queryFilter = QueryFilterHelper.toFilter(param);
            queryFilter.addCond(IssueTableField.TYPE_ID, Types.IssueType.INFO_ERROR_ISSUE.value);
            queryFilter.addCond(IssueTableField.IS_RESOLVED, Status.Resolve.UN_RESOLVED.value);
            queryFilter.addCond(IssueTableField.IS_DEL, Status.Delete.UN_DELETE.value);
            queryFilter.addCond(IssueTableField.DETAIL, issue.getDetail());
            queryFilter.addCond(IssueTableField.CUSTOMER1, issue.getCustomer1());
            queryFilter.addCond(IssueTableField.SUBTYPE_ID, issue.getSubTypeId());
            if (issue.getCustomer2() != null) {
                queryFilter.addCond(IssueTableField.CUSTOMER2, issue.getCustomer2());
            }

            List<InfoError> infoErrors = issueMapper.selectInfoError(queryFilter);
            if (infoErrors.isEmpty()) {
                issueMapper.insert(DBUtil.toRow(issue));
            }
        }
        log.info("buildCheckContent insert error count: " + issueList.size());
    }
}
