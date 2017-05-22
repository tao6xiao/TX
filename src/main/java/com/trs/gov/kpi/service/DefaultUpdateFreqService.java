package com.trs.gov.kpi.service;

import com.trs.gov.kpi.entity.DefaultUpdateFreq;

import java.text.ParseException;

/**
 * 按需更新的自查提醒service接口
 * Created by he.lang on 2017/5/15.
 */
public interface DefaultUpdateFreqService {

    /**
     * 通过站点id获取对应自查提醒对象
     * @param siteId
     * @return
     */
    DefaultUpdateFreq getDefaultUpdateFreqBySiteId(int siteId);

    /**
     * 新增当前站点的自查提醒记录
     * @param defaultUpdateFreq
     * @return
     */
    int addDefaultUpdateFreq(DefaultUpdateFreq defaultUpdateFreq) throws ParseException;

    /**
     * 修改当前站点的自查提醒记录
     * @param defaultUpdateFreq
     * @return
     */
    int updateDefaultUpdateFreq(DefaultUpdateFreq defaultUpdateFreq);
}
