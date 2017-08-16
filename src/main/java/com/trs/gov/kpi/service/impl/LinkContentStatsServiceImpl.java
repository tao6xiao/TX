package com.trs.gov.kpi.service.impl;

import com.trs.gov.kpi.dao.CommonMapper;
import com.trs.gov.kpi.dao.LinkContentStatsMapper;
import com.trs.gov.kpi.entity.LinkContentStats;
import com.trs.gov.kpi.service.LinkContentStatsService;
import com.trs.gov.kpi.utils.DBUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * Created by li.hao on 2017/8/11.
 */
@Service
public class LinkContentStatsServiceImpl implements LinkContentStatsService {

    @Resource
    private LinkContentStatsMapper linkContentStatsMapper;

    @Resource
    private CommonMapper commonMapper;


    @Override
    public void insertLinkContent(Integer siteId, Integer typeId,String url, String content, Date linkContentStatsCheckTime) {

        LinkContentStats linkContent = new LinkContentStats();
        linkContent.setSiteId(siteId);
        linkContent.setTypeId(typeId);
        linkContent.setUrl(url);
        linkContent.setMd5(DigestUtils.md5Hex(content));
        linkContent.setCheckTime(linkContentStatsCheckTime);

        commonMapper.insert(DBUtil.toRow(linkContent));
    }

    @Override
    public String getThisTimeMD5(Integer siteId, Integer typeId, String url) {
        return linkContentStatsMapper.selectThisTimeMD5(siteId, typeId, url);
    }

    @Override
    public String getLastTimeMD5(Integer siteId, Integer typeId, String url) {
        return linkContentStatsMapper.selectLastTimeMD5(siteId, typeId, url);
    }
}
