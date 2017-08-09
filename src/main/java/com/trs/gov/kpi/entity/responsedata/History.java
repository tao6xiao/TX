package com.trs.gov.kpi.entity.responsedata;

import lombok.Data;

import java.util.Date;
import java.util.List;

// TODO REVIEW LINWEI  注释说明一下该实体类干嘛用的， History这个名字不能很好的反映出来，需要修改
/**
 * Created by ranwei on 2017/6/9.
 */
@Data
public class History {

    private Date checkTime;

    private List data;

    public History(Date checkTime, List data){
        this.checkTime = checkTime;
        this.data = data;

    }
}
