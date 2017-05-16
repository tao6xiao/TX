package com.trs.gov.kpi.entity;

public class FrequencySetup {
    private Integer id;

    private Integer siteId;

    private Integer presetFeqId;

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

    public Integer getPresetFeqId() {
        return presetFeqId;
    }

    public void setPresetFeqId(Integer presetFeqId) {
        this.presetFeqId = presetFeqId;
    }

    public Integer getChnlId() {
        return chnlId;
    }

    public void setChnlId(Integer chnlId) {
        this.chnlId = chnlId;
    }
}