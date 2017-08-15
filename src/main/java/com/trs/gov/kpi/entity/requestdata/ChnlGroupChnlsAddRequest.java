package com.trs.gov.kpi.entity.requestdata;

import lombok.Data;

import java.util.Arrays;

/**
 * Created by he.lang on 2017/5/17.
 */
@Data
public class ChnlGroupChnlsAddRequest {
    private Integer siteId;
    private Integer groupId;
    private Integer[] chnlIds;

    @Override
    public String toString() {
        return "{" +
                "siteId=" + siteId +
                ", groupId=" + groupId +
                ", chnlIds=" + Arrays.toString(chnlIds) +
                '}';
    }
}
