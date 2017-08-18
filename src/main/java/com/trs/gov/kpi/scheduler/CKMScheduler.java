package com.trs.gov.kpi.scheduler;

import com.trs.gov.kpi.constant.*;
import com.trs.gov.kpi.dao.CommonMapper;
import com.trs.gov.kpi.dao.IssueMapper;
import com.trs.gov.kpi.dao.LinkContentStatsMapper;
import com.trs.gov.kpi.entity.InfoError;
import com.trs.gov.kpi.entity.Issue;
import com.trs.gov.kpi.entity.dao.DBUpdater;
import com.trs.gov.kpi.entity.dao.QueryFilter;
import com.trs.gov.kpi.entity.dao.Table;
import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.gov.kpi.entity.outerapi.ContentCheckResult;
import com.trs.gov.kpi.entity.outerapi.Site;
import com.trs.gov.kpi.entity.requestdata.PageDataRequestParam;
import com.trs.gov.kpi.service.LinkContentStatsService;
import com.trs.gov.kpi.service.MonitorRecordService;
import com.trs.gov.kpi.service.helper.QueryFilterHelper;
import com.trs.gov.kpi.service.outer.ChnlDocumentServiceHelper;
import com.trs.gov.kpi.service.outer.ContentCheckApiService;
import com.trs.gov.kpi.service.outer.SiteApiService;
import com.trs.gov.kpi.service.outer.SiteChannelServiceHelper;
import com.trs.gov.kpi.utils.*;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.*;

/**
 * Created by he.lang on 2017/5/24.
 */
@Slf4j
@Component
@Scope("prototype")
public class CKMScheduler implements SchedulerTask {

    private static final String LINE_SP = System.getProperty("line.separator");

    private static final String INDEX = "index";

    private static final String HTML_SUF = "</html>";

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
    private SiteChannelServiceHelper siteChannelServiceHelper;

    @Resource
    private IssueMapper issueMapper;

    @Resource
    PageCKMSpiderUtil spider;

    @Resource
    SiteApiService siteApiService;

    @Resource
    private LinkContentStatsService linkContentStatsService;

    @Resource
    private LinkContentStatsMapper linkContentStatsMapper;

    @Resource
    private MonitorRecordService monitorRecordService;

    @Resource
    private CommonMapper commonMapper;

    //错误信息计数
    @Getter
    Integer monitorResult = 0;

    //站点监测状态（0：自动监测；1：手动监测）
    @Setter
    @Getter
    private Integer monitorType;

    int isCheck = 0;
    //记录一个链接较上次，内容更新的数量
    double changeCount = 0;
    //记录所有链接较上次，内容更新的数量
    double allChangeCount = 0;

    private Date ckmCheckTime;

    //记录检测次数
    int ckmValidateTimes = 1;


    @Getter
    private EnumCheckJobType checkJobType = EnumCheckJobType.CHECK_CONTENT;

    @Override
    public void run() throws RemoteException {
        final Site checkSite = siteApiService.getSiteById(siteId, null);
        baseUrl = OuterApiServiceUtil.checkSiteAndGetUrl(siteId, checkSite);
        if (StringUtil.isEmpty(baseUrl)) {
            return;
        }
        spider.fetchPages(5, baseUrl, this, siteId);//测试url："http://www.55zxx.net/#jzl_kwd=20988652540&jzl_ctv=7035658676&jzl_mtt=2&jzl_adt=clg1"

        if (isCheck == 0) {
            monitorResult = getLastTimeMonitorResult();
        } else {
            int lastTimeMonitorResultCount = getLastTimeMonitorResult();
            monitorResult = (int) (lastTimeMonitorResultCount + allChangeCount);
        }
    }

    /**
     * 获取上一次完成检测的结果
     *
     * @return
     */
    private int getLastTimeMonitorResult() {
        //获取上一次检查完成的时间
        Date endTime = monitorRecordService.getLastMonitorEndTime(siteId, Types.IssueType.INFO_ERROR_ISSUE.value);
        if (endTime != null) {
            //根据上一次完成时间获取上一次检查结果
            return monitorRecordService.getResultByLastEndTime(siteId, Types.IssueType.INFO_ERROR_ISSUE.value, endTime);
        }
        return 0;
    }

    @Override
    public String getName() {
        return SchedulerType.CKM_SCHEDULER.toString();
    }

    private List<Issue> buildList(PageCKMSpiderUtil.CKMPage page, List<String> checkTypeList) throws RemoteException {
        List<Issue> issueList = new ArrayList<>();

        String checkContent = page.getContent();
        if (checkContent.length() <= 0) {
            return issueList;
        }
        ContentCheckResult result = null;
        //上一次检测时间
        Date lastCheckTime = linkContentStatsMapper.getLastCheckTimeByUrl(siteId, page.getUrl());
        try {

            String thisTimeMd5 = linkContentStatsService.getThisTimeMD5(siteId, Types.IssueType.INFO_ERROR_ISSUE.value, page.getUrl());
            String lastTimeMd5 = linkContentStatsService.getLastTimeMD5(siteId, Types.IssueType.INFO_ERROR_ISSUE.value, page.getUrl());
            if(ckmValidateTimes == 2){
                ckmValidateTimes = 1;
            }
            if (lastTimeMd5 == null || !thisTimeMd5.equals(lastTimeMd5)) {//第一次检查或链接内容发生变化
                //检测爬取内容
                result = contentCheckApiService.check(checkContent, CollectionUtil.join(checkTypeList, ";"));
                isCheck = 1;
            } else {//内容较上一次没有变化
                //更新Issue表中checkTime
                toUpdateCheckTime(page, lastCheckTime);
                return issueList;
            }
        } catch (Exception e) {
            String errorInfo = "siteId[" + siteId + "], url[" + baseUrl + "], failed to check content " + checkContent;
            log.error(errorInfo, e);
            LogUtil.addErrorLog(OperationType.REQUEST, ErrorType.REQUEST_FAILED, errorInfo, e);
            return issueList;
        }

        if (!result.isOk()) {
            log.error("siteId[" + siteId + "], url[" + baseUrl + "]return error: " + result.getMessage());
            return issueList;
        }

        if (result.getResult() != null) {
            issueList = toIssueList(page, checkTypeList, result);

            int lastTimIssueCount = getLastTimIssueCount(page, lastCheckTime);
            changeCount = (double) (issueList.size() - lastTimIssueCount);
        } else {
            if(isCheck == 1){
                int lastTimIssueCount = getLastTimIssueCount(page, lastCheckTime);
                changeCount = (double)(0 - lastTimIssueCount);
            }
        }
        //更新链接内容统计中的信息错误个数
        toUpdateLinkContentInfoErrorCount(page, issueList);

        return issueList;
    }

    /**
     * 获取上一次链接内容检测错误数量
     * @param page
     * @param lastCheckTime
     * @return
     */
    private int getLastTimIssueCount(PageCKMSpiderUtil.CKMPage page, Date lastCheckTime) {
        QueryFilter filter = new QueryFilter(Table.ISSUE);
        filter.addCond(IssueTableField.SITE_ID, siteId);
        filter.addCond(IssueTableField.CUSTOMER3, page.getUrl());
        filter.addCond(IssueTableField.CHECK_TIME,lastCheckTime);

        return commonMapper.count(filter);
    }

    private List<Issue> toIssueList(PageCKMSpiderUtil.CKMPage page, List<String> checkTypeList, ContentCheckResult result) {
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

                createPagePosHtml(absoluteDir, pageLocContent);

                // 源码定位
                String srcLocContent = generateSourceLocHtmlText(page, subIssueType, errorContent);

                createSrcPosHtml(absoluteDir, srcLocContent);

                // 创建头部导航页面
                createContHtml(absoluteDir, page.getUrl(), spider.getParentUrl(page.getUrl()));

                // 创建首页
                createIndexHtml(absoluteDir);

            } catch (IOException e) {
                String errorInfo = "failed to generate file of " + page.getUrl() + ", siteid[" + siteId + "] ";
                log.error(errorInfo, e);
                LogUtil.addErrorLog(OperationType.REQUEST, ErrorType.REQUEST_FAILED, errorInfo, e);
            }

            Issue issue = new Issue();
            issue.setSiteId(siteId);
            Integer chnlId = getChnlId(page);
            if (chnlId != null) {
                issue.setCustomer2(String.valueOf(chnlId));
                setDeptId(issue, chnlId);
            }
            issue.setTypeId(Types.IssueType.INFO_ERROR_ISSUE.value);
            issue.setSubTypeId(subIssueType.value);
            issue.setDetail("gov/kpi/loc/" + relativeDir.replace(File.separator, "/") + "/index.html");
            Date nowTime = new Date();
            issue.setIssueTime(nowTime);
            issue.setCheckTime(ckmCheckTime);
            issue.setCustomer1(errorContent);
            issue.setCustomer3(page.getUrl());//原始页面
            issueList.add(issue);
        }
        return issueList;
    }

    private void setDeptId(Issue issue, Integer chnlId) {
        try {
            issue.setDeptId(siteChannelServiceHelper.findRelatedDept(chnlId, ""));
        } catch (RemoteException e) {
            String errorInfo = MessageFormat.format("获取栏目所属部门失败! [siteId={0}, chnlId={1}]", siteId, chnlId);
            log.error(errorInfo, e);
            LogUtil.addErrorLog(OperationType.REQUEST, ErrorType.REQUEST_FAILED, errorInfo, e);
        }
    }

    @Nullable
    private Integer getChnlId(PageCKMSpiderUtil.CKMPage page) {
        Integer chnlId = null;
        try {
            chnlId = ChnlDocumentServiceHelper.getChnlIdByUrl("", page.getUrl(), siteId);
        } catch (RemoteException e) {
            String errorInfo = MessageFormat.format("获取栏目Id失败! [siteId={0}, url={1}]", siteId, page.getUrl());
            log.error(errorInfo, e);
            LogUtil.addErrorLog(OperationType.REQUEST, ErrorType.REQUEST_FAILED, errorInfo, e);
        }
        return chnlId;
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
                HTML_SUF;
        createFile(dir + File.separator + "index.html", htmlText);
    }

    public static void createContHtml(String dir, String orignalUrl, String parentUrl) throws IOException {
        String htmlText = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n" +
                "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n" +
                "\t<head>\n" +
                "\t\t<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />\n" +
                "\t\t<title>TRS网站健康检查系统-定位</title>\n" +
                "\t\t<link href=\"../../../../style/css.css\" rel=\"stylesheet\" type=\"text/css\">\n" +
                "\t</head>\n" +
                "\t<body>\n" +
                "\t\t<div class=\"jkd_positiontop\">\n" +
                "\t\t\t<div class=\"top_positiontop\">\n" +
                "\t\t\t\t<h1><a href =\"pos.html\" target =\"showframe\">页面定位</a></h1>\n" +
                "\t\t\t\t<h1><a href =\"src.html\" target =\"showframe\">源码定位</a></h1>\n" +
                "\t\t\t\t<h1><a href =\"" + orignalUrl + "\" target =\"showframe\">原始页面</a></h1>\n" +
                "\t\t\t\t<h1><a href =\"" + parentUrl + "\" target =\"showframe\">父页面</a></h1>\n" +
                "\t\t\t</div>\n" +
                "\t\t</div>\n" +
                "\t</body>\n" +
                HTML_SUF;
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

        String result = generateBasePageLoc(page.getUrl(), page.getContent());

        // 解析源码，找到错误位置，加上标记信息。
        return addTips(errorInfo, subIssueType, result);
    }

    public static String generateBasePageLoc(String url, String content) {
        if (StringUtil.isEmpty(content)) {
            return "<html><body><h1>快照内容已不存在！</h1></body></html>";
        }
        StringBuilder sb = new StringBuilder();
        // 给网站增加base标签
        sb.append("<base href=\"" + url + "\" />")
                .append(LINE_SP);
        sb.append(content.intern())
                .append(LINE_SP);
        sb.append(addScriptDef());
        return sb.toString();
    }

    private String addTips(String errorInfo, Types.InfoErrorIssueType subIssueType, String result) {
        String errorWord;
        String correctWord;
        String tipsResult = result;
        String[] errorInfos = errorInfo.replaceAll("[\\{\\}\"]", "").split(",");

        for (String error : errorInfos) {
            String[] words = getWords(error, subIssueType);
            errorWord = words[0];
            correctWord = words[1];
            Map<String, Object> resultMap = addFirstTip(subIssueType, errorWord, correctWord, tipsResult);
            if ((int) resultMap.get(INDEX) > -1) {
                tipsResult = addOtherTips(resultMap, subIssueType, errorWord, correctWord);
            }
        }
        return tipsResult;
    }

    //添加后续相同错误字词的提示
    private String addOtherTips(Map<String, Object> resultMap, Types.InfoErrorIssueType subIssueType, String errorWord, String correctWord) {
        int left = (int) resultMap.get(INDEX);
        String tipsResult = (String) resultMap.get("content");
        while (true) {
            int right = tipsResult.indexOf('>', left);
            left = tipsResult.indexOf('<', right);
            if (left == -1) {
                break;
            }
            String subString = tipsResult.substring(right, left);
            if (subString.contains(errorWord)) {
                int index = right + subString.indexOf(errorWord);
                String errorTip = "<font msg=\"" + getDisplayErrorWord(subIssueType, errorWord, correctWord) + "\" style=\"border:2px red solid;color:red;\">" + errorWord + "</font>";
                tipsResult = tipsResult.substring(0, index) + errorTip + tipsResult.substring(index + errorWord.length());
                left = index + errorTip.length();
            }
        }
        return tipsResult;
    }

    //添加第一个提示
    private Map<String, Object> addFirstTip(Types.InfoErrorIssueType subIssueType, String errorWord, String correctWord, String tipsResult) {
        int left = 0;
        int index = 0;
        String result = "";
        while (index == 0) {
            int right = tipsResult.indexOf('>', left);
            left = tipsResult.indexOf('<', right);
            if (left == -1) {
                break;
            }
            String subString = tipsResult.substring(right, left);
            if (subString.contains(errorWord)) {
                index = right + subString.indexOf(errorWord);
                String errorTip = "<font trserrid=\"anchor\" msg=\"" + getDisplayErrorWord(subIssueType, errorWord, correctWord) +
                        "\" msgtitle=\"定位\" style=\"border:2px red solid; color:red; \">" + errorWord + "</font>";
                result = tipsResult.substring(0, index) + errorTip + tipsResult.substring(index + errorWord.length());
                left = index + errorTip.length();
            }
        }
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put(INDEX, left);
        resultMap.put("content", result);
        return resultMap;
    }

    //获得错误字词和建议
    private String[] getWords(String error, Types.InfoErrorIssueType subIssueType) {
        String[] words = new String[2];
        if (Types.InfoErrorIssueType.TYPOS.equals(subIssueType)) {
            String[] info = error.split("：");
            words[0] = info[0];
            if (info.length > 1) {
                String[] correctInfo = info[1].split(":");
                words[1] = correctInfo[0];
            }
        } else {
            String[] info = error.split(";");
            words[0] = info[0];
            if (info.length > 1) {
                String[] correctInfo = info[1].split(":");
                words[1] = correctInfo[0];
            }
        }
        return words;
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
        if (StringUtil.isEmpty(page.getContent())) {
            return "<html><body><h1>快照内容已不存在！</h1></body></html>";
        }
        String result = generateBasSourceLoc(page.getContent());

        return addTips(errorInfo, subIssueType, result);
    }

    public static String generateBasSourceLoc(String content) {
        // 将html标签转义
        String sourceEscape = StringEscapeUtils.escapeHtml4(content);
        StringBuilder sb = new StringBuilder();
        sb.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">")
                .append(LINE_SP);
        sb.append("<html xmlns=\"http://www.w3.org/1999/xhtml\">")
                .append(LINE_SP);
        sb.append("	<head>")
                .append(LINE_SP);
        sb.append("		<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"/>")
                .append(LINE_SP);
        sb.append("		<title>源码定位</title>")
                .append(LINE_SP);
        sb.append("		<link href=\"http://gov.trs.cn/jsp/cis4/css/SyntaxHighlighter.css\" rel=\"stylesheet\" type=\"text/css\">")
                .append(LINE_SP);
        sb.append("	</head>")
                .append(LINE_SP);
        sb.append("	<body> ")
                .append(LINE_SP);
        sb.append("		<div class=\"sh_code\">")
                .append(LINE_SP);
        sb.append("			<ol start=\"1\">")
                .append(LINE_SP);
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
        sb.append("			</ol>")
                .append(LINE_SP);
        sb.append("		</div>")
                .append(LINE_SP);
        sb.append("	</body>")
                .append(LINE_SP);
        sb.append(HTML_SUF)
                .append(LINE_SP);
        sb.append(LINE_SP);
        sb.append(addScriptDef());
        return sb.toString();
    }

    /**
     * 在源码中增加定位用的脚本定义
     *
     * @return
     */
    public static StringBuilder addScriptDef() {
        StringBuilder sb = new StringBuilder();
        sb.append("<link href=\"http://gov.trs.cn/jsp/cis4/css/jquery.qtip.min.css\" rel=\"stylesheet\" type=\"text/css\">")
                .append(LINE_SP);
        sb.append("<script type=\"text/javascript\" src=\"http://gov.trs.cn/jsp/cis4/js/jquery.js\"></script>")
                .append(LINE_SP);
        sb.append("<script type=\"text/javascript\" src=\"http://gov.trs.cn/jsp/cis4/js/jquery.qtip.min.js\"></script>")
                .append(LINE_SP);
        sb.append("<script type=\"text/javascript\" src=\"http://gov.trs.cn/jsp/cis4/js/trsposition.js\"></script>");
        return sb;
    }


    /**
     * 插入监测出的信息错误数据
     * @param issueList
     */
    public void insert(List<Issue> issueList, PageCKMSpiderUtil.CKMPage page) {
        //记录错误插入条数
        int insertIssueInfoErrorCount = 0;
        for (Issue issue : issueList) {

            try {
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
                    insertIssueInfoErrorCount ++;
                } else {
                    DBUpdater update = new DBUpdater(Table.ISSUE.getTableName());
                    update.addField(IssueTableField.CHECK_TIME, ckmCheckTime);
                    commonMapper.update(update, queryFilter);
                }

            } catch (RemoteException e) {
                log.error("", e);
                LogUtil.addErrorLog(OperationType.REMOTE, ErrorType.REMOTE_FAILED, "插入信息错误数据失败，siteId[" + siteId + "]", e);
            }
        }
        /**
         * 如果检测出的错误数量和入库的错误数量不一致：
         * 1.会在执行一次检测
         * 2.第一次检测出的链接内容更新数不会被记录到 allChangeCount 中
         * 3.如果第二检测后还是出现检测出的错误数量和入库的错误数量不一致，就不在去做第三次检测，并将第二次检测出的链接内容更新数记录到 allChangeCount
         */
        if(insertIssueInfoErrorCount != issueList.size()){
            if(ckmValidateTimes < 2){
                Date secondCheckTime = new Date();
                //第二次检测时，更新LinkContentStats表中的时间
                toUpdateLinkContentStatsCheckTime(page, secondCheckTime);
                this.insert(page,secondCheckTime);
                ckmValidateTimes ++;
            }else {//第二次直接记录内容更新数
                allChangeCount += changeCount;
            }
        }else {//如果检测出的错误数量和入库的错误数量一致，就记录总的改变数量
            allChangeCount += changeCount;
        }
        log.info("buildCheckContent insert error count: " + issueList.size());
    }


    public void insert(PageCKMSpiderUtil.CKMPage page, Date checkTime) {
        ckmCheckTime = checkTime;
        List<String> checkTypeList = Types.InfoErrorIssueType.getAllCheckTypes();
        try {
            insert(buildList(page, checkTypeList), page);
        } catch (RemoteException e) {
            log.error("", e);
            LogUtil.addErrorLog(OperationType.REMOTE, ErrorType.REMOTE_FAILED, "检查信息错误信息失败，siteId[" + siteId + "]", e);
        }
    }

    /**
     * 链接内容统计——更新链接错误数量
     * @param page
     * @param issueList
     */
    private void toUpdateLinkContentInfoErrorCount(PageCKMSpiderUtil.CKMPage page, List<Issue> issueList) {
        QueryFilter filter = getQueryFilter(page);

        DBUpdater updater = new DBUpdater(Table.LINK_CONTENT_STATS.getTableName());
        updater.addField(LinkContentStatsTableFileld.INFO_ERROR_COUNT, issueList.size());
        commonMapper.update(updater,filter);
    }

    /**
     * 链接内容统计——更新时间
     * @param page
     * @param secondChcekTime
     */
    private void toUpdateLinkContentStatsCheckTime(PageCKMSpiderUtil.CKMPage page, Date secondChcekTime) {
        QueryFilter filter = getQueryFilter(page);

        DBUpdater updater = new DBUpdater(Table.LINK_CONTENT_STATS.getTableName());
        updater.addField(LinkContentStatsTableFileld.CHECK_TIME,secondChcekTime);
        commonMapper.update(updater,filter);
    }
    @NotNull
    private QueryFilter getQueryFilter(PageCKMSpiderUtil.CKMPage page) {
        QueryFilter filter = new QueryFilter(Table.LINK_CONTENT_STATS);
        filter.addCond(LinkContentStatsTableFileld.SITE_ID, siteId);
        filter.addCond(LinkContentStatsTableFileld.TYPE_ID, Types.MonitorRecordNameType.TASK_CHECK_CONTENT.value);
        filter.addCond(LinkContentStatsTableFileld.URL, page.getUrl());
        filter.addCond(LinkContentStatsTableFileld.CHECK_TIME, ckmCheckTime);
        return filter;
    }

    /**
     * 更新checkTime
     * @param page
     * @param lastCheckTime
     */
    private void toUpdateCheckTime(PageCKMSpiderUtil.CKMPage page, Date lastCheckTime) {
        QueryFilter filter = new QueryFilter(Table.ISSUE);
        filter.addCond(IssueTableField.CUSTOMER3, page.getUrl());
        filter.addCond(IssueTableField.CHECK_TIME, lastCheckTime);

        DBUpdater updater = new DBUpdater(Table.ISSUE.getTableName());
        updater.addField(IssueTableField.CHECK_TIME, ckmCheckTime);
        commonMapper.update(updater, filter);
    }


}
