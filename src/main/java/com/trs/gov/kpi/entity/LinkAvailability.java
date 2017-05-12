package com.trs.gov.kpi.entity;

import lombok.Data;

import java.util.Date;

/**
 * Created by rw103 on 2017/5/11.
 */
@Data
public class LinkAvailability {

    private Integer id;

    private Integer siteId;

    private Integer issueTypeId;

    private String issueTypeName;

    private String invalidLink;

    private String snapshot;

    private Date checkTime;

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", issueTypeName='" + issueTypeName + '\'' +
                ", invalidLink='" + invalidLink + '\'' +
                ", snapshot='" + snapshot + '\'' +
                ", checkTime=" + checkTime +
                '}';
    }
}
