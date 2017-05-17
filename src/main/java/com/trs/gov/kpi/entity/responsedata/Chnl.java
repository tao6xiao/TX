package com.trs.gov.kpi.entity.responsedata;

import lombok.Data;

/**
 * 用于栏目分类中的对应栏目的所对应记录的对象
 * Created by he.lang on 2017/5/17.
 */
@Data
public class Chnl {
    private Integer channelId;
    private String chnlDesc;
    private String chnlName;
    private String crUser;
    private String crTime;
    private String hasChildren;

}
