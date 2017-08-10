package com.trs.gov.kpi.entity.requestdata;

import com.trs.gov.kpi.entity.DutyDept;

/**
 * Created by he.lang on 2017/7/5.
 */
public class DutyDeptRequest extends DutyDept{

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("{");
        if(getChnlId() != null){
            builder.append("chnlId=");
            builder.append(getChnlId());
        }
        if(builder.length() > 1){
            builder.append(", ");
        }
        if(getSiteId() != null){
            builder.append("siteId=");
            builder.append(getSiteId());
        }
        if(builder.length() > 1){
            builder.append(", ");
        }
        if(getDeptId() != null){
            builder.append("deptId=");
            builder.append(getDeptId());
        }
        if(builder.length() > 1){
            builder.append(", ");
        }
        if(getContain() != null){
            builder.append("contain=");
            builder.append(getContain());
        }
        if(builder.toString().endsWith(", ")){
            builder.deleteCharAt(builder.lastIndexOf(", "));
        }
        builder.append("}");
        return builder.toString();
    }
}
