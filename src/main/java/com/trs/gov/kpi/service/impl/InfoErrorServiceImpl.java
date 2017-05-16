package com.trs.gov.kpi.service.impl;

import com.trs.gov.kpi.dao.InfoErrorMapper;
import com.trs.gov.kpi.entity.InfoError;
import com.trs.gov.kpi.service.InfoErrorService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by ranwei on 2017/5/15.
 */
@Service
public class InfoErrorServiceImpl extends OperationServiceImpl implements InfoErrorService {

    @Resource
    private InfoErrorMapper infoErrorMapper;

    @Override
    public int getHandledIssueCount(int siteId) {
        return infoErrorMapper.getHandledIssueCount(siteId);
    }

    @Override
    public int getUnhandledIssueCount(int siteId) {
        return infoErrorMapper.getUnhandledIssueCount(siteId);
    }

    @Override
    public int getIssueHistoryCount(InfoError infoError) {
        return infoErrorMapper.getIssueHistoryCount(infoError);
    }

    @Override
    public List<InfoError> getIssueList(Integer currPage, Integer pageSize, InfoError infoError) {
        return infoErrorMapper.getIssueList(currPage, pageSize, infoError);
    }
}
