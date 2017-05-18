package com.trs.gov.kpi.entity.outerapi;

import lombok.Data;

/**
 * Created by linwei on 2017/5/18.
 */
@Data
public class Channel {

    private boolean hasChildren;

    private int siteId;

    private int channelId;

    private String chnlName;

    private int parentId;


//    "HASCHILDREN" : "false",
//            "CHNLDETAILTEMP" : "0",
//            "OPERTIME" : "",
//            "STATUS" : "0",
//            "INHERIT" : "0",
//            "CONTENTADDEDITPAGE" : "../document/document_addedit.jsp",
//            "CRTIME" : "2009-07-21 11:53:29",
//            "CHANNELID" : "1",
//            "ATTRIBUTE" : "",
//            "LASTMODIFYTIME" : "",
//            "CONTENTSHOWPAGE" : "../document/document_detail_show.html",
//            "ADVTOOLBAR" : "",
//            "SITEID" : "1",
//            "CHNLQUERY" : "",
//            "CHNLOUTLINETEMP" : "0",
//            "ISCLUSTER" : "0",
//            "LINKURL" : "",
//            "CRUSER" : "admin",
//            "CHNLNAME" : "头条新闻",
//            "CHNLORDER" : "2",
//            "CHNLORDERBY" : "",
//            "CHNLTYPE" : "2",
//            "TOOLBAR" : "",
//            "CHNLDESC" : "头条新闻",
//            "FLUENCEXML" : "",
//            "ISSUBSCRIBE" : "0",
//            "ISCONTAINSCHILDREN" : "0",
//            "CONTENTLISTPAGE" : "../document/document_list.html",
//            "TRUENAME" : "系统管理员",
//            "PUBLISHPRO" : "1",
//            "PARENTID" : "0",
//            "CHNLPROP" : "4"
}
