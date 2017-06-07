package com.trs.gov.kpi.utils;

import com.trs.gov.kpi.annotation.DBField;
import com.trs.gov.kpi.annotation.DBTable;
import com.trs.gov.kpi.entity.dao.DBRow;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;

/**
 * Created by linwei on 2017/6/7.
 */
@Slf4j
public class DBUtil {

    private DBUtil(){

    }

    /**
     * 转换数据库po为dbRow
     * @param po
     * @return
     */
    public static DBRow toRow(Object po) {
        Class<?> clazz = po.getClass();
        if (!clazz.isAnnotationPresent(DBTable.class)) {
            return null;
        }

        DBRow row = new DBRow();
        Field[] fields = clazz.getDeclaredFields();
        for(Field field :fields){
            if (!field.isAnnotationPresent(DBField.class)) {
                continue;
            }


            DBField dbField = field.getAnnotation(DBField.class);
            if (dbField.autoInc()) {
                continue;
            }
            // 获取db字段名
            String fieldName;
            if (StringUtil.isEmpty(dbField.value())) {
                // 直接使用field的名字作为字段名
                fieldName = field.getName();
            } else {
                fieldName = dbField.value();
            }

            // 获取值
            try {
                field.setAccessible(true);
                Object value = field.get(po);
                if (value != null) {
                    row.addField(fieldName, value);
                }
            } catch (IllegalAccessException e) {
                log.error("", e);
            }
        }
        return row;
    }

}
