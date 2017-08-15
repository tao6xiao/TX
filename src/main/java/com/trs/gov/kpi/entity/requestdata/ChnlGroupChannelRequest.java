package com.trs.gov.kpi.entity.requestdata;

import com.trs.gov.kpi.entity.ChannelGroup;

/**
 * Created by he.lang on 2017/5/17.
 */
public class ChnlGroupChannelRequest extends ChannelGroup {
    @Override
    public String toString() {
        return "{" +
                "id=" + getId() +
                ", siteId=" + getSiteId() +
                ", groupId=" + getGroupId() +
                ", chnlId=" + getChnlId() +
                '}';
    }

}
