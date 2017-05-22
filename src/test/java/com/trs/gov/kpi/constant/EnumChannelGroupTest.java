package com.trs.gov.kpi.constant;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by linwei on 2017/5/22.
 */
public class EnumChannelGroupTest {
    @Test
    public void getChnlGroups() throws Exception {
        EnumChannelGroup[] groups = EnumChannelGroup.getChnlGroups();
        assertEquals(EnumChannelGroup.values().length-1, groups.length);

        boolean hasInvalidGroup = false;
        for (EnumChannelGroup group: groups) {
            if (group.getId() == EnumChannelGroup.INVALID.getId()) {
                hasInvalidGroup = true;
            }
        }
        assertEquals(false, hasInvalidGroup);
    }

}