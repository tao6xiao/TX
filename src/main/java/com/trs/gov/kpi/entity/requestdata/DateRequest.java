package com.trs.gov.kpi.entity.requestdata;

/**
 * Created by linwei on 2017/6/12.
 */
public class DateRequest {

    private String beginDateTime;

    private String endDateTime;

    private Integer pageSize;

    private Integer pageIndex;

    private String sortFields;

    /**
     * 查询历史记录的粒度，1-->天  2-->周  3-->月  4-->年
     */
    private Integer granularity;

    /**
     * 搜索的字段
     */
    private String searchField;

    /**
     * 搜索的关键字
     */
    private String searchText;

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

    public String getSortFields() {
        return sortFields;
    }

    public void setSortFields(String sortFields) {
        if (sortFields != null && sortFields.trim().isEmpty()) {
            return;
        }
        this.sortFields = sortFields;
    }

    public Integer getGranularity() {
        return granularity;
    }

    public void setGranularity(Integer granularity) {
        this.granularity = granularity;
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
}
