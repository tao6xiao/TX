package com.trs.gov.kpi.entity.dao;

import com.trs.gov.kpi.utils.StringUtil;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by linwei on 2017/5/22.
 */
public class OrCondDBFields {

    @Getter
    private List<CondDBField> fields;

    /**
     * 添加一个条件字段
     *
     * @param fieldName
     * @param value
     * @return
     */
    public CondDBField addCond(String fieldName, Object value) {
        if (StringUtil.isEmpty(fieldName) || value == null) {
            return null;
        }
        return addCond(new CondDBField(fieldName, value));
    }

    /**
     * 添加一个条件字段
     * @param field
     * @return
     */
    public CondDBField addCond(CondDBField field) {
        if (field == null) {
            return null;
        }

        if (fields == null) {
            fields = new ArrayList<>();
        }
        fields.add(field);
        return field;
    }

}
