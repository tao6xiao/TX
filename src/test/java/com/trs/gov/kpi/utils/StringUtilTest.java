package com.trs.gov.kpi.utils;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by he.lang on 2017/6/8.
 */
public class StringUtilTest {
    @Test
    public void stringToIntegerArray() throws Exception {
        String str = "1,2,3";
        Integer[] array = StringUtil.stringToIntegerArray(str);
        assertTrue(array != null && array.length > 0);

        str = "1";
        array = StringUtil.stringToIntegerArray(str);
        assertTrue(array[0] == 1);

        str = "1,";
        array = StringUtil.stringToIntegerArray(str);
        assertTrue(array[0] == 1);

        str = "1.";
        array = StringUtil.stringToIntegerArray(str);
        assertTrue(array[0] == 1);

    }

}