package com.trs.gov.kpi.utils;

import java.util.Collection;

/**
 * Created by linwei on 2017/5/25.
 */
public class CollectionUtil {

    private CollectionUtil(){

    }

    public static <T> String join(Collection<T>  collection, String delimiter) {
        if (collection == null || collection.isEmpty()) {
            return "";
        }

        StringBuilder joinedStr = new StringBuilder();
        for (T t : collection) {
            joinedStr.append(t);
            joinedStr.append(delimiter);
        }
        joinedStr.setLength(joinedStr.length()-delimiter.length());
        return joinedStr.toString();
    }
}
