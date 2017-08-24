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
        assertFalse(RemoteAddrUtil.isIPAddress(address));
        address = "0.0.0.0";
        assertTrue(RemoteAddrUtil.isIPAddress(address));
        address = "10.0.0.0";
        assertTrue(RemoteAddrUtil.isIPAddress(address));
        address = "010.0.0.0";
        assertTrue(RemoteAddrUtil.isIPAddress(address));
        address = "255.255.255.255";
        assertTrue(RemoteAddrUtil.isIPAddress(address));
        address = "256.0.0.0";
        assertFalse(RemoteAddrUtil.isIPAddress(address));
        address = "-1.0.0.0";
        assertFalse(RemoteAddrUtil.isIPAddress(address));
        address = ".0.0.0";
        assertFalse(RemoteAddrUtil.isIPAddress(address));
        address = "a.0.0.0";
        assertFalse(RemoteAddrUtil.isIPAddress(address));
        assertFalse(RemoteAddrUtil.isIPAddress(null));
    }
}