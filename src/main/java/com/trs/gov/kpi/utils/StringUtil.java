package com.trs.gov.kpi.utils;

import com.trs.gov.kpi.constant.ErrorType;
import com.trs.gov.kpi.constant.OperationType;
import lombok.extern.slf4j.Slf4j;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by linwei on 2017/5/18.
 */
@Slf4j
public class StringUtil {

    private StringUtil() {

    }

    public static boolean isEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    //String数组转Integer数组
    public static Integer[] stringToIntegerArray(String str) {
        if (str == null) {
            return new Integer[0];
        }
        String[] arrayForString = str.split(",");
        Integer[] arrayForInteger;
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

    /**
     * 处理接受参数中的特殊字符
     *
     * @param str
     * @return
     */
    public static String escape(String str) {
        if (isEmpty(str)) {
            return str;
        }
        return str.replaceAll("((?=[\\x21-\\x7e]+)[^A-Za-z0-9])", "\\\\$1");
    }

    /**
     * 处理请求url中的特殊字符
     *
     * @param value
     * @return
     */
    public static String encodeUrlParam(String value) {
        try {
            return URLEncoder.encode(value, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            log.error("", e);
            LogUtil.addErrorLog(OperationType.REQUEST, ErrorType.REQUEST_FAILED, "处理url中特殊字符，url=" + value, e);
            return value;
        }
    }


}
