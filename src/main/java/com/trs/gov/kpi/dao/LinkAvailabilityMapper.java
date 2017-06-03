package com.trs.gov.kpi.dao;

import com.trs.gov.kpi.entity.dao.CondDBField;
import com.trs.gov.kpi.entity.dao.DBPager;
import com.trs.gov.kpi.entity.dao.SortDBField;
import com.trs.gov.kpi.entity.responsedata.LinkAvailabilityResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by rw103 on 2017/5/11.
 */
@Mapper
public interface LinkAvailabilityMapper {

    /**
     * 获取符合条件的链接问题数量
     *
     * @param condFields
     * @param sortFields
     * @param pager
     * @return
     */
    List<LinkAvailabilityResponse> getIssueListBySql(@Param("condFields") List<CondDBField> condFields, @Param("sortFields") List<SortDBField> sortFields, @Param("pager") DBPager pager);

    /**
     * 获取符合条件的链接问题数量
     *
     * @param condFields
     * @return
     */
    int getIssueCount(@Param("condFields") List<CondDBField> condFields);

}
