package com.trs.gov.kpi.entity.outerapi;

import lombok.Data;

/**
 * Created by linwei on 2017/5/24.
 */
public class Document {

    private Integer siteId;

    private int channelId;

    private int metaDataId;

    private String  docContent = "";

    private String docTitle = "";

    private String docPubUrl;

    public Integer getSiteId() {
        return siteId;
    }

    public void setSiteId(Integer siteId) {
        this.siteId = siteId;
    }

    public int getChannelId() {
        return channelId;
    }

    public void setChannelId(int channelId) {
        this.channelId = channelId;
    }

    public int getMetaDataId() {
        return metaDataId;
    }

    public void setMetaDataId(int metaDataId) {
        this.metaDataId = metaDataId;
    }

    public String getDocContent() {
        return docContent;
    }

    public void setDocContent(String docContent) {
        if (docContent != null) {
            this.docContent = docContent;
        }
    }

    public String getDocTitle() {
        return docTitle;
    }

    public void setDocTitle(String docTitle) {
        if (docTitle != null) {
            this.docTitle = docTitle;
        }
    }

    public String getDocPubUrl() {
        return docPubUrl;
    }

    public void setDocPubUrl(String docPubUrl) {
        this.docPubUrl = docPubUrl;
    }
//    "CHANNELID" : "19",
//            "WCMMETATABLEGOVDOCWEBSITEID" : "7",
//            "DOCRELTIME" : "2017-04-25 15:00:00", // 撰写时间
//            "DOCAUTHOR"  : "张三", // 撰写人（作者）
//            "DOCSOURCENAME" : "四川省政府", // 来源
//            "DOCLINK" : "http://www.baidu.com", // 链接
//            "METADATAID" : "287",    // 等同于DocId
//            "DOCCONTENT" : "正文\n图片是： dafdsf",
//            "DOCTITLE" : "二级分类的第【6】篇文章",
//            "TITLECOLOR" : "#FFF",
//            "DOCHTMLCON" : "正文<br/>图片是：<p> dafdsf</p>",
//            "CRUSER" : "admin",
//            "CRTIME" : "2017-03-23 14:07:46",
//            "DOCTYPE" : "20",
//            "DOCSTATUS" : "2",  // 1，2-待编（1-新稿，2-经过编辑），18-送审，16-送签，10-已发
}
