package com.trs.gov.kpi.utils;

/**
 * Created by HLoach on 2017/5/11.
 */
public class DataTypeConversion {

    //String数组转Integer数组
    public static Integer[] stringArrayToIntegerArray(String[] arrayForString){
        Integer[] arrayForInteger = new Integer[arrayForString.length];
        for (int i = 0; i < arrayForString.length; i++){
            Integer elementForInteger = Integer.parseInt(arrayForString[i]);
            arrayForInteger[i] = elementForInteger;
        }
        return arrayForInteger;
    }

    //Integer数组转String
    public static String integerArrayToString(Integer[] arrayForInteger){
        String str = null;
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < arrayForInteger.length; i++){
            String elementForString  = arrayForInteger[i].toString();
            buffer.append(elementForString);
        }
        str = buffer.toString();
        return str;
    }

}
