package com.trs.gov.kpi.dao;

import com.trs.gov.kpi.entity.IssueBase;
import com.trs.gov.kpi.entity.LinkAvailability;
import com.trs.gov.kpi.entity.dao.CondDBField;
import com.trs.gov.kpi.entity.dao.DBPager;
import com.trs.gov.kpi.entity.dao.SortDBField;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * Created by rw103 on 2017/5/11.
 */
@Mapper
public interface LinkAvailabilityMapper extends OperationMapper {

    /**
     * 获取每月新增问题数
     *
     * @param issueBase
     * @return
     */
    int getIssueHistoryCount(@Param("issueBase") IssueBase issueBase);

    /**
     * 查询未解决问题集合
     *
     * @param startIndex
     * @param pageSize
     * @param issueBase
     * @return
     */
    List<LinkAvailability> getIssueList(@Param("startIndex") Integer startIndex, @Param("pageSize") Integer pageSize, @Param("issueBase") IssueBase issueBase);

    /**
     * 获取符合条件的链接问题数量
     *
     * @param condFields
     * @param sortFields
     * @param pager
     * @return
     */
    List<LinkAvailability> getIssueListBySql(@Param("condFields") List<CondDBField> condFields, @Param("sortFields") List<SortDBField> sortFields, @Param("pager") DBPager pager);


    /**
     * 批量添加链接问题
     *
     * @param linkAvailabilities
     */
    void batchInsertLinkAvailabilities(@Param("linkAvailabilities") List<LinkAvailability> linkAvailabilities);

    /**
     * 获取符合条件的链接问题数量
     *
     * @param condFields
     * @return
     */
    int getIssueCount(@Param("condFields") List<CondDBField> condFields);

    /**
     * 查询未解决失效图片的问题数
     *
     * @param issueBase
     * @return
     */
    int getInvalidImageCount(@Param("issueBase") IssueBase issueBase);

    /**
     * 查询未解决连接超时的问题数
     *
     * @param issueBase
     * @return
     */
    int getConnTimeoutCount(@Param("issueBase") IssueBase issueBase);

    /**
     * 查询未解决失效附件的问题数
     *
     * @param issueBase
     * @return
     */
    int getInvalidFileCount(@Param("issueBase") IssueBase issueBase);

    /**
     * 查询未解决失效首页的问题数
     *
     * @param issueBase
     * @return
     */
    int getInvalidHomepageCount(@Param("issueBase") IssueBase issueBase);

    /**
     * 根据站点编号获取首页
     *
     * @param issueBase
     * @return
     */
    String getIndexUrl(@Param("issueBase") IssueBase issueBase);

    /**
     * 查询问题中是否包含首页不可用
     *
     * @param indexUrl
     * @return
     */
    int getIndexAvailability(@Param("indexUrl") String indexUrl, @Param("issueBase") IssueBase issueBase);

    /**
     * 查询首页不可用问题的监测时间
     *
     * @param indexUrl
     * @return
     */
    Date getMonitorTime(@Param("indexUrl") String indexUrl, @Param("issueBase") IssueBase issueBase);

    /**
     * @param issueBase
     * @return
     */
    int getInvalidLinkCount(@Param("issueBase") IssueBase issueBase);

}
