package com.trs.gov.kpi.scheduler;

import com.trs.gov.kpi.utils.SpiderUtils;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by wangxuan on 2017/5/11.
 */
@Slf4j
@Component
public class HomePageCheckScheduler extends AbstractScheduler {

    @Getter
    public final String name = "home_page_check";

    @Getter
    private List<String> hosts = new LinkedList<>();

    @Getter
    private final Runnable task = new Runnable() {

        @Override
        public void run() {

            log.info("check start...");
            SpiderUtils.homePageCheck(hosts.toArray(new String[0]));
        }
    };
}
