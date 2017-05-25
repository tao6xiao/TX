package com.trs.gov.kpi.entity;

import com.trs.gov.kpi.utils.StringUtil;
import lombok.Data;

import java.util.List;

/**
 * Created by rw103 on 2017/5/13.
 */
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

    private Integer isResolved = 0;

    private Integer isDel = 0;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getSiteId() {
        return siteId;
    }

    public void setSiteId(Integer siteId) {
        this.siteId = siteId;
    }

    public String getBeginDateTime() {
        return beginDateTime;
    }

    public void setBeginDateTime(String beginDateTime) {
        if (!StringUtil.isEmpty(beginDateTime)) {
            this.beginDateTime = beginDateTime;
        }
    }

    public String getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(String endDateTime) {
        if (!StringUtil.isEmpty(endDateTime)) {
            this.endDateTime = endDateTime;
        }
    }

    public String getSearchField() {
        return searchField;
    }

    public void setSearchField(String searchField) {
        if (!StringUtil.isEmpty(searchField)) {
            this.searchField = searchField;
        }
    }

    public String getSearchText() {
        return searchText;
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }

    public List<Integer> getIds() {
        return ids;
    }

    public void setIds(List<Integer> ids) {
        this.ids = ids;
    }

    public Integer getGranularity() {
        return granularity;
    }

    public void setGranularity(Integer granularity) {
        this.granularity = granularity;
    }

    public Integer getIsResolved() {
        return isResolved;
    }

    public void setIsResolved(Integer isResolved) {
        this.isResolved = isResolved;
    }

    public Integer getIsDel() {
        return isDel;
    }

    public void setIsDel(Integer isDel) {
        this.isDel = isDel;
    }

}
