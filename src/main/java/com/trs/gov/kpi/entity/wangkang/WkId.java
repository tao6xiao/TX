package com.trs.gov.kpi.entity.wangkang;

import lombok.Data;

/**
 * Created by linwei on 2017/7/13.
 */

@Data
public class WkId {

    // 编号
    private int id;

    // 类型
    private String type;

    /**
     * 创建一个新的监测id
     *
     * @return
     */
    public static WkId createCheckId() {
        final WkId wkId = new WkId();
        wkId.setType("checkId");
        return wkId;
    }
}
