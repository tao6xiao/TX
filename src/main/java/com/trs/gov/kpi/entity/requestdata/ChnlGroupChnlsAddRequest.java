package com.trs.gov.kpi.entity.requestdata;

import lombok.Data;

import java.util.Arrays;

/**
 * Created by he.lang on 2017/5/17.
 */
@Data
public class ChnlGroupChnlsAddRequest {
    private Integer siteId;
    private Integer groupId;
    private Integer[] chnlIds;

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("{");
        if(siteId != null){
            builder.append("siteId=");
            builder.append(siteId);
        }
        if(builder.length() > 1){
            builder.append(", ");
        }
        if(groupId != null){
            builder.append("groupId=");
            builder.append(groupId);
        }
        if(builder.length() > 1){
            builder.append(", ");
        }
        if(chnlIds != null){
            builder.append("chnlIds=");
            builder.append(Arrays.toString(chnlIds));
        }
        builder.append('}');
        return builder.toString();
    }
}
