package com.trs.gov.kpi.entity.msg;

import lombok.Data;

/**
 * Created by li.hao on 2017/7/13.
 */
@Data
public class CheckEndMsg implements IMQMsg {

    public static final String MSG_TYPE = "CheckEndMsg";

    private int siteId;

    private int checkId;

    @Override
    public String getType() {
        return MSG_TYPE;
    }
}
