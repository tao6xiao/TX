package com.trs.gov.kpi.entity;

import lombok.Data;

import java.util.Date;

/**
 * Created by rw103 on 2017/5/13.
 */
@Data
public class InfoUpdate extends IssueBase{

    private String id;

    private Integer siteId;

    private String chnlName;

    private Integer chnlId;

    private String chnlUrl;

    private String issueTypeName;

    private Date checkTime;

}
