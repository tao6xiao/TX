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
        try {
            array = StringUtil.stringToIntegerArray(str);
            assertTrue(false);
        } catch (NumberFormatException e) {
            assertTrue(true);
        }
    }

    @Test
    public void escape() throws Exception {
        String str = "123@#$";
        assertEquals("123\\@\\#\\$",StringUtil.escape(str));
    }

    @Test
    public void escape_null() throws Exception {
        String str = null;
        assertEquals(null,StringUtil.escape(str));
    }

    @Test
    public void encodeUrlParam() throws Exception {
        String str = "2017-06-06 12:00:00";
        assertEquals("2017-06-06+12%3A00%3A00",StringUtil.encodeUrlParam(str));
    }

}