package com.trs.gov.kpi.entity.dao;

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
        isBeginTime = beginTime;
    }

    public boolean isEndTime() {
        return isEndTime;
    }

    public void setEndTime(boolean endTime) {
        if (endTime) {
            isEndTime = endTime;
            isBeginTime = false;
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
