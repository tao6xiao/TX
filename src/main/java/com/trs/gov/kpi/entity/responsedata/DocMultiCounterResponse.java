package com.trs.gov.kpi.entity.responsedata;

import lombok.Data;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        DocMultiCounterResponse that = (DocMultiCounterResponse) o;
        return Objects.equals(getDaibianDocCount(), that.getDaibianDocCount()) &&
                Objects.equals(getDaishenDocCount(), that.getDaishenDocCount()) &&
                Objects.equals(getDaiqianDocCount(), that.getDaiqianDocCount()) &&
                Objects.equals(getYifaDocCount(), that.getYifaDocCount()) &&
                Objects.equals(getNewDocCount(), that.getNewDocCount()) &&
                Objects.equals(getQuoteDocCount(), that.getQuoteDocCount()) &&
                Objects.equals(getMirrorDocCount(), that.getMirrorDocCount()) &&
                Objects.equals(getCopyDocCount(), that.getCopyDocCount()) &&
                Objects.equals(getPushDocCount(), that.getPushDocCount()) &&
                Objects.equals(getDistributeDocCount(), that.getDistributeDocCount());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getDaibianDocCount(), getDaishenDocCount(), getDaiqianDocCount(), getYifaDocCount(), getNewDocCount(), getQuoteDocCount(), getMirrorDocCount(), getCopyDocCount(), getPushDocCount(), getDistributeDocCount());
    }
}
