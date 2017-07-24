package com.trs.gov.kpi.processor;

import com.esotericsoftware.minlog.Log;
import com.trs.gov.kpi.constant.Types;
import com.trs.gov.kpi.dao.CommonMapper;
import com.trs.gov.kpi.entity.msg.CalcScoreMsg;
import com.trs.gov.kpi.entity.msg.CheckEndMsg;
import com.trs.gov.kpi.entity.msg.IMQMsg;
import com.trs.gov.kpi.entity.msg.InvalidLinkMsg;
import com.trs.gov.kpi.entity.wangkang.WkAllStats;
import com.trs.gov.kpi.entity.wangkang.WkIssue;
import com.trs.gov.kpi.entity.wangkang.WkIssueCount;
import com.trs.gov.kpi.entity.wangkang.WkScore;
import com.trs.gov.kpi.msgqueue.CommonMQ;
import com.trs.gov.kpi.msgqueue.MQListener;
import com.trs.gov.kpi.service.wangkang.WkAllStatsService;
import com.trs.gov.kpi.service.wangkang.WkIssueService;
import com.trs.gov.kpi.service.wangkang.WkScoreService;
import com.trs.gov.kpi.utils.DBUtil;
import com.trs.gov.kpi.utils.StringUtil;
import com.trs.gov.kpi.utils.WebPageUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringEscapeUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Created by linwei on 2017/7/12.
 */
@Slf4j
@Component
public class InvalidLinkProcessor implements MQListener {

    private final String name = "InvalidLinkProcessor";

    @Value("${wk.location.dir}")
    private String locationDir;

    @Resource
    private CommonMapper commonMapper;

    @Resource
    private WkIssueService wkIssueService;

    @Resource
    private WkAllStatsService wkAllStatsService;

    @Resource
    private WkScoreService wkScoreService;

    @Resource
    private CommonMQ commonMQ;

    @Override
    public String getType() {
        return InvalidLinkMsg.MSG_TYPE;
    }

    @Override
    public void onMessage(IMQMsg msg) {

        if (msg.getType().endsWith(CheckEndMsg.MSG_TYPE)) {
            CheckEndMsg checkEndMsg = (CheckEndMsg)msg;
            try {
                WkAllStats wkAllStats = new WkAllStats();
                wkAllStats.setSiteId(checkEndMsg.getSiteId());
                wkAllStats.setCheckId(checkEndMsg.getCheckId());
                wkAllStats.setInvalidLink(wkIssueService.getInvalidLinkCount(checkEndMsg.getSiteId(), checkEndMsg.getCheckId()));
                wkAllStatsService.insertOrUpdateInvalidLink(wkAllStats);

                // 比较并记录入库
                compareLastCheckAndInsert(checkEndMsg.getSiteId(), checkEndMsg.getCheckId());

                final int invalidLinkCount = wkIssueService.getInvalidLinkCount(checkEndMsg.getSiteId(), checkEndMsg.getCheckId());
                calcScoreAndInsert(checkEndMsg.getSiteId(), checkEndMsg.getCheckId() ,invalidLinkCount);
            } catch (Throwable e) {
                log.error("", e);
            }

            CalcScoreMsg calcSpeedScoreMsg = new CalcScoreMsg();
            calcSpeedScoreMsg.setCheckId(checkEndMsg.getCheckId());
            calcSpeedScoreMsg.setSiteId(checkEndMsg.getSiteId());
            calcSpeedScoreMsg.setScoreType("invalidLink");
            commonMQ.publishMsg(calcSpeedScoreMsg);

        } else {
            InvalidLinkMsg invalidLinkMsg = (InvalidLinkMsg)msg;

            Types.WkLinkIssueType issueType = WebPageUtil.toWkLinkType(invalidLinkMsg.getUrlType());

            WkIssue issue = new WkIssue();
            final String relativeDir = CKMProcessWorker.getRelativeDir(invalidLinkMsg.getSiteId(), invalidLinkMsg.getCheckId(), invalidLinkMsg.getUrl(), 1, 2);

            try {
                String absoluteDir = locationDir + File.separator + relativeDir;
                CKMProcessWorker.createDir(absoluteDir);

                // 网页定位
                String pageLocContent = generatePageLocHtmlText(invalidLinkMsg);
                if (pageLocContent == null) {
                    pageLocContent = "";
                }
                CKMProcessWorker.createPagePosHtml(absoluteDir, pageLocContent);

                // 源码定位
                String srcLocContent = generateSourceLocHtmlText(invalidLinkMsg);
                if (srcLocContent == null) {
                    srcLocContent = "";
                }
                CKMProcessWorker.createSrcPosHtml(absoluteDir, srcLocContent);

                // 创建头部导航页面
                CKMProcessWorker.createContHtml(absoluteDir, invalidLinkMsg.getUrl(), invalidLinkMsg.getParentUrl());

                // 创建首页
                CKMProcessWorker.createIndexHtml(absoluteDir);
            } catch (Throwable e) {
                log.error("generate location pages error! relativeDir = " + relativeDir, e);
            }

            issue.setLocationUrl("gov/wangkang/loc/" +  relativeDir.replace(File.separator, "/") + "/" + "index.html");
            issue.setTypeId(Types.WkSiteCheckType.INVALID_LINK.value);
            issue.setSubTypeId(issueType.value);
            issue.setSiteId(invalidLinkMsg.getSiteId());
            issue.setUrl(invalidLinkMsg.getUrl());
            issue.setCheckTime(new Date());
            issue.setCheckId(invalidLinkMsg.getCheckId());
            issue.setParentUrl(invalidLinkMsg.getParentUrl());
            issue.setChnlName(CKMProcessWorker.getChnlName(invalidLinkMsg.getParentUrl()));
            issue.setDetailInfo(String.valueOf(invalidLinkMsg.getErrorCode()));
            commonMapper.insert(DBUtil.toRow(issue));
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InvalidLinkProcessor that = (InvalidLinkProcessor) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }


    private String generatePageLocHtmlText(InvalidLinkMsg msg) {

        StringBuffer sb = new StringBuffer();
        // 给网站增加base标签
        sb.append("<base href=\"" + msg.getParentUrl() + "\" />");
        sb.append(CKMProcessWorker.LINE_SP);
        sb.append(msg.getParentContent().intern());
        // 在源码中增加定位用的脚本定义
        sb.append(CKMProcessWorker.LINE_SP);
        sb.append("<link href=\"http://gov.trs.cn/jsp/cis4/css/jquery.qtip.min.css\" rel=\"stylesheet\" type=\"text/css\">");
        sb.append(CKMProcessWorker.LINE_SP);
        sb.append("<script type=\"text/javascript\" src=\"http://gov.trs.cn/jsp/cis4/js/jquery.js\"></script>");
        sb.append(CKMProcessWorker.LINE_SP);
        sb.append("<script type=\"text/javascript\" src=\"http://gov.trs.cn/jsp/cis4/js/jquery.qtip.min.js\"></script>");
        sb.append(CKMProcessWorker.LINE_SP);
        sb.append("<script type=\"text/javascript\" src=\"http://gov.trs.cn/jsp/cis4/js/trsposition.js\"></script>");
        // 解析源码，找到断链标签，加上标记信息。
        String result = sb.toString();

        final Document parseDoc = Jsoup.parse(result, msg.getParentUrl());
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
            if (absHref.equals(msg.getUrl())) {
                link.attr("trserrid", "anchor");
                String msgStr = "状态：" + msg.getErrorCode() + "  [<font color=red>" + getDisplayErrorWord(msg.getUrl()) + "</font>]<br>地址：<br><a target=_blank style='color:#0000FF;font-size:12px' href='" + msg.getUrl() + "'>" + msg.getUrl() + "</a>";
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
            if (absHref.equals(msg.getUrl())) {
                link.attr("trserrid", "anchor");
                String msgStr = "状态：" + msg.getErrorCode() + "  [<font color=red>" + getDisplayErrorWord(msg.getUrl()) + "</font>]<br>地址：<br><a target=_blank style='color:#0000FF;font-size:12px' href='" + msg.getUrl() + "'>" + msg.getUrl() + "</a>";
                link.attr("msg", msgStr);
                link.attr("msgtitle", "定位");
                link.attr("style", "border:2px red solid;color:red;");
            }
        }
        if (parseDoc != null) {
            result = parseDoc.html();
        }

        return result;
    }

    private String getDisplayErrorWord(String url) {
        return "无效链接：" + url;
    }

    private String generateSourceLocHtmlText(InvalidLinkMsg msg) {

        // 将html标签转义
        String sourceEscape = StringEscapeUtils.escapeHtml4(msg.getParentContent());
        StringBuffer sb = new StringBuffer();
        sb.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">");
        sb.append(CKMProcessWorker.LINE_SP);
        sb.append("<html xmlns=\"http://www.w3.org/1999/xhtml\">");
        sb.append(CKMProcessWorker.LINE_SP);
        sb.append("	<head>");
        sb.append(CKMProcessWorker.LINE_SP);
        sb.append("		<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"/>");
        sb.append(CKMProcessWorker.LINE_SP);
        sb.append("		<title>源码定位</title>");
        sb.append(CKMProcessWorker.LINE_SP);
        sb.append("		<link href=\"http://gov.trs.cn/jsp/cis4/css/SyntaxHighlighter.css\" rel=\"stylesheet\" type=\"text/css\">");
        sb.append(CKMProcessWorker.LINE_SP);
        sb.append("	</head>");
        sb.append(CKMProcessWorker.LINE_SP);
        sb.append("	<body> ");
        sb.append(CKMProcessWorker.LINE_SP);
        sb.append("		<div class=\"sh_code\">");
        sb.append(CKMProcessWorker.LINE_SP);
        sb.append("			<ol start=\"1\">");
        sb.append(CKMProcessWorker.LINE_SP);
        sourceEscape = sourceEscape.replaceAll("\r", "");
        sourceEscape = sourceEscape.replaceAll("\n", CKMProcessWorker.LINE_SP);
        sourceEscape = sourceEscape.replaceAll(" ", "&nbsp;");
        sourceEscape = sourceEscape.replaceAll("\t", "&nbsp;&nbsp;&nbsp;&nbsp;");
        String[] sourceArr = sourceEscape.split(CKMProcessWorker.LINE_SP);
        for (int i = 0; i < sourceArr.length; i++) {
            String line = sourceArr[i];
            if (i % 2 == 0) {
                sb.append("				<li>" + line + "</li>");
            } else {
                sb.append("				<li class=\"alt\">" + line + "</li>");
            }
            sb.append(CKMProcessWorker.LINE_SP);
        }
        sb.append("			</ol>");
        sb.append(CKMProcessWorker.LINE_SP);
        sb.append("		</div>");
        sb.append(CKMProcessWorker.LINE_SP);
        sb.append("	</body>");
        sb.append(CKMProcessWorker.LINE_SP);
        sb.append("</html>");
        sb.append(CKMProcessWorker.LINE_SP);

        // 在源码中增加定位用的脚本定义
        sb.append(CKMProcessWorker.LINE_SP);
        sb.append("<link href=\"http://gov.trs.cn/jsp/cis4/css/jquery.qtip.min.css\" rel=\"stylesheet\" type=\"text/css\">");
        sb.append(CKMProcessWorker.LINE_SP);
        sb.append("<script type=\"text/javascript\" src=\"http://gov.trs.cn/jsp/cis4/js/jquery.js\"></script>");
        sb.append(CKMProcessWorker.LINE_SP);
        sb.append("<script type=\"text/javascript\" src=\"http://gov.trs.cn/jsp/cis4/js/jquery.qtip.min.js\"></script>");
        sb.append(CKMProcessWorker.LINE_SP);
        sb.append("<script type=\"text/javascript\" src=\"http://gov.trs.cn/jsp/cis4/js/trsposition.js\"></script>");

        String result = sb.toString();
        int index = result.indexOf(msg.getUrl(), 0);
        if (index == -1) {
            return result;
        } else {
            String msgStr = "状态：" + msg.getErrorCode() + "  [<font color=red>" + getDisplayErrorWord(msg.getUrl()) + "</font>]<br>地址：<br><a target=_blank style='color:#0000FF;font-size:12px' href='" + msg.getUrl() + "'>" + msg.getUrl() + "</a>";
            String errorinfo = "<font trserrid=\"anchor\" msg=\"" + msgStr + "\" msgtitle=\"定位\" style=\"border:2px red solid;color:red;\">" + msg.getUrl() + "</font>";
            result = result.substring(0, index) + errorinfo + result.substring(index + msg.getUrl().length());
            index = result.indexOf(msg.getUrl(), index + errorinfo.length());
        }

        while (index != -1) {
            String msgStr = "状态：" + msg.getErrorCode() + "  [<font color=red>" + getDisplayErrorWord(msg.getUrl()) + "</font>]<br>地址：<br><a target=_blank style='color:#0000FF;font-size:12px' href='" + msg.getUrl() + "'>" + msg.getUrl() + "</a>";
            String errorinfo = "<font msg=\"" + msgStr + "\" style=\"border:2px red solid;color:red;\">" + msg.getUrl() + "</font>";
            result = result.substring(0, index) + errorinfo + result.substring(index + msg.getUrl().length());
            index = result.indexOf(msg.getUrl(), index + errorinfo.length());
        }

        return result;
    }


    private void compareLastCheckAndInsert(Integer siteId, Integer checkId) {

        Integer lastCheckid= wkAllStatsService.getLastCheckId(siteId, checkId);
        if (lastCheckid == null) {
            // 第一次检查
            final int invalidLinkCount = wkIssueService.getInvalidLinkCount(siteId, checkId);
            WkIssueCount issueCount = new WkIssueCount();
            issueCount.setCheckId(checkId);
            issueCount.setCheckTime(new Date());
            issueCount.setIsResolved(0);
            issueCount.setUnResolved(invalidLinkCount);
            issueCount.setSiteId(siteId);
            issueCount.setTypeId(Types.WkSiteCheckType.INVALID_LINK.value);
            commonMapper.insert(DBUtil.toRow(issueCount));
        } else {
            final List<WkIssue> curInvalidLinkList = wkIssueService.getInvalidLinkList(siteId, checkId);
            final List<WkIssue> lastInvalidLinkList = wkIssueService.getInvalidLinkList(siteId, lastCheckid);

            int resolvedCount = 0;
            boolean isResolved;
            for (WkIssue lastLink : lastInvalidLinkList) {
                isResolved = true;
                for (int index = 0; index < curInvalidLinkList.size(); index++) {
                    if (lastLink.getUrl().equals(curInvalidLinkList.get(index).getUrl())
                            && lastLink.getParentUrl().equals(curInvalidLinkList.get(index).getParentUrl())) {
                        isResolved = false;
                        break;
                    }
                }
                if (isResolved) {
                    resolvedCount++;
                }
            }

            final int invalidLinkCount = wkIssueService.getInvalidLinkCount(siteId, checkId);
            WkIssueCount issueCount = new WkIssueCount();
            issueCount.setCheckId(checkId);
            issueCount.setCheckTime(new Date());
            issueCount.setIsResolved(resolvedCount);
            issueCount.setUnResolved(invalidLinkCount);
            issueCount.setSiteId(siteId);
            issueCount.setTypeId(Types.WkSiteCheckType.INVALID_LINK.value);
            commonMapper.insert(DBUtil.toRow(issueCount));
        }
    }

    private void calcScoreAndInsert(Integer siteId, Integer checkId, int invalidLinkCount) {
        /**
         * SL链接得分(权值40%)	 	=L1x50%+L2x20%+L3x30%
         * L1. L1(常规链接错误率R百分比得分)			=	100(1-ln(R/100+1))
         *      网页链接错误、图片链接错误、视频链接错误、附件链接错误。
         * L2. (其它链接错误率R百分比得分)				=	100(1-ln(R/100+1))
         *      js、css等文件链接错误。
         * L3. (错误链接最小层次L得分)					=	100ln(L/10+1) 		L<10
         */

        int invalidLinkScore = 100;
        if (invalidLinkCount > 0) {
            int routineLinkCount = wkIssueService.getRoutineLinkCount(siteId, checkId);
            double routineLinkR = routineLinkCount / invalidLinkCount;
            double linkL1Log = Math.log(routineLinkR/100 + 1);
            double linkL1Score = 100 * (1 - linkL1Log);

            int othersLinkCount = wkIssueService.getOthersLinkCount(siteId, checkId);
            double othersLinkCountR = othersLinkCount / invalidLinkCount;
            double linkL2Log = Math.log(othersLinkCountR/100 + 1);
            double linkL2Score = 100 * (1 - linkL2Log);

            // TODO: 求出错误链接最小层次L(目前规则待定；L3给定默认值100)
//            double L = 11;
//            if (10 <= L){
//                L = 10;
//            }
//            double linkL3 = Math.log(L/10 + 1);
//            double linkL3Score = 100 * linkL3;
            double linkL3Score = 100;

            invalidLinkScore = (int)(linkL1Score * 0.5 + linkL2Score * 0.2 + linkL3Score * 0.3);
            if (invalidLinkScore < 0) {
                invalidLinkScore = 0;
            }
        }
        WkScore score = new WkScore();
        score.setSiteId(siteId);
        score.setCheckId(checkId);
        score.setCheckTime(new Date());
        score.setInvalidLink(invalidLinkScore);

        wkScoreService.insertOrUpdateInvalidLink(score);
    }
}
