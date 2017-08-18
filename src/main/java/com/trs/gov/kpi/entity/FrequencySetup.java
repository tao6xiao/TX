package com.trs.gov.kpi.entity;

import com.trs.gov.kpi.annotation.DBField;
import com.trs.gov.kpi.annotation.DBTable;

import java.util.Date;

@DBTable("frequencysetup")
public class FrequencySetup {

    @DBField(autoInc = true)
    private Integer id;

    @DBField
    private Integer siteId;

    @DBField
    private Integer presetFeqId;

    @DBField
    private Integer chnlId;

    @DBField
    private Date setTime;

    @DBField
    private Byte isOpen;

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

    public Date getSetTime() {
        return setTime;
    }

    public void setSetTime(Date setTime) {
        this.setTime = setTime;
    }

    public Byte getIsOpen() {
        return isOpen;
    }

    public void setIsOpen(Byte isOpen) {
        this.isOpen = isOpen;
    }
}