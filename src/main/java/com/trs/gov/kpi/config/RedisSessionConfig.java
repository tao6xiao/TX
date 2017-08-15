package com.trs.gov.kpi.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * Created by linwei on 2017/8/8.
 */

//@Configuration
//maxInactiveIntervalInSeconds session超时时间,单位秒, 默认30分钟超时
//@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 1800)
public class RedisSessionConfig {
}
