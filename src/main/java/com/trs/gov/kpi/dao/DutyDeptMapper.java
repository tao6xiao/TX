package com.trs.gov.kpi.dao;

import com.trs.gov.kpi.entity.DutyDept;
import com.trs.gov.kpi.entity.dao.DBRow;
import com.trs.gov.kpi.entity.dao.QueryFilter;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by he.lang on 2017/7/5.
 */
@Mapper
public interface DutyDeptMapper {
    /**
     * 插入记录
     *
     * @param row
     * @return
     */
    int insert(DBRow row);


    /**
     * 查询相关对象集合
     *
     * @param filter
     * @return
     */
    List<DutyDept> select(QueryFilter filter);

    /**
     * 查询数量
     *
     * @param filter
     * @return
     */
    int count(QueryFilter filter);

    /**
     * 修改栏目和部门关系
     *
     * @param dutyDept
     * @return
     */
    int update(DutyDept dutyDept);

    /**
     * 删除对应站点和栏目下设置的部门
     *
     * @param siteId
     * @param chnlId
     */
    int delete(@Param("siteId") int siteId, @Param("chnlId") int chnlId);
}
