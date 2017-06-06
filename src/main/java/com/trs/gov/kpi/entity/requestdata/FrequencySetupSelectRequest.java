package com.trs.gov.kpi.entity.requestdata;

import lombok.Data;

/**
 * Created by he.lang on 2017/6/6.
 */
public class FrequencySetupSelectRequest {
    private Integer siteId;

    private Integer pageIndex;

    private Integer pageSize;

    private String searchField = "";

    private String searchText = "";

    public void setSiteId(Integer siteId) {
        this.siteId = siteId;
    }

    public void setPageIndex(Integer pageIndex) {
        this.pageIndex = pageIndex;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getSiteId() {
        return siteId;
    }

    public Integer getPageIndex() {
        return pageIndex;
    }

    public Integer getPageSize() {
        return pageSize;
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
