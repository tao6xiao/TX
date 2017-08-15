package com.trs.gov.kpi.entity;

public class ChannelGroup {
    private Integer id;

    private Integer siteId;

    private Integer groupId;

    private Integer chnlId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSiteId() {
        return siteId;
    }

    public void setSiteId(Integer siteId) {
        this.siteId = siteId;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public Integer getChnlId() {
        return chnlId;
    }

    public void setChnlId(Integer chnlId) {
        this.chnlId = chnlId;
    }

    @Override
    public String toString() {
        return "ChannelGroup{" +
                "id=" + id +
                ", siteId=" + siteId +
                ", groupId=" + groupId +
                ", chnlId=" + chnlId +
                '}';
    }
}