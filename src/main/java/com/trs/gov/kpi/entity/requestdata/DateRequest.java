package com.trs.gov.kpi.entity.requestdata;

import com.trs.gov.kpi.constant.ErrorType;
import com.trs.gov.kpi.constant.Granularity;
import com.trs.gov.kpi.constant.OperationType;
import com.trs.gov.kpi.utils.DateUtil;
import com.trs.gov.kpi.utils.LogUtil;
import com.trs.gov.kpi.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by linwei on 2017/6/12.
 */
@Slf4j
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

    /**
     * 设置默认起止时间
     */
    public void setDefaultDate() {
        if (StringUtil.isEmpty(getBeginDateTime())) {
            Date endDate = getEndDate();
            setEndDateTime(DateUtil.toString(endDate));
            String beginTime;
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(endDate);

            if (Granularity.DAY.equals(getGranularity())) {
                calendar.set(Calendar.DAY_OF_MONTH, 1);
                beginTime = DateUtil.toString(calendar.getTime());
            } else if (Granularity.WEEK.equals(getGranularity())) {
                calendar.add(Calendar.WEEK_OF_YEAR, -11);
                calendar.set(Calendar.DAY_OF_WEEK, 1);
                beginTime = DateUtil.toString(calendar.getTime());
            } else if (Granularity.YEAR.equals(getGranularity())) {
                calendar.add(Calendar.YEAR, -5);
                calendar.set(Calendar.MONTH, 0);
                calendar.set(Calendar.DAY_OF_MONTH, 1);
                beginTime = DateUtil.toString(calendar.getTime());
            } else {//不设置，默认为月
                calendar.set(Calendar.MONTH, 0);
                calendar.set(Calendar.DAY_OF_MONTH, 1);
                beginTime = DateUtil.toString(calendar.getTime());
            }
            setBeginDateTime(beginTime);
        }
    }

    private Date getEndDate() {
        if (StringUtil.isEmpty(getEndDateTime())) {
            return new Date();
        } else {
            try {
                return DateUtil.toDate(getEndDateTime());
            } catch (ParseException e) {
                String errorInfo = "时间不合法：" + getEndDateTime();
                log.error(errorInfo, e);
                LogUtil.addErrorLog(OperationType.REQUEST, ErrorType.BIZ_EXCEPTION, errorInfo, e);
            }
            return new Date();
        }
    }
}
