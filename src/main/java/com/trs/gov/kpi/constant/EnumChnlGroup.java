package com.trs.gov.kpi.constant;

import lombok.Getter;

/**
 * Created by he.lang on 2017/5/16.
 */
public enum EnumChnlGroup {
    COMMON(1,"公共类栏目"),
    CONSULTATION(2,"咨询类栏目"),
    INVESTIGATION(3,"调查征集"),
    HANDLE_GUIDE(4,"办事指南"),
    ONLINE_INTERVIEW(5,"在线访谈"),
    BUSINESS_KNOWLEDGE_BASE(6,"业务知识库");

    @Getter
    private int id;//栏目分类编号

    @Getter
    private String name;//分类名称

    private EnumChnlGroup(int id, String name){
        this.id = id;
        this.name = name;
    }

    public static EnumChnlGroup[] getChnlGroups(){
        return EnumChnlGroup.values();
    }

    public static EnumChnlGroup valueOf(int id){
        for (EnumChnlGroup enumChnlGroup : EnumChnlGroup.values()) {
            if(enumChnlGroup.getId() == id){
                return enumChnlGroup;
            }
        }
        return null;
    }
}
