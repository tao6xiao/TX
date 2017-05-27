package com.trs.gov.kpi.service;


import com.trs.gov.kpi.entity.IssueBase;
import com.trs.gov.kpi.entity.responsedata.IssueIsResolvedResponse;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

/**
 * 综合监测：已处理service
 * Created by he.lang on 2017/5/19.
 */
public interface IntegratedMonitorIsResolvedService extends OperationService{

    /**
     * 获取分页数据（通用方法：已解决，已忽略）
     * @param pageIndex
     * @param pageSize
     * @param issue
     * @return
     */
    List<IssueIsResolvedResponse> getPageDataIsResolvedList(Integer pageIndex, Integer pageSize, @ModelAttribute IssueBase issue);

    /**
     * 获取已解决的数据条数（通用方法，已解决，已忽略）
     * @param issue
     * @return
     */
    int getPageDataIsResolvedItemCount(IssueBase issue);

}
