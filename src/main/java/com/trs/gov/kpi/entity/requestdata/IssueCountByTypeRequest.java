package com.trs.gov.kpi.entity.requestdata;

import lombok.Data;

/**
 * Created by yuanyuan on 2017/6/18.
 */
@Data
public class IssueCountByTypeRequest extends IssueCountRequest {

    // 网站可用性
    public static final int TYPE_SITE_AVALIABLE = 1;

    // 信息更新
    public static final int TYPE_INFO_UPDATE = 2;

    // 信息错误
    public static final int TYPE_INFO_ERROR = 3;

    private int typeId;

}
