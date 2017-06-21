package com.trs.gov.kpi.service;

import com.trs.gov.kpi.entity.MonitorFrequency;
import com.trs.gov.kpi.entity.exception.BizException;
import com.trs.gov.kpi.entity.responsedata.MonitorFrequencyResponse;
import com.trs.gov.kpi.entity.requestdata.MonitorFrequencySetUp;

import java.util.List;

/**
 * Created by helang on 2017/5/12.
 */
public interface MonitorFrequencyService {

    /**
     * 通过siteId查询对应的监测频率记录，并将返回结果处理成MonitorFrequencyDeal对象，返回给前端的数据模板中的Data
     * @param siteId
     * @return
     */
    List<MonitorFrequencyResponse> queryBySiteId(int siteId);

    /**
     * 插入监测频率记录（一个站点有多个监测频率记录）
     * @param monitorFrequencySetUp
     * @return
     */
    int addMonitorFrequencySetUp(MonitorFrequencySetUp monitorFrequencySetUp) throws BizException;

    /**
     * 通过siteId去验证当前传入的对象是否存在，存在将做修改记录，不存在将插入记录，实际也是一次通过siteId的查询
     * @param siteId
     * @return
     */
    List<MonitorFrequency> checkSiteIdAndTypeAreBothExitsOrNot(int siteId);

    /**
     * 传入设置的对象，去更新数据库中对应siteId和typeId的记录
     * @param monitorFrequencySetUp
     * @return
     */
    int updateMonitorFrequencySetUp(MonitorFrequencySetUp monitorFrequencySetUp) throws BizException;
}
