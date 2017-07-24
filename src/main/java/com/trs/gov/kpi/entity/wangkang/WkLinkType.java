package com.trs.gov.kpi.entity.wangkang;

import com.trs.gov.kpi.annotation.DBField;
import com.trs.gov.kpi.annotation.DBTable;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * Created by li.hao on 2017/7/14.
 */
@DBTable("wklinktype")
public class WkLinkType {

    @Setter
    @Getter
    @DBField
    private Integer id;

    @Setter
    @Getter
    @DBField
    private Integer checkId;//检查编号

    @Setter
    @Getter
    @DBField
    private Integer siteId;//网站编号

    @Getter
    @DBField
    private Integer allLink = 0;//链接总数

    @Getter
    @DBField
    private Integer webLink = 0;//网页个数

    @Getter
    @DBField
    private Integer imageLink = 0;//图片个数

    @Getter
    @DBField
    private Integer videoLink = 0;//视频个数

    @Getter
    @DBField
    private Integer enclosuLink = 0;//附件个数

    @Setter
    @Getter
    @DBField
    private Date checkTime;//入库时间

    public void calcTotal() {
        allLink = webLink + imageLink + videoLink + enclosuLink;
    }

    public void setWebLink(Integer webLink) {
        if (webLink != null) {
            this.webLink = webLink;
        }
    }

    public void setImageLink(Integer imageLink) {
        if (imageLink != null) {
            this.imageLink = imageLink;
        }
    }

    public void setVideoLink(Integer videoLink) {
        if (videoLink != null) {
            this.videoLink = videoLink;
        }
    }

    public void setEnclosuLink(Integer enclosuLink) {
        if (enclosuLink != null) {
            this.enclosuLink = enclosuLink;
        }
    }
}
