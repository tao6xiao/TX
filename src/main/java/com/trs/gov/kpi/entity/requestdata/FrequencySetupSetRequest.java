package com.trs.gov.kpi.entity.requestdata;

import lombok.Data;

/**
 * Created by he.lang on 2017/5/16.
 */
@Data
public class FrequencySetupSetRequest {

    private Integer siteId;

    private Integer presetFeqId;

    private Integer[] chnlIds;
}
