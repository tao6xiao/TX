package com.trs.gov.kpi.entity.responsedata;

import lombok.Data;

/**
 * Created by linwei on 2017/6/15.
 */
@Data
public class DocMultiCounterResponse {
    private Long daibianDocCount; // 待编数量,
    private Long daishenDocCount; // 待审数量,
    private Long daiqianDocCount; // 待签数量,
    private Long yifaDocCount; // 已发数量,
    private Long newDocCount; // 原稿数量
    private Long quoteDocCount; // 链接引用数量
    private Long mirrorDocCount; // 镜像引用数量
    private Long copyDocCount; // 复制数量
    private Long pushDocCount; // 上报数量
    private Long distributeDocCount; // 下达数量
}
