package com.trs.gov.kpi.entity.msg;

import lombok.Data;

/**
 * Created by linwei on 2017/7/12.
 */
@Data
public class PageInfoMsg implements IMQMsg {

    public static final String MSG_TYPE = "PageInfoMsg";

    // 站点id
    private int siteId;

    // 检查编号
    private int checkId;

    // 网页url
    private String url;

    // parent url
    private String parentUrl;

    // 访问速度
    private long speed;

    // 内容
    private String content;

    @Override
    public String getType() {
        return MSG_TYPE;
    }

}
