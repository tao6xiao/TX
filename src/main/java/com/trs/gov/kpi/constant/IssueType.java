package com.trs.gov.kpi.constant;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by wangxuan on 2017/5/15.
 */
public enum IssueType {

    AVAILABLE_ISSUE(1, "可用性问题"),
    UPDATE_ISSUE(2, "信息更新问题"),
    INFO_ISSUE(3, "信息错误"),
    INFO_UPDATE_WARNING(51,"信息更新预警"),
    RESPOND_WARNING(52, "互动回应预警");

    @Getter
    private int code;

    @Getter
    private String name;

    private IssueType(int code, String name) {

        this.code = code;
        this.name = name;
    }

    public static IssueType getIssueTypeByName(String name) {

        for(IssueType issueType: IssueType.values()) {

            if(StringUtils.equals(issueType.getName(), name)) {

                return issueType;
            }
        }
        return null;
    }
}
