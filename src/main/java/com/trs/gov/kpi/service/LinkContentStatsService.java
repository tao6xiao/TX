package com.trs.gov.kpi.service;

/**
 * Created by li.hao on 2017/8/11.
 */
public interface LinkContentStatsService {

    /**
     * 插入链接内容信息
     *
     * @param siteId
     * @param typeId
     * @param content
     */
    void insertLinkContent(Integer siteId, Integer typeId,String url, String content);

    /**
     * 获取当前检测的链接内容的md5编码
     *
     * @param siteId
     * @param typeId
     * @return
     */
    String getThisTimeMD5(Integer siteId, Integer typeId, String url);

    /**
     * 获取上一次检测的链接内容的md5编码
     *
     * @param siteId
     * @param typeId
     * @return
     */
    String getLastTimeMD5(Integer siteId, Integer typeId, String url);
}
