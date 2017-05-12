package com.trs.gov.kpi.utils;

import org.apache.tomcat.util.buf.StringUtils;

import java.util.Arrays;

/**
 * Created by HLoach on 2017/5/11.
 */
public class DataTypeConversion {

    //String数组转Integer数组
    public static Integer[] stringToIntegerArray(String str){
        String[] arrayForString = str.split(",");
        Integer[] arrayForInteger = null;
        if(arrayForString.length > 1) {
            arrayForInteger = new Integer[arrayForString.length];
            for (int i = 0; i < arrayForString.length; i++) {
                Integer elementForInteger = Integer.parseInt(arrayForString[i]);
                arrayForInteger[i] = elementForInteger;
            }
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
            if(i+1 != arrayForInteger.length){
                buffer.append(",");
            }
        }
        if(buffer.length() > 0) {
            str = buffer.toString();
        }
        return str;
    }

}
