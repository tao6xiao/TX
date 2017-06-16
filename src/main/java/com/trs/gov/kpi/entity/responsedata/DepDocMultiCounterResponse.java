package com.trs.gov.kpi.entity.responsedata;

import lombok.Getter;

import java.util.Objects;

/**
 * Created by linwei on 2017/6/15.
 */
public class DepDocMultiCounterResponse extends DocMultiCounterResponse{

    // 部门
    @Getter
    private Long departmentId;

    // 部门名称
    @Getter
    private String departmentName;

    public void setDepartmentId(Long id) {
        this.departmentId = id;
        // TODO wait for editcenter
        this.departmentName = String.valueOf(id);
    }

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
