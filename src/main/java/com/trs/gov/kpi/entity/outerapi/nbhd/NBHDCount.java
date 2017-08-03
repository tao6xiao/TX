package com.trs.gov.kpi.entity.outerapi.nbhd;

import lombok.Data;

/**
 * Created by ranwei on 2017/6/9.
 */
@Data
public class NBHDCount {

    private int overTimeCount;// 回复不及时(过期)信件数量

    private int commonCount;// 未过期信件数量
}
