package com.trs.gov.kpi.service;

import com.trs.gov.kpi.entity.InfoUpdate;

import java.util.List;

/**
 * Created by rw103 on 2017/5/13.
 */
public interface InfoUpdateService extends OperationService{


    List<InfoUpdate> getIssueList(int currPage, int pageSize, InfoUpdate infoUpdate);
}
