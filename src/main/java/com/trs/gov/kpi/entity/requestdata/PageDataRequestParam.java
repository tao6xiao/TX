package com.trs.gov.kpi.entity.requestdata;

import javax.validation.constraints.NotNull;

/**
 * Created by linwei on 2017/5/22.
 */
public class PageDataRequestParam {

    /**
     * 问题编号
     */
    private String id;

    /**
     * 查询历史记录的粒度，1-->天  2-->周  3-->月  4-->年
     */
    public Integer granularity;

    @NotNull
    private Integer siteId;

    private String beginDateTime;

    private String endDateTime;

    private String searchField;

    private String searchText;

    private String sortFields;

    private Integer pageSize;

    private Integer pageIndex;

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
        if (beginDateTime != null && beginDateTime.trim().isEmpty()) {
            return;
        }
        this.beginDateTime = beginDateTime;
    }

    public String getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(String endDateTime) {
        if (endDateTime != null && endDateTime.trim().isEmpty()) {
            return;
        }
        this.endDateTime = endDateTime;
    }

    public String getSearchField() {
        return searchField;
    }

    public void setSearchField(String searchField) {
        if (searchField != null && searchField.trim().isEmpty()) {
            return;
        }
        this.searchField = searchField;
    }

    public String getSearchText() {
        return searchText;
    }

    public void setSearchText(String searchText) {
        if (searchText != null && searchText.trim().isEmpty()) {
            return;
        }
        this.searchText = searchText;
    }

    public String getSortFields() {
        return sortFields;
    }

    public void setSortFields(String sortFields) {
        if (sortFields != null && sortFields.trim().isEmpty()) {
            return;
        }
        this.sortFields = sortFields;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(Integer pageIndex) {
        this.pageIndex = pageIndex;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getGranularity() {
        return granularity;
    }

    public void setGranularity(Integer granularity) {
        this.granularity = granularity;
    }
}
