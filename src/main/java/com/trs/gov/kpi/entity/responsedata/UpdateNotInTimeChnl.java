package com.trs.gov.kpi.entity.responsedata;

import lombok.Data;

/**
 * Created by he.lang on 2017/6/19.
 */
@Data
public class UpdateNotInTimeChnl extends EmptyChnl {
    private Double countMonth;

    @Override
    public boolean equals(Object obj) {
        if (!super.equals(obj)) {
            return true;
        }
        UpdateNotInTimeChnl notInTimeChnl = (UpdateNotInTimeChnl) obj;
        return countMonth.equals(notInTimeChnl.getCountMonth());
    }

    @Override
    public int hashCode() {
        return 0;
    }

}
