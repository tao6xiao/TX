package com.trs.gov.kpi.entity.outerapi.nbhd;

import lombok.Data;

/**
 * Created by ranwei on 2017/6/9.
 */
@Data
public class NBHDMessage {
    private long crTime; //咨询时间
    private String status; //信件流转状态
    private String queryNumber; //查询编号
    private int overtimeDays; //超期天数
    private int isDeadline; //是否超期
    private int metadataId; //信件id
    private String title;//信件标题
}
