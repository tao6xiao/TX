package com.trs.gov.kpi.entity.responsedata;

import lombok.Data;

/**
 * Created by he.lang on 2017/5/16.
 */
@Data
public class FrequencySetupResponse {
    private Integer id;

    private Integer presetFeqId;

    private Integer updateFreq;

    private Integer alertFreq;

    private Integer chnlId;

    private String chnlName;
}
