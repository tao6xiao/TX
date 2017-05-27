package com.trs.gov.kpi.entity.responsedata;

import lombok.Data;

/**
 * Created by he.lang on 2017/5/19.
 */
@Data
public class IssueIsNotResolvedResponse {
    private String id;

    private String issueTypeName;

    private String detail;

    private String issueTime;
}
