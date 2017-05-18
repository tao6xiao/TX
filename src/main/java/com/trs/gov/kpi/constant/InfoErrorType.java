package com.trs.gov.kpi.constant;

/**
 * 信息错误的子类型
 */
public enum InfoErrorType {

    TYPOS(1,"错别字"),SENSITIVE_WORDS(2,"敏感词");

    public final int value;

    public final String name;

    InfoErrorType(int type, String name) {
        this.value = type;
        this.name = name;
    }
}
