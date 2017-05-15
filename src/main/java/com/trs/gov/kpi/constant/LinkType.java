package com.trs.gov.kpi.constant;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by wangxuan on 2017/5/15.
 */
public enum LinkType {

    PAGE(1, "链接失效"),
    IMAGE(2, "图片失效"),
    FILE(3, "附件失效");

    @Getter
    private int code;
    @Getter
    private String name;

    private LinkType(int code, String name) {

        this.code = code;
        this.name = name;
    }

    public static LinkType getTypeByCode(int code) {

        for(LinkType linkType: LinkType.values()) {

            if(linkType.getCode() == code) {

                return linkType;
            }
        }
        return null;
    }

    public static LinkType getTypeByName(String name) {

        for(LinkType linkType: LinkType.values()) {

            if(StringUtils.equals(linkType.getName(), name)) {

                return linkType;
            }
        }
        return null;
    }
}
