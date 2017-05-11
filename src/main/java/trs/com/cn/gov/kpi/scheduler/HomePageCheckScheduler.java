package trs.com.cn.gov.kpi.scheduler;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import trs.com.cn.gov.kpi.utils.SpiderUtils;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

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
