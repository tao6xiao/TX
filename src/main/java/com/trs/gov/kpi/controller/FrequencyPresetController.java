package com.trs.gov.kpi.controller;

import com.trs.gov.kpi.entity.ApiPageData;
import com.trs.gov.kpi.entity.FrequencyPresetResponseDeal;
import com.trs.gov.kpi.entity.Pager;
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
        pageSize = PageInfoDeal.checkPageSizeIsNullOrNot(pageSize);
        pageIndex = PageInfoDeal.checkPageIndexIsNullOrNot(pageIndex);
        pageIndex = PageInfoDeal.dealAndcheckPageIndexIsMinusOrOutOfRang(pageIndex, itemCount);
        int pageCount = PageInfoDeal.getPageCount(itemCount, pageSize);
        List<FrequencyPresetResponseDeal> frequencyPresetResponseDealList = frequencyPresetService.getPageDataBySiteId(siteId, pageIndex, pageSize);
        ApiPageData apiPageData = new ApiPageData();
        Pager pager = new Pager();
        pager.setCurrPage(pageIndex + 1);
        pager.setPageSize(pageSize);
        pager.setPageCount(pageCount);
        pager.setItemCount(itemCount);
        apiPageData.setPager(pager);
        apiPageData.setData(frequencyPresetResponseDealList);
        return apiPageData;
    }
}
