package com.trs.gov.kpi.entity.requestdata;

import lombok.Data;

/**
 * Created by he.lang on 2017/5/17.
 */
@Data
public class ChnlGroupChnlsAddRequestDetail {
    private Integer siteId;
    private Integer groupId;
    private Integer[] chnlIds;
}
