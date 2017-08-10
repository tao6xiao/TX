package com.trs.gov.kpi.entity.requestdata;

import com.trs.gov.kpi.entity.ChannelGroup;

/**
 * Created by he.lang on 2017/5/17.
 */
public class ChnlGroupChannelRequest extends ChannelGroup {
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("{");
        if(getId() != null){
            builder.append("id=");
            builder.append(getId());
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
        if(getGroupId() != null){
            builder.append("groupId=");
            builder.append(getGroupId());
        }
        if(builder.length() > 1){
            builder.append(", ");
        }
        if(getChnlId() != null){
            builder.append("chnlId=");
            builder.append(getChnlId());
        }
        builder.append("}");
        return builder.toString();
    }
}
