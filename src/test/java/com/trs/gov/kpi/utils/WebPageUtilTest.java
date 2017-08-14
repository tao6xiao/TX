package com.trs.gov.kpi.utils;

import com.trs.gov.kpi.constant.EnumUrlType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by ranwei on 2017/8/14.
 */
public class WebPageUtilTest {

    @Test
    public void getUrlType_resSuffixs() throws Exception {
        assertEquals(EnumUrlType.RES, WebPageUtil.getUrlType(".css"));
    }
    @Test
    public void getUrlType_videoSuffixs() throws Exception {
        assertEquals(EnumUrlType.VIDEO, WebPageUtil.getUrlType(".mp4"));
    }
    @Test
    public void getUrlType_imageSuffixs() throws Exception {
        assertEquals(EnumUrlType.IMAGE, WebPageUtil.getUrlType(".png"));
    }
    @Test
    public void getUrlType_fileSuffixs() throws Exception {
        assertEquals(EnumUrlType.FILE, WebPageUtil.getUrlType(".doc"));
    }
    @Test
    public void getUrlType_audioSuffixs() throws Exception {
        assertEquals(EnumUrlType.AUDIO, WebPageUtil.getUrlType(".MP3"));
    }
    @Test
    public void getUrlType_htmlSuffixs() throws Exception {
        assertEquals(EnumUrlType.HTML, WebPageUtil.getUrlType(".html"));
    }
    @Test
    public void getUrlType_null() throws Exception {
        assertEquals(EnumUrlType.HTML, WebPageUtil.getUrlType(""));
    }

}