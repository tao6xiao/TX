package com.trs.gov.kpi.entity.responsedata;

import com.trs.gov.kpi.entity.Issue;
import lombok.Data;

import java.util.Date;

/**
 * 综合监测：预警提醒返回数据处理对象
 * Created by he.lang on 2017/5/19.
 */
@Data
public class IssueWarningResponse {
    private String id;

    private String issueTypeName;

    private String detail;

    private String issueTime;

    private String chnlName;

    private Long limitTime;
}
