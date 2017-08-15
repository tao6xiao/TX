package com.trs.gov.kpi.entity.requestdata;

import com.trs.gov.kpi.entity.FrequencySetup;

/**
 * Created by he.lang on 2017/5/16.
 */
public class FrequencySetupUpdateRequest extends FrequencySetup {

    @Override
    public String toString() {
        return "{" +
                "id=" + getId() +
                ", siteId=" + getSiteId() +
                ", presetFeqId=" + getPresetFeqId() +
                ", chnlId=" + getChnlId() +
                ", setTime=" + getSetTime() +
                ", isOpen=" + getIsOpen() +
                '}';
    }

}
