package com.trs.gov.kpi.entity.responsedata;

import lombok.Data;

/**
 * Created by linwei on 2017/6/15.
 */
@Data
public class DocMultiCounterResponse {
    private Long daibianDocCount = 0L; // 待编数量,
    private Long daishenDocCount = 0L; // 待审数量,
    private Long daiqianDocCount = 0L; // 待签数量,
    private Long yifaDocCount = 0L; // 已发数量,
    private Long newDocCount = 0L; // 原稿数量
    private Long quoteDocCount = 0L; // 链接引用数量
    private Long mirrorDocCount = 0L; // 镜像引用数量
    private Long copyDocCount = 0L; // 复制数量
    private Long pushDocCount = 0L; // 上报数量
    private Long distributeDocCount = 0L; // 下达数量

}
