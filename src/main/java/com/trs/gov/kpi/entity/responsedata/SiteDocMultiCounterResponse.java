package com.trs.gov.kpi.entity.responsedata;

import com.trs.gov.kpi.constant.ErrorType;
import com.trs.gov.kpi.constant.OperationType;
import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.gov.kpi.entity.outerapi.Site;
import com.trs.gov.kpi.service.outer.SiteApiService;
import com.trs.gov.kpi.utils.LogUtil;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

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
            final Site site = siteApiService.getSiteById(id.intValue(), null);
            if (site != null) {
                this.siteName = site.getSiteName();
            } else {
                this.siteName = "SiteId[" + id + "]";
            }
        } catch (RemoteException e) {
            log.error("调用外部接口 getSiteById 失败", e);
            LogUtil.addErrorLog(OperationType.REMOTE, ErrorType.REMOTE_FAILED, "调用外部接口 getSiteById 失败", e);
        }
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
