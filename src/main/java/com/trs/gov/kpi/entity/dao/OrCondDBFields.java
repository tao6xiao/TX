package com.trs.gov.kpi.entity.dao;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by linwei on 2017/5/22.
 */
public class OrCondDBFields {

    private List<CondDBField> fields;

    public void addCondDBField(CondDBField field) {
        if (field == null) {
            return;
        }

        if (fields == null) {
            fields = new ArrayList<>();
        }
        fields.add(field);
    }

    public List<CondDBField> getFields() {
        return fields;
    }

    public void setFields(List<CondDBField> fields) {
        this.fields = fields;
    }
}
