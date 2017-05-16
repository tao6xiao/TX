package com.trs.gov.kpi.service;

import com.trs.gov.kpi.entity.InfoError;

import java.util.List;

/**
 * Created by ranwei on 2017/5/15.
 */
public interface InfoErrorService extends OperationService {

    int getIssueHistoryCount(InfoError infoError);

    List<InfoError> getIssueList(Integer currPage, Integer pageSize, InfoError infoError);

}
