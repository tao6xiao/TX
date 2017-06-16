package com.trs.gov.kpi.entity.responsedata;

import lombok.Data;

import java.util.Objects;

/**
 * Created by linwei on 2017/6/15.
 */
@Data
public class SiteDocMultiCounterResponse extends DocMultiCounterResponse {

    // 站点编号
    private Long siteId;

    // 站点名称
    private String siteName;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        SiteDocMultiCounterResponse that = (SiteDocMultiCounterResponse) o;
        return Objects.equals(getSiteId(), that.getSiteId()) &&
                Objects.equals(getSiteName(), that.getSiteName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getSiteId(), getSiteName());
    }
}
