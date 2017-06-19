package com.trs.gov.kpi.entity.responsedata;

import lombok.Data;

import java.util.List;

/**
 * Created by he.lang on 2017/6/19.
 */
@Data
public class MonthUpdateResponse {
    private List<EmptyChnl> emptyChnl;

    private List<UpdateNotInTimeChnl> updateNotInTimeChnl;

}
