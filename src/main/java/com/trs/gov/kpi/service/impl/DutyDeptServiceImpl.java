package com.trs.gov.kpi.service.impl;

import com.trs.gov.kpi.constant.DutyDeptTableField;
import com.trs.gov.kpi.dao.DutyDeptMapper;
import com.trs.gov.kpi.entity.DutyDept;
import com.trs.gov.kpi.entity.dao.QueryFilter;
import com.trs.gov.kpi.entity.dao.Table;
import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.gov.kpi.entity.requestdata.DutyDeptRequest;
import com.trs.gov.kpi.entity.requestdata.PageDataRequestParam;
import com.trs.gov.kpi.entity.responsedata.ApiPageData;
import com.trs.gov.kpi.entity.responsedata.DutyDeptResponse;
import com.trs.gov.kpi.entity.responsedata.Pager;
import com.trs.gov.kpi.service.DutyDeptService;
import com.trs.gov.kpi.service.helper.QueryFilterHelper;
import com.trs.gov.kpi.service.outer.DeptApiService;
import com.trs.gov.kpi.service.outer.SiteApiService;
import com.trs.gov.kpi.utils.DBUtil;
import com.trs.gov.kpi.utils.PageInfoDeal;
import com.trs.gov.kpi.utils.StringUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by he.lang on 2017/7/5.
 */
@Service
public class DutyDeptServiceImpl implements DutyDeptService {

    @Resource
    DutyDeptMapper deptMapper;

    @Resource
    SiteApiService siteApiService;

    @Resource
    DeptApiService deptApiService;

    @Override
    public void add(DutyDeptRequest deptRequest) {
        deptMapper.insert(DBUtil.toRow(getByRequest(deptRequest)));
    }

    private DutyDept getByRequest(DutyDeptRequest deptRequest) {
        DutyDept dutyDept = new DutyDept();
        dutyDept.setChnlId(deptRequest.getChnlId());
        dutyDept.setSiteId(deptRequest.getSiteId());
        dutyDept.setDeptId(deptRequest.getDeptId());
        dutyDept.setContain(deptRequest.getContain());
        return dutyDept;
    }

    @Override
    public void update(DutyDeptRequest deptRequest) {
        deptMapper.update(getByRequest(deptRequest));
    }

    @Override
    public DutyDept getByChnlId(int chnlId) {
        QueryFilter filter = new QueryFilter(Table.DUTY_DEPT);
        filter.addCond(DutyDeptTableField.CHNL_ID, chnlId);
        if (deptMapper.select(filter) == null || deptMapper.select(filter).isEmpty()) {
            return null;
        }
        return deptMapper.select(filter).get(0);
    }

    @Override
    public ApiPageData get(PageDataRequestParam param) throws RemoteException {
        if (!StringUtil.isEmpty(param.getSearchText())) {
            param.setSearchText(StringUtil.escape(param.getSearchText()));
        }
        QueryFilter filter = QueryFilterHelper.toSetDeptFilter(param, siteApiService, deptApiService);
        Pager pager = PageInfoDeal.buildResponsePager(param.getPageIndex(), param.getPageSize(), deptMapper.count(filter));
        filter.setPager(pager);
        List<DutyDept> dutyDepts = deptMapper.select(filter);
        return new ApiPageData(pager, getResponseList(dutyDepts));
    }

    private List<DutyDeptResponse> getResponseList(List<DutyDept> dutyDepts) throws RemoteException {
        if (dutyDepts == null || dutyDepts.isEmpty()) {
            return new ArrayList<>();
        }
        List<DutyDeptResponse> deptResponses = new ArrayList<>();
        for (DutyDept dutyDept : dutyDepts) {
            deptResponses.add(getResponse(dutyDept));
        }
        return deptResponses;
    }

    private DutyDeptResponse getResponse(DutyDept dutyDept) throws RemoteException {
        DutyDeptResponse deptResponse = new DutyDeptResponse();
        if (siteApiService.getChannelById(dutyDept.getChnlId(), "") != null && deptApiService.findDeptById("", dutyDept.getDeptId()) != null) {
            deptResponse.setChnlId(dutyDept.getChnlId());
            deptResponse.setChnlName(siteApiService.getChannelById(dutyDept.getChnlId(), "").getChnlName());
            deptResponse.setSiteId(dutyDept.getSiteId());
            deptResponse.setDeptId(dutyDept.getDeptId());
            deptResponse.setDeptName(deptApiService.findDeptById("", dutyDept.getDeptId()).getGName());
            deptResponse.setContain(dutyDept.getContain());
        }
        return deptResponse;
    }
}
