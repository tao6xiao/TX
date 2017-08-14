package com.trs.gov.kpi.utils;

import com.trs.gov.kpi.constant.EnumUrlType;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by he.lang on 2017/7/11.
 */
public class WebPageUtil {


    private static final String[] resSuffixs = new String[]{"css", "js"};

    private static final String[] imageSuffixs = new String[]{"gif", "mpg", "mpe", "m1v", "mov", "qt", "jpeg", "jpg", "jpe", "bmp", "gif", "ico", "png", "tif", "tiff"};

    private static final String[] fileSuffixs = new String[]{"zip", "doc", "xls", "xlsx", "docx", "rar"};

    private static final String[] audioSuffixs = new String[]{"MP1", "MP2", "MP3", "AAC", "WAV", "WMA", "CDA", "FLAC", "M4A", "MID", "MKA", "MP2", "MPA", "MPC", "APE", "OFR", "OGG", "RA",
            "WV", "TTA", "AC3", "DTS"};

    private static final String[] htmlSuffixs = new String[]{"htm", "html", "shtml", "xml"};

    private static final String[] videoSuffixs = new String[]{"MP4", "3GP", "AVI", "MKV", "WMV", "MPG", "VOB", "FLV", "SWF", "MOV", "RMVB", "MPEG", "KAR", "SMF", "WM", "ASX",
            "ASF", "SWFL", "AWF"};

    private WebPageUtil() {
    }

    /**
     * 判断url的类型
     *
     * @param url
     * @return
     */
    public static EnumUrlType getUrlType(String url) {

        String suffix = url.substring(url.lastIndexOf('.') + 1);


        if (isExist(suffix, htmlSuffixs)) {
            return EnumUrlType.HTML;
        } else if (isExist(suffix, videoSuffixs)) {
            return EnumUrlType.VIDEO;
        } else if (isExist(suffix, imageSuffixs)) {
            return EnumUrlType.IMAGE;
        } else if (isExist(suffix, fileSuffixs)) {
            return EnumUrlType.FILE;
        } else if (isExist(suffix, audioSuffixs)) {
            return EnumUrlType.AUDIO;
        } else if (isExist(suffix, resSuffixs)) {
            return EnumUrlType.RES;
        }
        return EnumUrlType.HTML;
    }

    private static boolean isExist(String checkSuffix, String[] suffixes) {
        for (String suffix : suffixes) {
            if (StringUtils.equalsIgnoreCase(checkSuffix, suffix)) {
                return true;
            }
        }
        return false;
    }

}
