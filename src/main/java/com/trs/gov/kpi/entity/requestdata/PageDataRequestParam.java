package com.trs.gov.kpi.entity.requestdata;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * Created by linwei on 2017/5/22.
 */
@Data
public class PageDataRequestParam extends DateRequest {

    /**
     * 问题编号
     */
    private String id;

    @NotNull
    private Integer siteId;

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("{");
        if(id != null){
            builder.append("id=");
            builder.append(id);
        }
        if(builder.length() > 1){
            builder.append(", ");
        }
        if(siteId != null){
            builder.append("siteId=");
            builder.append(siteId);
        }
        if(builder.length() > 1){
            builder.append(", ");
        }
        if(getBeginDateTime() != null){
            builder.append("beginDateTime=");
            builder.append(getBeginDateTime());
        }
        if(builder.length() > 1){
            builder.append(", ");
        }
        if(getEndDateTime() != null){
            builder.append("endDateTime=");
            builder.append(getEndDateTime());
        }
        if(builder.length() > 1){
            builder.append(", ");
        }
        if(getPageSize() != null){
            builder.append("pageSize=");
            builder.append(getPageSize());
        }
        if(builder.length() > 1){
            builder.append(", ");
        }
        if(getPageIndex() != null){
            builder.append("pageIndex=");
            builder.append(getPageIndex());
        }
        if(builder.length() > 1){
            builder.append(", ");
        }
        if(getSortFields() != null){
            builder.append("sortFields=");
            builder.append(getSortFields());
        }
        if(builder.length() > 1){
            builder.append(", ");
        }
        if(getGranularity() != null){
            builder.append("granularity=");
            builder.append(getGranularity());
        }
        if(builder.length() > 1){
            builder.append(", ");
        }
        if(getSearchField() != null){
            builder.append("searchField=");
            builder.append(getSortFields());
        }
        if(builder.length() > 1){
            builder.append(", ");
        }
        if(getSearchText() != null){
            builder.append("searchText=");
            builder.append(getSearchText());
        }
        if(builder.toString().endsWith(", ")){
            builder.deleteCharAt(builder.lastIndexOf(", "));
        }
        builder.append("}");
        return builder.toString();
    }
}
