package com.trs.gov.kpi.entity.responsedata;

import com.trs.gov.kpi.entity.DutyDept;
import lombok.Getter;
import lombok.Setter;

/**
 * 栏目部门关系响应类
 * Created by he.lang on 2017/7/5.
 */

public class DutyDeptResponse extends DutyDept {
    @Getter
    @Setter
    private String chnlName;

    @Getter
    @Setter
    private String deptName;

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
