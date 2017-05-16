package com.trs.gov.kpi.constant;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by wangxuan on 2017/5/15.
 */
public enum DocumentErrorType {

    WORD(1, "错别字", "字词"),
    SENSITIVE(2, "敏感信息", "敏感词");

    @Getter
    private int code;

    @Getter
    private String name;

    @Getter
    private String keyWord;

    private DocumentErrorType(int code, String name, String keyWord) {

        this.code = code;
        this.name = name;
        this.keyWord = keyWord;
    }

    public static DocumentErrorType getTypeByCode(int code) {

        for(DocumentErrorType documentErrorType: DocumentErrorType.values()) {

            if(documentErrorType.getCode() == code) {

                return documentErrorType;
            }
        }
        return null;
    }

    public static DocumentErrorType getTypeByName(String name) {

        for(DocumentErrorType documentErrorType: DocumentErrorType.values()) {

            if(StringUtils.equals(documentErrorType.getName(), name)) {

                return documentErrorType;
            }
        }
        return null;
    }

    public static DocumentErrorType getTypeByKey(String key) {

        for(DocumentErrorType documentErrorType: DocumentErrorType.values()) {

            if(StringUtils.equals(documentErrorType.keyWord, key)) {

                return documentErrorType;
            }
        }
        return null;
    }
}
