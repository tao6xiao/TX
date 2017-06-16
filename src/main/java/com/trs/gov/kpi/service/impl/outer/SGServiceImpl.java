package com.trs.gov.kpi.service.impl.outer;

import com.trs.gov.kpi.entity.outerapi.sp.SGHistoryStatistics;
import com.trs.gov.kpi.entity.outerapi.sp.SGPageDataRes;
import com.trs.gov.kpi.entity.outerapi.sp.SGStatistics;
import com.trs.gov.kpi.entity.requestdata.PageDataRequestParam;
import com.trs.gov.kpi.service.outer.SGService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ranwei on 2017/6/12.
 */
@Service
public class SGServiceImpl implements SGService {

    @Override
    public SGPageDataRes getSGList(PageDataRequestParam param) {
        return null;
    }

    @Override
    public SGStatistics getSGCount(PageDataRequestParam param) {
        return null;
    }

    @Override
    public List<SGHistoryStatistics> getSGHistoryCount(PageDataRequestParam param) {
        return new ArrayList<>();
    }
}
