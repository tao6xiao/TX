package com.trs.gov.kpi.entity.dao;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据更新
 * Created by linwei on 2017/6/9.
 */
public class DBUpdater {

    @Getter
    private String tableName;

    @Getter
    private List<DBField> fields;

    public DBUpdater(String tableName) {
        this.tableName = tableName;
    }

    public void addField(DBField field) {
        if (field == null) {
            return;
        }

        if (fields == null) {
            fields = new ArrayList<>();
        }

        fields.add(field);
    }

    public void addField(String filedName, Object value) {
        this.addField(new DBField(filedName, value));
    }

}
