package com.trs.gov.kpi.service.impl;

import com.trs.gov.kpi.dao.InfoUpdateMapper;
import com.trs.gov.kpi.entity.InfoUpdate;
import com.trs.gov.kpi.service.InfoUpdateService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by rw103 on 2017/5/13.
 */
@Service
public class InfoUpdateServiceImpl extends OperationServiceImpl implements InfoUpdateService {

    @Resource
    private InfoUpdateMapper infoUpdateMapper;

    @Override
    public int getHandledIssueCount(int siteId) {
        return infoUpdateMapper.getHandledIssueCount(siteId);
    }

    @Override
    public int getUpdateNotIntimeCount(int siteId) {
        return infoUpdateMapper.getUpdateNotIntimeCount(siteId);
    }


    @Override
    public int getUpdateWarningCount(int siteId) {
        return infoUpdateMapper.getUpdateWarningCount(siteId);
    }

    @Override
    public List<InfoUpdate> getIssueList(int currPage, int pageSize, InfoUpdate infoUpdate) {
        return infoUpdateMapper.getIssueList(currPage, pageSize, infoUpdate);
    }
}
