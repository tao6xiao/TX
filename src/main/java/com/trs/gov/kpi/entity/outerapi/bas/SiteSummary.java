package com.trs.gov.kpi.entity.outerapi.bas;

import lombok.Data;

/**
 * Created by ranwei on 2017/6/14.
 */
@Data
public class SiteSummary {

    private Integer mpId;// 网站ID

    private String displayName;// 网站显示名称

    private String mainUrl;// 网站网址

    private String strDay;//哪天的统计数据

    private Integer pv; // 该天的浏览量

    private Integer uv;// 该天的访客数

    private Integer nuv;//该天的新访客数

    private Integer bounceRate;//该天的跳出率

    private Double avgViews;// 该天的人均浏览页数

    private Integer avgDuration;// 该天的次均停留时长

    private Integer uv7;// 7天的访客数(该天前7天内，含该天)

    private Integer uv30;// 30天的访客数(该天前30天内，含该天)

    private Integer uvAll;// 该网站的总访客数

    private Integer avgViews7;// 7天的人均浏览页数

    private Integer avgDuration7;// 7天的次均停留时长

    private Integer avgDuration30;// 30天的次均停留时长
}
