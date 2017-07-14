package com.trs.gov.kpi.entity.msg;

import lombok.Data;

/**
 * Created by linwei on 2017/7/12.
 */
@Data
public class InvalidLinkMsg implements IMQMsg {

    public static final String MSG_TYPE = "InvalidLinkMsg";

    // 站点id
    private int siteId;

    // 检查编号
    private int checkId;

    // 网页url
    private String url;

    // parent url
    private String parentUrl;

    // 内容
    private String parentContent;

    // 错误码
    private int errorCode = 404;

    @Override
    public String getType() {
        return MSG_TYPE;
    }
}
