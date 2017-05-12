package com.trs.gov.kpi.service;

import com.trs.gov.kpi.entity.MonitorFrequency;
import com.trs.gov.kpi.entity.MonitorFrequencyDeal;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by helang on 2017/5/12.
 */
@Service
public interface MonitorFrequencyService {

    /**
     * 通过siteId查询对应的监测频率记录，并将返回结果处理成MonitorFrequencyDeal对象，返回给前端的数据模板中的Data
     * @param siteId
     * @return
     */
    List<MonitorFrequencyDeal> queryBySiteId(int siteId);

}
