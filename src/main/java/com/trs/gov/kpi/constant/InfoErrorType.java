package com.trs.gov.kpi.constant;

/**
 * 信息错误的子类型
 */
public enum InfoErrorType {

    INVALID(-1, "不合法"),
    TYPOS(31,"错别字"),SENSITIVE_WORDS(32,"敏感词");

    public final int value;

    public final String name;

    private InfoErrorType(int type, String name) {
        this.value = type;
        this.name = name;
    }

    public static InfoErrorType getSubTypeIdByName(String name){
        for (InfoErrorType type : InfoErrorType.values()) {
            if(name.equals(type.name)){
                return type;
            }
        }
        return INVALID;
    }
}
