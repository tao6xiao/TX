package com.trs.gov.kpi.utils;

import com.trs.gov.kpi.constant.EnumUrlType;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by he.lang on 2017/7/11.
 */
public class WebPageUtil {


    private static final String[] resSuffixs = new String[]{"css", "js"};

    private static final String[] imageSuffixs = new String[]{"png", "gif", "mpg", "mpeg", "mpe", "m1v", "mov", "qt", "jpeg", "jpg", "jpe", "bmp", "gif", "ico", "png", "tif", "tiff"};

    private static final String[] fileSuffixs = new String[]{"zip", "doc", "xls", "xlsx", "docx", "rar"};

    private static final String[] audioSuffixs = new String[]{"MP3", "AAC", "WAV", "WMA", "CDA", "FLAC", "M4A", "MID", "MKA", "MP2", "MPA", "MPC", "APE", "OFR", "OGG", "RA", "WV", "TTA", "AC3", "DTS"};

    private static final String[] htmlSuffixs = new String[]{"htm", "html", "shtml", "xml"};

    private static final String[] videoSuffixs = new String[]{"m3u", "mpega", "mp1", "mp2", "mp3", "mpa", "wav", "wax", "wma", "aif", "aifc", "aiff", "au", "snd", "ulw", "mid", "midi", "kar", "smf", "wm", "wmv", "wvx", "avi", "flv", "asx", "asf", "swf", "swfl", "awf"};

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

        for (String imageSuffix : htmlSuffixs) {

            if (StringUtils.equalsIgnoreCase(suffix, imageSuffix)) {

                return EnumUrlType.HTML;
            }
        }

        for (String imageSuffix : videoSuffixs) {

            if (StringUtils.equalsIgnoreCase(suffix, imageSuffix)) {

                return EnumUrlType.VIDEO;
            }
        }

        for (String imageSuffix : imageSuffixs) {

            if (StringUtils.equalsIgnoreCase(suffix, imageSuffix)) {

                return EnumUrlType.IMAGE;
            }
        }

        for (String fileSuffix : fileSuffixs) {

            if (StringUtils.equalsIgnoreCase(suffix, fileSuffix)) {

                return EnumUrlType.FILE;
            }
        }

        for (String fileSuffix : audioSuffixs) {

            if (StringUtils.equalsIgnoreCase(suffix, fileSuffix)) {

                return EnumUrlType.VIDEO;
            }
        }

        for (String fileSuffix : resSuffixs) {

            if (StringUtils.equalsIgnoreCase(suffix, fileSuffix)) {

                return EnumUrlType.RES;
            }
        }

        return EnumUrlType.HTML;
    }

}
