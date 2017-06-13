package com.trs.gov.kpi.service.impl.outer;

import com.trs.gov.kpi.entity.outerapi.sp.SGPageDataRes;
import com.trs.gov.kpi.entity.outerapi.sp.SPHistoryStatistics;
import com.trs.gov.kpi.entity.outerapi.sp.SPStatistics;
import com.trs.gov.kpi.entity.requestdata.PageDataRequestParam;
import com.trs.gov.kpi.service.outer.SPService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ranwei on 2017/6/12.
 */
@Service
public class SPServiceImpl implements SPService {

    @Override
    public SGPageDataRes getSGList(PageDataRequestParam param) {
        return null;
    }

    @Override
    public SPStatistics getSPCount(PageDataRequestParam param) {
        return null;
    }

    @Override
    public List<SPHistoryStatistics> getSPHistoryCount(PageDataRequestParam param) {
        return new ArrayList<>();
    }
}
