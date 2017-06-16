package com.trs.gov.kpi.entity.responsedata;

import lombok.Data;

import java.util.Objects;

/**
 * Created by linwei on 2017/6/15.
 */
@Data
public class DepDocMultiCounterResponse extends DocMultiCounterResponse{

    // 部门
    private Long departmentId;

    // 部门名称
    private String departmentName;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        DepDocMultiCounterResponse that = (DepDocMultiCounterResponse) o;
        return Objects.equals(getDepartmentId(), that.getDepartmentId()) &&
                Objects.equals(getDepartmentName(), that.getDepartmentName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getDepartmentId(), getDepartmentName());
    }
}
