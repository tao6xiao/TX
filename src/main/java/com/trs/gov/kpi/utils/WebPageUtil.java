package com.trs.gov.kpi.utils;

import com.trs.gov.kpi.constant.EnumUrlType;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

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

    /**
     * 缓存父链接
     *
     * @param targetUrls
     * @param pageParentMap
     * @param pageUrl
     */
    public static void addParentUrl(List<String> targetUrls, Map<String, Set<String>> pageParentMap, String pageUrl) {
        for (String targetUrl : targetUrls) {
            if (!pageParentMap.containsKey(targetUrl)) {
                pageParentMap.put(targetUrl.intern(), Collections.synchronizedSet(new HashSet<String>()));
            }
            Set<String> parentUrlSet = pageParentMap.get(targetUrl);
            if (!targetUrl.equals(pageUrl.intern()) && !isEqual(targetUrl, pageUrl)) {
                parentUrlSet.add(pageUrl.intern());
            }
        }
    }

    /**
     * 判断是否为同一url
     * @param targetUrl
     * @param pageUrl
     * @return
     */
    private static Boolean isEqual(String targetUrl, String pageUrl) {
        if (targetUrl.startsWith(pageUrl)) {
            String remainStr = targetUrl.substring(pageUrl.length());
            if (remainStr.equals("/") || remainStr.equals("#") || remainStr.endsWith("/#")) {
                return true;
            }
        }
        if (pageUrl.startsWith(targetUrl)) {
            String remainStr = pageUrl.substring(targetUrl.length());
            if (remainStr.equals("/") || remainStr.equals("#") || remainStr.endsWith("/#")) {
                return true;
            }
        }
        return false;
    }

}
