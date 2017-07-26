package com.trs.gov.kpi.scheduler;

import com.trs.gov.kpi.constant.Types;
import com.trs.gov.kpi.constant.WebpageTableField;
import com.trs.gov.kpi.dao.WebPageMapper;
import com.trs.gov.kpi.entity.PageDepth;
import com.trs.gov.kpi.entity.PageSpace;
import com.trs.gov.kpi.entity.ReplySpeed;
import com.trs.gov.kpi.entity.UrlLength;
import com.trs.gov.kpi.entity.dao.QueryFilter;
import com.trs.gov.kpi.entity.dao.Table;
import com.trs.gov.kpi.entity.outerapi.Site;
import com.trs.gov.kpi.entity.responsedata.LinkAvailabilityResponse;
import com.trs.gov.kpi.service.LinkAvailabilityService;
import com.trs.gov.kpi.service.WebPageService;
import com.trs.gov.kpi.utils.SpiderUtils;
import com.trs.gov.kpi.utils.StringUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.utils.UrlUtils;

import javax.annotation.Resource;
import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Created by wangxuan on 2017/5/10.
 * 定时任务，抓取网站链接是否可用
 */
@Slf4j
@Component
@Scope("prototype")
public class LinkAnalysisScheduler implements SchedulerTask {

    @Value("${issue.location.dir}")
    private String locationDir;

    @Resource
    LinkAvailabilityService linkAvailabilityService;

    @Resource
    private SiteApiService siteApiService;

    @Resource
    SpiderUtils spider;

    @Resource
    WebPageService webPageService;

    @Resource
    WebPageMapper webPageMapper;

    @Setter
    @Getter
    private Integer siteId;

    @Setter
    @Getter
    private String baseUrl;

    @Setter
    @Getter
    private Boolean isTimeNode;

    @Override
    public void run() {

        log.info("LinkAnalysisScheduler " + siteId + " start...");
        try {

            final Site checkSite = siteApiService.getSiteById(siteId, null);
            if (checkSite == null) {
                log.error("site[" + siteId + "] is not exsit!");
                return;
            }

            baseUrl = checkSite.getWebHttp();
            if (StringUtil.isEmpty(baseUrl)) {
                log.warn("site[" + siteId + "]'s web http is empty!");
                return;
            }

            List<Pair<String, String>> unavailableUrlAndParentUrls = spider.linkCheck(3, siteId, "http://tunchang.hainan.gov.cn/tcgov/");
            Date checkTime = new Date();
            log.info("LinkAnalysisScheduler " + siteId + "  write location file and insert start...");
            for (Pair<String, String> unavailableUrlAndParentUrl : unavailableUrlAndParentUrls) {
                LinkAvailabilityResponse linkAvailabilityResponse = new LinkAvailabilityResponse();
                linkAvailabilityResponse.setInvalidLink(unavailableUrlAndParentUrl.getValue());
                linkAvailabilityResponse.setCheckTime(checkTime);
                linkAvailabilityResponse.setSiteId(siteId);
                linkAvailabilityResponse.setIssueTypeId(getTypeByLink(unavailableUrlAndParentUrl.getValue()).value);
                final String relativeDir = CKMScheduler.getRelativeDir(siteId, Types.IssueType.LINK_AVAILABLE_ISSUE.value, linkAvailabilityResponse.getIssueTypeId(),
                        linkAvailabilityResponse.getInvalidLink());
                String absoluteDir = locationDir + File.separator + relativeDir;
                linkAvailabilityResponse.setSnapshot(absoluteDir + File.separator + "cont.html");

                if (!linkAvailabilityService.existLinkAvailability(siteId, unavailableUrlAndParentUrl.getValue())) {

                    CKMScheduler.createDir(absoluteDir);

                    // 网页定位
                    String pageLocContent = generatePageLocHtmlText(unavailableUrlAndParentUrl, spider.getPageContentMap().get(unavailableUrlAndParentUrl.getKey()), spider
                            .getPageStatusCodeMap().get(unavailableUrlAndParentUrl.getValue()));
                    if (pageLocContent == null) {
                        continue;
                    }
                    CKMScheduler.createPagePosHtml(absoluteDir, pageLocContent);

                    // 源码定位
                    String srcLocContent = generateSourceLocHtmlText(unavailableUrlAndParentUrl, spider.getPageContentMap().get(unavailableUrlAndParentUrl.getKey()), spider
                            .getPageStatusCodeMap().get(unavailableUrlAndParentUrl.getValue()));
                    if (srcLocContent == null) {
                        continue;
                    }
                    CKMScheduler.createSrcPosHtml(absoluteDir, srcLocContent);

                    // 创建头部导航页面
                    CKMScheduler.createContHtml(absoluteDir, unavailableUrlAndParentUrl.getValue(), unavailableUrlAndParentUrl.getKey());

                    // 创建首页
                    CKMScheduler.createIndexHtml(absoluteDir);
                    linkAvailabilityService.insertLinkAvailability(linkAvailabilityResponse);
                }
            }
            log.info("LinkAnalysisScheduler " + siteId + "  write location file and insert end...");

            //获取响应速度基本信息，信息入库并去除重复数据和更新数据库信息
            Set<ReplySpeed> replySpeedSet = spider.getReplySpeeds();
            for (ReplySpeed replySpeedTo : replySpeedSet) {

                QueryFilter queryFilter = new QueryFilter(Table.WEB_PAGE);
                queryFilter.addCond(WebpageTableField.SITE_ID, siteId);
                queryFilter.addCond(WebpageTableField.PAGE_LINK, replySpeedTo.getPageLink());
                queryFilter.addCond(WebpageTableField.CHNL_ID, replySpeedTo.getChnlId());
                queryFilter.addCond(WebpageTableField.TYPE_ID, Types.AnalysisType.REPLY_SPEED.value);

                List<ReplySpeed> pageSpaceList = webPageMapper.selectReplySpeed(queryFilter);
                updateOrInsertSpeed(pageSpaceList, replySpeedTo);
            }

            //获取过大页面信息；信息入库并去除重复数据和更新数据库信息
            Set<PageSpace> biggerPageSpace = spider.biggerPageSpace();
            for (PageSpace pageSpaceTo : biggerPageSpace) {
                QueryFilter queryFilter = new QueryFilter(Table.WEB_PAGE);
                queryFilter.addCond(WebpageTableField.SITE_ID, siteId);
                queryFilter.addCond(WebpageTableField.PAGE_LINK, pageSpaceTo.getPageLink());
                queryFilter.addCond(WebpageTableField.CHNL_ID, pageSpaceTo.getChnlId());
                queryFilter.addCond(WebpageTableField.TYPE_ID, Types.AnalysisType.OVERSIZE_PAGE.value);

                List<PageSpace> pageSpaceList = webPageMapper.selectPageSpace(queryFilter);
                updateOrInsertSpace(pageSpaceList, pageSpaceTo);
            }

            //获取过长URL页面信息；信息入库并去除重复数据和更新数据库信息
            Set<UrlLength> biggerUerLenght = spider.getBiggerUrlPage();
            for (UrlLength urlLenghtTo : biggerUerLenght) {

                QueryFilter queryFilter = new QueryFilter(Table.WEB_PAGE);
                queryFilter.addCond(WebpageTableField.SITE_ID, siteId);
                queryFilter.addCond(WebpageTableField.PAGE_LINK, urlLenghtTo.getPageLink());
                queryFilter.addCond(WebpageTableField.CHNL_ID, urlLenghtTo.getChnlId());
                queryFilter.addCond(WebpageTableField.TYPE_ID, Types.AnalysisType.TOO_LONG_URL.value);

                List<UrlLength> urlLenghtList = webPageMapper.selectUrlLength(queryFilter);
                updateOrInsertLength(urlLenghtList, urlLenghtTo);
            }

            //获取过深页面信息；信息入库并去除重复数据和更新数据库信息
            Set<PageDepth> pageDepthSet = spider.getPageDepths();
            for (PageDepth pageDepthTo : pageDepthSet) {
                QueryFilter queryFilter = new QueryFilter(Table.WEB_PAGE);
                queryFilter.addCond(WebpageTableField.SITE_ID, siteId);
                queryFilter.addCond(WebpageTableField.PAGE_LINK, pageDepthTo.getPageLink());
                queryFilter.addCond(WebpageTableField.CHNL_ID, pageDepthTo.getChnlId());
                queryFilter.addCond(WebpageTableField.TYPE_ID, Types.AnalysisType.OVER_DEEP_PAGE.value);

                List<PageDepth> pageDepthList = webPageMapper.selectPageDepth(queryFilter);
                updateOrInsertDepth(pageDepthList, pageDepthTo);
            }

        } catch (Exception e) {
            log.error("check link:{}, siteId:{} availability error!", baseUrl, siteId, e);
        } finally {
            log.info("LinkAnalysisScheduler " + siteId + " end...");
        }
    }

    private String generateSourceLocHtmlText(Pair<String, String> unavailableUrlAndParentUrl, String parentUrlContent, Integer pageStatusCode) {
        // 将html标签转义
        String sourceEscape = StringEscapeUtils.escapeHtml4(parentUrlContent);
        StringBuffer sb = new StringBuffer();
        sb.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">");
        sb.append(CKMScheduler.LINE_SP);
        sb.append("<html xmlns=\"http://www.w3.org/1999/xhtml\">");
        sb.append(CKMScheduler.LINE_SP);
        sb.append("	<head>");
        sb.append(CKMScheduler.LINE_SP);
        sb.append("		<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"/>");
        sb.append(CKMScheduler.LINE_SP);
        sb.append("		<title>源码定位</title>");
        sb.append(CKMScheduler.LINE_SP);
        sb.append("		<link href=\"http://gov.trs.cn/jsp/cis4/css/SyntaxHighlighter.css\" rel=\"stylesheet\" type=\"text/css\">");
        sb.append(CKMScheduler.LINE_SP);
        sb.append("	</head>");
        sb.append(CKMScheduler.LINE_SP);
        sb.append("	<body> ");
        sb.append(CKMScheduler.LINE_SP);
        sb.append("		<div class=\"sh_code\">");
        sb.append(CKMScheduler.LINE_SP);
        sb.append("			<ol start=\"1\">");
        sb.append(CKMScheduler.LINE_SP);
        sourceEscape = sourceEscape.replaceAll("\r", "");
        sourceEscape = sourceEscape.replaceAll("\n", CKMScheduler.LINE_SP);
        sourceEscape = sourceEscape.replaceAll(" ", "&nbsp;");
        sourceEscape = sourceEscape.replaceAll("\t", "&nbsp;&nbsp;&nbsp;&nbsp;");
        String[] sourceArr = sourceEscape.split(CKMScheduler.LINE_SP);
        for (int i = 0; i < sourceArr.length; i++) {
            String line = sourceArr[i];
            if (i % 2 == 0) {
                sb.append("				<li>" + line + "</li>");
            } else {
                sb.append("				<li class=\"alt\">" + line + "</li>");
            }
            sb.append(CKMScheduler.LINE_SP);
        }
        sb.append("			</ol>");
        sb.append(CKMScheduler.LINE_SP);
        sb.append("		</div>");
        sb.append(CKMScheduler.LINE_SP);
        sb.append("	</body>");
        sb.append(CKMScheduler.LINE_SP);
        sb.append("</html>");
        sb.append(CKMScheduler.LINE_SP);

        // 在源码中增加定位用的脚本定义
        sb.append(CKMScheduler.LINE_SP);
        sb.append("<link href=\"http://gov.trs.cn/jsp/cis4/css/jquery.qtip.min.css\" rel=\"stylesheet\" type=\"text/css\">");
        sb.append(CKMScheduler.LINE_SP);
        sb.append("<script type=\"text/javascript\" src=\"http://gov.trs.cn/jsp/cis4/js/jquery.js\"></script>");
        sb.append(CKMScheduler.LINE_SP);
        sb.append("<script type=\"text/javascript\" src=\"http://gov.trs.cn/jsp/cis4/js/jquery.qtip.min.js\"></script>");
        sb.append(CKMScheduler.LINE_SP);
        sb.append("<script type=\"text/javascript\" src=\"http://gov.trs.cn/jsp/cis4/js/trsposition.js\"></script>");

        String result = sb.toString();
        //将绝对路径中的basUrl去掉，取到相对路径
        String[] urls = unavailableUrlAndParentUrl.getValue().split("/");
        String relUrl = urls[urls.length-1];
        int start = 0;
        int index = 0;
        while (result.indexOf(relUrl, start) > 0) {
            //找到包含相对路径的index后，取得前后双引号内的内容，也就是页面上的链接
            int tmp = result.indexOf(relUrl, start);
            start = tmp + relUrl.length();
            int urlStart = tmp;
            int urlEnd = tmp;
            while (!"&quot;".equals(result.substring(urlStart - 6, urlStart))) {
                urlStart--;
            }
            while (!"&quot;".equals(result.substring(urlEnd + 1, urlEnd + 7))) {
                urlEnd++;
            }
            String srcRelUrl = result.substring(urlStart, urlEnd + 1);
            srcRelUrl = UrlUtils.canonicalizeUrl(srcRelUrl, unavailableUrlAndParentUrl.getKey());
            if (unavailableUrlAndParentUrl.getValue().equals(srcRelUrl)) {
                index = urlStart;
                break;
            }
        }
        if (index == 0) {
            return null;
        } else {
            String msgStr = "状态：" + pageStatusCode + "  [<font color=red>" + getDisplayErrorWord(unavailableUrlAndParentUrl.getValue()) +
                    "</font>]<br>地址：<br><a target=_blank style='color:#0000FF;font-size:12px' href='" + unavailableUrlAndParentUrl.getValue() + "'>" + unavailableUrlAndParentUrl.getValue() +
                    "</a>";
            String errorinfo = "<font trserrid=\"anchor\" msg=\"" + msgStr + "\" msgtitle=\"定位\" style=\"border:2px red solid;color:red;\">" + unavailableUrlAndParentUrl.getValue() +
                    "</font>";
            result = result.substring(0, index) + errorinfo + result.substring(index + unavailableUrlAndParentUrl.getValue().length());
            start = index + errorinfo.length();
        }

        while (result.indexOf(relUrl, start) > 0) {
            int tmp = result.indexOf(relUrl, start);
            start = tmp + relUrl.length();
            int urlStart = tmp;
            int urlEnd = tmp;
            while (!"&quot;".equals(result.substring(urlStart - 6, urlStart))) {
                urlStart--;
            }
            while (!"&quot;".equals(result.substring(urlEnd + 1, urlEnd + 7))) {
                urlEnd++;
            }
            String srcRelUrl = result.substring(urlStart, urlEnd + 1);
            srcRelUrl = UrlUtils.canonicalizeUrl(srcRelUrl, unavailableUrlAndParentUrl.getKey());
            if (unavailableUrlAndParentUrl.getValue().equals(srcRelUrl)) {
                index = urlStart;
                String msgStr = "状态：" + pageStatusCode + "  [<font color=red>" + getDisplayErrorWord(unavailableUrlAndParentUrl.getValue()) +
                        "</font>]<br>地址：<br><a target=_blank style='color:#0000FF;font-size:12px' href='" + unavailableUrlAndParentUrl.getValue() + "'>" + unavailableUrlAndParentUrl.getValue
                        () + "</a>";
                String errorinfo = "<font msg=\"" + msgStr + "\" style=\"border:2px red solid;color:red;\">" + unavailableUrlAndParentUrl.getValue() + "</font>";
                result = result.substring(0, index) + errorinfo + result.substring(index + unavailableUrlAndParentUrl.getValue().length());
                start = index + errorinfo.length();
            }

        }

        return result;
    }

    private String generatePageLocHtmlText(Pair<String, String> unavailableUrlAndParentUrl, String parentUrlContent, Integer pageStatusCode) {
        StringBuffer sb = new StringBuffer();
        // 给网站增加base标签
        sb.append("<base href=\"" + "http://tunchang.hainan.gov.cn/tcgov/" + "\" />");
        sb.append(CKMScheduler.LINE_SP);
        sb.append(parentUrlContent.intern());
        // 在源码中增加定位用的脚本定义
        sb.append(CKMScheduler.LINE_SP);
        sb.append("<link href=\"http://gov.trs.cn/jsp/cis4/css/jquery.qtip.min.css\" rel=\"stylesheet\" type=\"text/css\">");
        sb.append(CKMScheduler.LINE_SP);
        sb.append("<script type=\"text/javascript\" src=\"http://gov.trs.cn/jsp/cis4/js/jquery.js\"></script>");
        sb.append(CKMScheduler.LINE_SP);
        sb.append("<script type=\"text/javascript\" src=\"http://gov.trs.cn/jsp/cis4/js/jquery.qtip.min.js\"></script>");
        sb.append(CKMScheduler.LINE_SP);
        sb.append("<script type=\"text/javascript\" src=\"http://gov.trs.cn/jsp/cis4/js/trsposition.js\"></script>");
        // 解析源码，找到断链标签，加上标记信息。
        String result = sb.toString();

        final Document parseDoc = Jsoup.parse(result, unavailableUrlAndParentUrl.getKey());
        Elements links = parseDoc.select("[href]");
        Elements media = parseDoc.select("[src]");

        // 处理a标签
        for (int i = 0; i < links.size(); i++) {
            Element link = links.get(i);
            String relHref = link.attr("href");
            String absHref = link.attr("abs:href");
            if (StringUtil.isEmpty(absHref)) {
                absHref = relHref;
            }
            if (absHref.equals(unavailableUrlAndParentUrl.getValue())) {
                link.attr("trserrid", "anchor");
                String msgStr = "状态：" + pageStatusCode + "  [<font color=red>" + getDisplayErrorWord(unavailableUrlAndParentUrl.getValue()) +
                        "</font>]<br>地址：<br><a target=_blank style='color:#0000FF;font-size:12px' href='" + unavailableUrlAndParentUrl.getValue() + "'>" + unavailableUrlAndParentUrl
                        .getValue() + "</a>";
                link.attr("msg", msgStr);
                link.attr("msgtitle", "定位");
                link.attr("style", "border:2px red solid;color:red;");
            }
        }
        // 处理img标签
        for (int i = 0; i < media.size(); i++) {
            Element link = media.get(i);
            String relHref = link.attr("src");
            String absHref = link.attr("abs:src");
            if (StringUtil.isEmpty(absHref)) {
                absHref = relHref;
            }
            if (absHref.equals(unavailableUrlAndParentUrl.getValue())) {
                link.attr("trserrid", "anchor");
                String msgStr = "状态：" + pageStatusCode + "  [<font color=red>" + getDisplayErrorWord(unavailableUrlAndParentUrl.getValue()) +
                        "</font>]<br>地址：<br><a target=_blank style='color:#0000FF;font-size:12px' href='" + unavailableUrlAndParentUrl.getValue() + "'>" + unavailableUrlAndParentUrl
                        .getValue() + "</a>";
                link.attr("msg", msgStr);
                link.attr("msgtitle", "定位");
                link.attr("style", "border:2px red solid;color:red;");
            }
        }

        result = parseDoc.html();

        return result;
    }

    private String getDisplayErrorWord(String url) {
        return "无效链接：" + url;
    }

    private void updateOrInsertSpeed(List<ReplySpeed> pageSpaceList, ReplySpeed replySpeedTo) {
        if (pageSpaceList.isEmpty()) {
            replySpeedTo.setSiteId(siteId);
            webPageService.insertReplyspeed(replySpeedTo);
        } else {
            webPageMapper.updateReplySpeed(replySpeedTo);
        }
    }

    private void updateOrInsertSpace(List<PageSpace> pageSpaceList, PageSpace pageSpaceTo) {
        if (pageSpaceList.isEmpty()) {
            pageSpaceTo.setSiteId(siteId);
            webPageService.insertPageSpace(pageSpaceTo);
        } else {
            webPageMapper.updatePageSpace(pageSpaceTo);
        }
    }

    private void updateOrInsertLength(List<UrlLength> urlLenghtList, UrlLength urlLenghtTo) {
        if (urlLenghtList.isEmpty()) {
            urlLenghtTo.setSiteId(siteId);
            webPageService.insertUrlLength(urlLenghtTo);
        } else {
            webPageMapper.updateUrlLength(urlLenghtTo);
        }
    }

    private void updateOrInsertDepth(List<PageDepth> pageDepthList, PageDepth pageDepthTo) {
        if (pageDepthList.isEmpty()) {
            pageDepthTo.setSiteId(siteId);
            webPageService.insertPageDepth(pageDepthTo);
        } else {
            webPageMapper.updatePageDepth(pageDepthTo);
        }
    }

    private String[] imageSuffixs = new String[]{"bmp", "jpg", "jpeg", "png", "gif"};

    private String[] fileSuffixs = new String[]{"zip", "doc", "xls", "xlsx", "docx", "rar"};

    private Types.LinkAvailableIssueType getTypeByLink(String url) {

        String suffix = url.substring(url.lastIndexOf('.') + 1);
        for (String imageSuffix : imageSuffixs) {

            if (StringUtils.equalsIgnoreCase(suffix, imageSuffix)) {

                return Types.LinkAvailableIssueType.INVALID_IMAGE;
            }
        }

        for (String fileSuffix : fileSuffixs) {

            if (StringUtils.equalsIgnoreCase(suffix, fileSuffix)) {

                return Types.LinkAvailableIssueType.INVALID_FILE;
            }
        }

        return Types.LinkAvailableIssueType.INVALID_LINK;
    }
}
