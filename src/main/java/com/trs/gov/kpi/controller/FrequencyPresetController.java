package com.trs.gov.kpi.controller;

import com.trs.gov.kpi.entity.responsedata.ApiPageData;
import com.trs.gov.kpi.entity.responsedata.FrequencyPresetResponseDeal;
import com.trs.gov.kpi.entity.exception.BizException;
import com.trs.gov.kpi.service.FrequencyPresetService;
import com.trs.gov.kpi.utils.PageInfoDeal;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 更新频率及预警初设Controller
 * Created by he.lang on 2017/5/15.
 */
@RestController
@RequestMapping("/gov/kpi/setting")
public class FrequencyPresetController {
    @Resource
    FrequencyPresetService frequencyPresetService;

    @RequestMapping(value = "/presetfreq", method = RequestMethod.GET)
    @ResponseBody
    public ApiPageData getPageDataBySiteId(@RequestParam("siteId") Integer siteId, @RequestParam("pageSize") Integer pageSize, @RequestParam("pageIndex") Integer pageIndex) throws BizException {
        if(siteId == null){
            throw new BizException("站点编号不能为null");
        }
        int itemCount = frequencyPresetService.getItemCountBySiteId(siteId);
        ApiPageData apiPageData = PageInfoDeal.getApiPageData(pageIndex, pageSize, itemCount);
        List<FrequencyPresetResponseDeal> frequencyPresetResponseDealList = frequencyPresetService.getPageDataBySiteId(siteId, apiPageData.getPager().getCurrPage()-1, apiPageData.getPager().getPageSize());
        apiPageData.setData(frequencyPresetResponseDealList);
        return apiPageData;
    }
}
