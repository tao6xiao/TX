package com.trs.gov.kpi.entity.responsedata;

import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.gov.kpi.service.outer.DeptApiService;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * Created by linwei on 2017/6/15.
 */
@Component
@Scope("prototype")
@Slf4j
public class DepDocMultiCounterResponse extends DocMultiCounterResponse{

    // 部门
    @Getter
    private Long departmentId;

    // 部门名称
    @Getter
    private String departmentName;

    @Resource
    DeptApiService deptApiService;

    public void setDepartmentId(Long id) {
        this.departmentId = id;
        try {
            if(deptApiService.findDeptById("", Math.toIntExact(id)) != null) {
                this.departmentName = deptApiService.findDeptById("", Math.toIntExact(id)).getGName();
            }
        } catch (RemoteException e) {
            log.error("调用外部接口 findDeptById 失败",e);
        }
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
