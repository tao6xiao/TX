package com.trs.gov.kpi.scheduler;

import com.trs.gov.kpi.utils.SpiderUtils;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by wangxuan on 2017/5/10.
 * 定时任务，抓取网站链接是否可用
 */
@Slf4j
@Component
public class LinkAnalysisScheduler extends AbstractScheduler{

    @Getter
    private String name = "link_analysis";

    //用于存放所有站点的入口url
    @Getter
    private List<String> baseUrls = new LinkedList<>();

    @Getter
    private final Runnable task = new Runnable() {

        @Override
        public void run() {

            log.info("check start...");
            SpiderUtils.linkCheck(1, baseUrls.toArray(new String[0]));
        }
    };

}
