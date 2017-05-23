package com.trs.gov.kpi.entity.check;

import com.trs.gov.kpi.entity.outerapi.Channel;
import lombok.Data;

/**
 *
 * 用于对栏目更新进行检查
 * 涉及到更新不及时，更新预警，自查更新提示
 *
 * Created by linwei on 2017/5/23.
 */
@Data
public class CheckingChannel {

    // 栏目
    private Channel channel;

    // 当前周期的起始时间
    private String beginDateTime;

    // 当前周期的结束时间
    private String endDateTime;

    // 当前周期的预警提醒起始时间
    private String warningBeginDateTime;

    // 当前周期的预警提醒结束时间
    private String warningEndDateTime;

    // 预警天数
    private int warningDay;

    // 自查更新天数
    private int selfCheckDay;

    // 是否进行更新不及时和预警更新检查
    private boolean shouldIssueCheck;

    // 是否进行自查更新提示
    private boolean shouldSelfCheck;

    // 是否为问题
    private boolean isIssue = false;

    // 是否预警
    private boolean isWarning = false;

    // 是否自查提醒
    private boolean isSelfWarning = false;
}
