package com.trs.gov.kpi.entity;

import lombok.Data;

import java.util.List;

/**
 * Created by rw103 on 2017/5/13.
 */
@Data
public class IssueBase {

    /**
     * 问题编号
     */
    private String id;

    /**
     * 站点编号
     */
    private Integer siteId;

    /**
     * 问题记录起止时间
     */
    public String beginDateTime;
    public String endDateTime;

    /**
     * 用于模糊查询
     * searchField-->按哪个字段查询  searchText-->查询的关键字
     */
    public String searchField;
    public String searchText;

    /**
     * 用于存放根据字段检索时，typeName转换为的typeId集合
     */
    public List<Integer> ids;

    /**
     * 查询历史记录的粒度，1-->天  2-->周  3-->月  4-->年
     */
    public Integer granularity;
}
