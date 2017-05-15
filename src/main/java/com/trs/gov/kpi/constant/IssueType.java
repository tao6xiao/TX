package com.trs.gov.kpi.constant;

import lombok.Getter;

/**
 * Created by wangxuan on 2017/5/15.
 */
public enum IssueType {

    AVAILABLE_ISSUE(1),
    UPDATE_ISSUE(2),
    INFO_ISSUE(3);

    @Getter
    private int code;

    private IssueType(int code) {

        this.code = code;
    }
}
