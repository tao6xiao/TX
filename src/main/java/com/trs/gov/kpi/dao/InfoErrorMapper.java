package com.trs.gov.kpi.dao;

import com.trs.gov.kpi.entity.InfoError;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by ran.wei on 2017/5/15.
 */
@Mapper
public interface InfoErrorMapper extends OperationMapper {

    /**
     * 查询未解决问题集合
     * @param currPage
     * @param pageSize
     * @param infoError
     * @return
     */
    List<InfoError> getIssueList(@Param("currPage") int currPage,@Param("pageSize") int pageSize,@Param("infoError") InfoError infoError);
}
