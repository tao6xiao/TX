package com.trs.gov.kpi.entity.dao;

import com.trs.gov.kpi.utils.StringUtil;

import java.util.Collection;

/**
 * Created by linwei on 2017/5/22.
 */
public class CondDBField {
    private String fieldName;
    private Object condValue;
    private boolean isLike = false;
    private boolean isEqual = true;
    private boolean isBeginTime = false;
    private boolean isEndTime = false;
    private boolean isCollection = false;

    public CondDBField(String fieldName, Object condValue) {
        if (StringUtil.isEmpty(fieldName) || condValue == null) {
            throw new IllegalArgumentException("empty field or cond");
        }
        this.fieldName = fieldName;
        this.condValue = condValue;
        if (condValue instanceof Collection) {
            this.setCollection(true);
        }
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public Object getCondValue() {
        return condValue;
    }

    public void setCondValue(Object condValue) {
        this.condValue = condValue;
    }

    public boolean isLike() {
        return isLike;
    }

    public void setLike(boolean like) {
        if (like) {
            isLike = like;
            isEqual = false;
            isCollection = false;
        }
    }

    public boolean isEqual() {
        return isEqual;
    }

    public void setEqual(boolean equal) {
        if (isEqual) {
            isEqual = equal;
            isCollection = false;
            isLike = false;
        }
    }

    public boolean isBeginTime() {
        return isBeginTime;
    }

    public void setBeginTime(boolean beginTime) {
        if (beginTime) {
            isLike = false;
            isEqual = false;
            isCollection = false;
            isBeginTime = beginTime;
            isEndTime = false;
        }
    }

    public boolean isEndTime() {
        return isEndTime;
    }

    public void setEndTime(boolean endTime) {
        if (endTime) {
            isEndTime = endTime;
            isEqual = false;
        }
    }

    public boolean isCollection() {
        return isCollection;
    }

    public void setCollection(boolean collection) {
        if (collection) {
            isCollection = collection;
            isLike = false;
            isEqual = false;
        }
    }
}
