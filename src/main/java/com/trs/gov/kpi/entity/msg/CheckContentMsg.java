package com.trs.gov.kpi.entity.msg;

import lombok.Data;

/**
 * 内容检测消息
 *
 * Created by li.hao on 2017/7/11.
 */
@Data
public class CheckContentMsg {

    // 站点id
    private int siteId;

    // 检查编号
    private int checkId;

    // 网页url
    private String url;

    // parent url
    private String parentUrl;

    // 内容
    private String content;

}
