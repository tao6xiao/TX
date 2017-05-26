package com.trs.gov.kpi.entity;

import lombok.Data;

import java.util.Date;

/**
 * Created by he.lang on 2017/5/26.
 */
@Data
public class InfoUpdateDao {

    private String id;
    private Integer siteId;
    private Integer subTypeId;
    private String chnlUrl;
    private Date checkTime;
    private Integer isResolved;
    private Integer isDel;
    private Integer chnlId;

}
