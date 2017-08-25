package com.trs.gov.kpi.scheduler;

import com.trs.gov.kpi.constant.EnumCheckJobType;
import com.trs.gov.kpi.constant.SchedulerType;
import com.trs.gov.kpi.entity.exception.BizException;
import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.gov.kpi.service.outer.SiteApiService;
import com.trs.gov.kpi.utils.OuterApiServiceUtil;
import com.trs.gov.kpi.utils.SpiderUtils;
import com.trs.gov.kpi.utils.SpringContextUtil;
import com.trs.gov.kpi.utils.StringUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.Serializable;

/**
 * Created by wangxuan on 2017/5/10.
 * 定时任务，抓取网站链接是否可用
 */
@Slf4j
@Component
@Scope("prototype")
public class LinkAnalysisScheduler implements SchedulerTask, Serializable {

    @Resource
    private transient SiteApiService siteApiService;

    @Resource
    private transient SpiderUtils spider;

    @Setter
    @Getter
    private Integer siteId;

    @Setter
    @Getter
    private String baseUrl;

    @Setter
    @Getter
    private Boolean isTimeNode;

    //站点监测状态（0：自动监测；1：手动监测）
    @Setter
    @Getter
    private int monitorType;

    @Getter
    private EnumCheckJobType checkJobType = EnumCheckJobType.CHECK_LINK;

    @Getter
    private int monitorResult;

    @Override
    public void run() throws RemoteException, BizException {

        siteApiService = SpringContextUtil.getBean(SiteApiService.class);
        spider = SpringContextUtil.getBean(SpiderUtils.class);

        baseUrl = OuterApiServiceUtil.getUrl(siteApiService.getSiteById(siteId, null));
        if (StringUtil.isEmpty(baseUrl)) {
            return;
        }
        spider.linkCheck(3, siteId, baseUrl);//测试url：http://tunchang.hainan.gov.cn/tcgov/

        monitorResult = spider.getIssueCount();
    }

    @Override
    public String getName() {
        return SchedulerType.LINK_ANALYSIS_SCHEDULER.toString();
    }

}
