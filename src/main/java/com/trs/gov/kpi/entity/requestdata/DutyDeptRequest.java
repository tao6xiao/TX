package com.trs.gov.kpi.entity.requestdata;

import com.trs.gov.kpi.entity.DutyDept;

/**
 * Created by he.lang on 2017/7/5.
 */
public class DutyDeptRequest extends DutyDept{

    @Override
    public String toString() {
        return "{" +
                "chnlId=" + getChnlId() +
                ", siteId=" + getSiteId() +
                ", deptId=" + getDeptId() +
                ", contain=" + getContain() +
                '}';
    }
}
