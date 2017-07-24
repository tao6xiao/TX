package com.trs.gov.kpi.service.wangkang;

import com.trs.gov.kpi.entity.wangkang.WkIssue;

import java.util.List;

/**
 * Created by linwei on 2017/7/14.
 */
public interface WkIssueService {

    /**
     * 根据网站编号，检查编号，和typeId查询内容错误总数
     * @param siteId
     * @param checkId
     * @return
     */
    int getErrorWordsCount(Integer siteId, Integer checkId);

    /**
     * 根据网站编号，检查编号，和typeId查询错误链接总数
     * @param siteId
     * @param checkId
     * @return
     */
    int getInvalidLinkCount(Integer siteId, Integer checkId);

    List<WkIssue> getErrorWordsList(Integer siteId, Integer checkId);

    List<WkIssue> getInvalidLinkList(Integer siteId, Integer checkId);

    /**
     * 根据网站编号、检查编号、subTypeId查询错别字数量
     * @param siteId
     * @param checkId
     * @return
     */
    int getTyposCount(Integer siteId, Integer checkId);

    /**
     * 根据网站编号、检查编号、subTypeId查询敏感词数量
     * @param siteId
     * @param checkId
     * @return
     */
    int getSensitiveWordsCount(Integer siteId, Integer checkId);

    /**
     * 根据网站编号、检查编号、subTypeId查询政治词语数量
     * @param siteId
     * @param checkId
     * @return
     */
    int getPoliticsCount(Integer siteId, Integer checkId);

    /**
     * 根据网站编号、检查编号、subTypeId常规链接错误数量
     * @param siteId
     * @param checkId
     * @return
     */
    int getRoutineLinkCount(Integer siteId, Integer checkId);

    /**
     * 根据网站编号、检查编号、subTypeId其他链接错误数量
     * @param siteId
     * @param checkId
     * @return
     */
    int getOthersLinkCount(Integer siteId, Integer checkId);
}
