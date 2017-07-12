package com.trs.gov.kpi.utils;

import com.trs.gov.kpi.constant.EnumUrlType;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by he.lang on 2017/7/11.
 */
public class WebPageUtil {

    private static final String[] imageSuffixs = new String[]{"bmp", "jpg", "jpeg", "png", "gif"};

    private static final String[] fileSuffixs = new String[]{"zip", "doc", "xls", "xlsx", "docx", "rar"};

    private static final String[] audioSuffixs = new String[]{"MP3", "AAC", "WAV", "WMA", "CDA", "FLAC", "M4A", "MID", "MKA", "MP2", "MPA", "MPC", "APE", "OFR", "OGG", "RA", "WV", "TTA", "AC3", "DTS"};

    private static final String[] htmlSuffixs = new String[]{"htm", "html", "shtml", "xml"};

    private static final String[] videoSuffixs = new String[]{"avi", "rmvb", "rm", "asf", "divx", "mpg", "mpeg", "mpe", "wmv", "mp4", "mkv", "vob"};

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

        for (String imageSuffix : videoSuffixs) {

            if (StringUtils.equalsIgnoreCase(suffix, imageSuffix)) {

                return EnumUrlType.VIDEO;
            }
        }

        for (String imageSuffix : htmlSuffixs) {

            if (StringUtils.equalsIgnoreCase(suffix, imageSuffix)) {

                return EnumUrlType.HTML;
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

                return EnumUrlType.AUDIO;
            }
        }

        return EnumUrlType.HTML;
    }

}
