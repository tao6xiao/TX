package com.trs.gov.kpi.service.impl.wangkang;

import com.trs.gov.kpi.dao.CommonMapper;
import com.trs.gov.kpi.dao.WkAvgSpeedUpdateContentMapper;
import com.trs.gov.kpi.entity.responsedata.WkAvgSpeedAndUpdateContentResponse;
import com.trs.gov.kpi.entity.wangkang.WkAvgSpeed;
import com.trs.gov.kpi.entity.wangkang.WkUpdateContent;
import com.trs.gov.kpi.service.wangkang.WkAvgSpeedUpdateContentService;
import com.trs.gov.kpi.utils.DBUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by li.hao on 2017/7/12.
 */
@Service
public class WkAvgSpeedUpdateContentServiceImpl implements WkAvgSpeedUpdateContentService {

    @Resource
    WkAvgSpeedUpdateContentMapper wkAvgSpeedUpdateContentMapper;

    @Resource
    CommonMapper commonMapper;

    /*---平均访问速度---*/
    @Override
    public List<WkAvgSpeedAndUpdateContentResponse> getAvgSpeedHistory() {
        List<WkAvgSpeed> wkAvgSpeedList = wkAvgSpeedUpdateContentMapper.getAvgSpeedHistory();
        List<WkAvgSpeedAndUpdateContentResponse> wkAvgSpeedAndUpdateContentList = new ArrayList<>();

        if(wkAvgSpeedList.size() != 0){
            for (WkAvgSpeed wkAvgSpeed: wkAvgSpeedList) {
                WkAvgSpeedAndUpdateContentResponse WkAvgSpeedAndUpdateContent = new WkAvgSpeedAndUpdateContentResponse();
                WkAvgSpeedAndUpdateContent.setCheckTime(wkAvgSpeed.getCheckTime());
                WkAvgSpeedAndUpdateContent.setAvgSpeed(wkAvgSpeed.getAvgSpeed());

                wkAvgSpeedAndUpdateContentList.add(WkAvgSpeedAndUpdateContent);
            }
        }
        return wkAvgSpeedAndUpdateContentList;
    }

    @Override
    public void insertOnceCheckWkAvgSpeed(WkAvgSpeed wkAvgSpeed) {

        commonMapper.insert(DBUtil.toRow(wkAvgSpeed));
    }

    /*---网站更新数---*/
    @Override
    public List<WkAvgSpeedAndUpdateContentResponse> getUpdateContentHistory() {
        List<WkUpdateContent> wkUpdateContentList = wkAvgSpeedUpdateContentMapper.getUpdateContentHistory();
        List<WkAvgSpeedAndUpdateContentResponse> wkAvgSpeedAndUpdateContentList = new ArrayList<>();

        if(wkUpdateContentList.size() != 0){
            for (WkUpdateContent wkUpdateContent: wkUpdateContentList) {
                WkAvgSpeedAndUpdateContentResponse WkAvgSpeedAndUpdateContent = new WkAvgSpeedAndUpdateContentResponse();
                WkAvgSpeedAndUpdateContent.setCheckTime(wkUpdateContent.getCheckTime());
                WkAvgSpeedAndUpdateContent.setUpdateContent(wkUpdateContent.getUpdateContent());

                wkAvgSpeedAndUpdateContentList.add(WkAvgSpeedAndUpdateContent);
            }
        }
        return wkAvgSpeedAndUpdateContentList;
    }

    @Override
    public void insertOnceCheckWkUpdateContent(WkUpdateContent wkUpdateContent) {

        commonMapper.insert(DBUtil.toRow(wkUpdateContent));
    }
}
