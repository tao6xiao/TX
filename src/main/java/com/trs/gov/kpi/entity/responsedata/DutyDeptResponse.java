package com.trs.gov.kpi.entity.responsedata;

import com.trs.gov.kpi.entity.DutyDept;
import lombok.Data;

/**
 * 栏目部门关系响应类
 * Created by he.lang on 2017/7/5.
 */
@Data
public class DutyDeptResponse extends DutyDept{
    private String chnlName;

    private String deptName;
}
