package com.trs.gov.kpi.utils;

/**
 * Created by linwei on 2017/5/18.
 */
public class StringUtil {

    private StringUtil() {

    }

    public static boolean isEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    //String数组转Integer数组
    public static Integer[] stringToIntegerArray(String str) {
        String[] arrayForString = str.split(",");
        Integer[] arrayForInteger = null;
        if (arrayForString.length > 1) {
            arrayForInteger = new Integer[arrayForString.length];
            for (int i = 0; i < arrayForString.length; i++) {
                Integer elementForInteger = Integer.parseInt(arrayForString[i]);
                arrayForInteger[i] = elementForInteger;
            }
        } else {
            arrayForInteger = new Integer[1];
            Integer elementForInteger = Integer.parseInt(arrayForString[0]);
            arrayForInteger[0] = elementForInteger;
        }
        return arrayForInteger;
    }


}
