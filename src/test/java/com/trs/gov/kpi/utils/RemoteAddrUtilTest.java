package com.trs.gov.kpi.utils;

import com.trs.gov.kpi.constant.ErrorType;
import com.trs.gov.kpi.constant.OperationType;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by tao.xiao on 2017/8/18.
 */
public class RemoteAddrUtilTest {

    @Test
    public void is_IPAddress(){
        String address = new String();
        assertFalse(is_IPAddress(address));
        address = "0.0.0.0";
        assertTrue(is_IPAddress(address));
        address = "10.0.0.0";
        assertTrue(is_IPAddress(address));
        address = "010.0.0.0";
        assertTrue(is_IPAddress(address));
        address = "255.255.255.255";
        assertTrue(is_IPAddress(address));
        address = "256.0.0.0";
        assertFalse(is_IPAddress(address));
        address = "-1.0.0.0";
        assertFalse(is_IPAddress(address));
        address = ".0.0.0";
        assertFalse(is_IPAddress(address));
        address = "a.0.0.0";
        assertFalse(is_IPAddress(address));
        assertFalse(is_IPAddress(null));
    }
    public static final boolean is_IPAddress(String address) {
        if (address != null) {
            String addr = address.trim();
            String pattern =
                    "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                            "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                            "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                            "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";
            return addr.matches(pattern);
        }
        return false;
    }
}