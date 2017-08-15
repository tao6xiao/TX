package com.trs.gov.kpi.entity.responsedata;

import com.trs.gov.kpi.constant.ErrorType;
import com.trs.gov.kpi.constant.OperationType;
import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.gov.kpi.service.outer.DeptApiService;
import com.trs.gov.kpi.utils.LogUtil;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Created by linwei on 2017/6/15.
 */
@Component
@Scope("prototype")
@Slf4j
public class DepDocMultiCounterResponse extends DocMultiCounterResponse {

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
            if (deptApiService.findDeptById("", Math.toIntExact(id)) != null) {
                this.departmentName = deptApiService.findDeptById("", Math.toIntExact(id)).getGName();
            }
        } catch (RemoteException e) {
            log.error("调用外部接口 findDeptById 失败", e);
            LogUtil.addErrorLog(OperationType.REMOTE, ErrorType.REMOTE_FAILED, "调用外部接口 findDeptById 失败", e);
        }
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
