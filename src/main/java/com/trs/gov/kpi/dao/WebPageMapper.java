package com.trs.gov.kpi.dao;

import com.trs.gov.kpi.entity.*;
import com.trs.gov.kpi.entity.dao.QueryFilter;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * Created by ranwei on 2017/6/6.
 */
@Mapper
public interface WebPageMapper {

    /**
     * 查询数量
     *
     * @param filter
     * @return
     */
    int count(QueryFilter filter);

    /**
     * 查询响应速度
     *
     * @param filter
     * @return
     */
    List<ReplySpeed> selectReplySpeed(QueryFilter filter);

    /**
     * 查询过大页面
     *
     * @param filter
     * @return
     */
    List<PageSpace> selectPageSpace(QueryFilter filter);

    /**
     * 查询过深页面
     *
     * @param filter
     * @return
     */
    List<PageDepth> selectPageDepth(QueryFilter filter);

    /**
     * 查询冗余代码
     *
     * @param filter
     * @return
     */
    List<RepeatCode> selectRepeatCode(QueryFilter filter);

    /**
     * 查询过长URL页面
     *
     * @param filter
     * @return
     */
    List<UrlLength> selectUrlLength(QueryFilter filter);

    /**
     * 查询监测时间最早的网页分析数据
     *
     * @return
     */
    Date getEarliestCheckTime();

    /**
     * 批量处理网页问题
     *
     * @param siteId
     * @param ids
     */
    void handlePageByIds(@Param("siteId") int siteId, @Param("ids") List<Integer> ids);

    /**
     * 批量忽略网页问题
     *
     * @param siteId
     * @param ids
     */
    void ignorePageByIds(@Param("siteId") int siteId, @Param("ids") List<Integer> ids);

    /**
     * 批量删除网页问题
     *
     * @param siteId
     * @param ids
     */
    void delPageByIds(@Param("siteId") int siteId, @Param("ids") List<Integer> ids);

    /**
     * 更新过大页面：
     * 多次检测时存储最新的一次
     *
     * @param pageSpace
     */
    void updatePageSpace(PageSpace pageSpace);

    /**
     * 删除过大页面：
     * 第二次检测时当前数据以不存在，需要将其从数据库中清除
     *
     * @param id
     */
    void daleteRepeatPageSpace(String id);

    /**
     * 更新过长URL页面：
     * 多次检测时存储最新的一次
     *
     * @param urlLength
     */
    void updateUrlLength(UrlLength urlLength);

    /**
     * 删除过长URL页面：
     * 第二次检测时当前数据以不存在，需要将其从数据库中清除
     *
     * @param id
     */
    void daleteRepeatUrlLength(String id);

    /**
     * 更新过深页面：
     * 多次检测时存储最新的一次
     *
     * @param pageDepth
     */
    void updatePageDepth(PageDepth pageDepth);

    /**
     * 删除过深页面：
     * 第二次检测时当前数据以不存在，需要将其从数据库中清除
     *
     * @param id
     */
    void daleteRepeatPageDepth(String id);

    /**
     * 更新响应速度：
     * 多次检测时存储最新的一次
     *
     * @param replySpeed
     */
    void updateReplySpeed(ReplySpeed replySpeed);

    /**
     * 删除响应速度：
     * 第二次检测时当前数据以不存在，需要将其从数据库中清除
     *
     * @param id
     */
    void daleteRepeatReplySpeed(String id);

}
