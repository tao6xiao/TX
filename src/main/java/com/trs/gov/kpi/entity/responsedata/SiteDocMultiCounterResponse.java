package com.trs.gov.kpi.entity.responsedata;

import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.gov.kpi.entity.outerapi.Site;
import com.trs.gov.kpi.service.outer.SiteApiService;
import com.trs.gov.kpi.utils.SpringContextUtil;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * Created by linwei on 2017/6/15.
 */
@Component
@Scope("prototype")
@Slf4j
public class SiteDocMultiCounterResponse extends DocMultiCounterResponse {

    @Resource
    private SiteApiService siteApiService;

    // 站点编号
    @Getter
    private Long siteId;

    // 站点名称
    @Getter
    private String siteName;



    public void setSiteId(Long id) {
        this.siteId = id;
        try {
//            SiteApiService siteApiService = (SiteApiService) SpringContextUtil.getBean(SiteApiService.class);
            final Site site = siteApiService.getSiteById(id.intValue(), null);
            if (site != null) {
                this.siteName = site.getSiteName();
            } else {
                this.siteName = "SiteId[" + id + "]";
            }
        } catch (RemoteException e) {
            log.error("", e);
        }
    }

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
