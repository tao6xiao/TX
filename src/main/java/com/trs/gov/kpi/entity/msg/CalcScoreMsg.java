package com.trs.gov.kpi.entity.msg;

import lombok.Data;

/**
 * Created by linwei on 2017/7/15.
 */
@Data
public class CalcScoreMsg implements IMQMsg {

    public static final String  MSG_TYPE = "CalcScoreMsg";

    @Override
    public String getType() {
        return MSG_TYPE;
    }

    private Integer siteId;

    private Integer checkId;

    private String scoreType;

}
