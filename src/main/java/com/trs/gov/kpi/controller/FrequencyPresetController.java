package com.trs.gov.kpi.controller;

import com.trs.gov.kpi.entity.FrequencyPreset;
import com.trs.gov.kpi.entity.exception.BizException;
import com.trs.gov.kpi.entity.requestdata.FrequencyPresetRequest;
import com.trs.gov.kpi.entity.responsedata.ApiPageData;
import com.trs.gov.kpi.entity.responsedata.FrequencyPresetResponse;
import com.trs.gov.kpi.service.FrequencyPresetService;
import com.trs.gov.kpi.utils.PageInfoDeal;
import com.trs.gov.kpi.utils.ParamCheckUtil;
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

    /**
     * 获取更新频率及预警初设的分页数据
     *
     * @param siteId
     * @param pageSize
     * @param pageIndex
     * @return
     * @throws BizException
     */
    @RequestMapping(value = "/presetfreq", method = RequestMethod.GET)
    @ResponseBody
    public ApiPageData getPageDataBySiteId(@RequestParam("siteId") Integer siteId, Integer pageSize, Integer pageIndex) throws BizException {
        if (siteId == null) {
            throw new BizException("参数不合法！");
        }
        ParamCheckUtil.pagerCheck(pageIndex, pageSize);
        int itemCount = frequencyPresetService.getItemCountBySiteId(siteId);
        ApiPageData apiPageData = PageInfoDeal.buildApiPageData(pageIndex, pageSize, itemCount);
        List<FrequencyPresetResponse> responseList = frequencyPresetService.getPageDataBySiteId(
                siteId, apiPageData.getPager().getCurrPage() - 1, apiPageData.getPager().getPageSize());
        apiPageData.setData(responseList);
        return apiPageData;
    }

    /**
     * 添加预设记录
     *
     * @param request
     * @return
     * @throws BizException
     */
    @RequestMapping(value = "/presetfreq", method = RequestMethod.POST)
    @ResponseBody
    public Object addFrequencyPreset(@ModelAttribute FrequencyPresetRequest request) throws BizException {
        if (request.getSiteId() == null || request.getUpdateFreq() == null || request.getAlertFreq() == null) {
            throw new BizException("参数不合法！");
        }
        frequencyPresetService.addFrequencyPreset(request);
        return null;
    }

    /**
     * 通过siteId和id修改预设记录
     *
     * @param preset
     * @return
     * @throws BizException
     */
    @RequestMapping(value = "/presetfreq", method = RequestMethod.PUT)
    @ResponseBody
    public Object updateFrequencyPresetBySiteIdAndId(@ModelAttribute FrequencyPreset preset) throws BizException {
        if (preset.getSiteId() == null || preset.getId() == null
                || preset.getUpdateFreq() == null || preset.getAlertFreq() == null) {
            throw new BizException("参数不合法！");
        }
        frequencyPresetService.updateFrequencyPresetBySiteIdAndId(preset);
        return null;
    }

    /**
     * 通过siteId和id删除预设记录
     *
     * @param siteId
     * @param id
     * @return
     * @throws BizException
     */
    @RequestMapping(value = "/presetfreq", method = RequestMethod.DELETE)
    @ResponseBody
    public Object deleteFrequencyPreset(@RequestParam("siteId") Integer siteId, @RequestParam("id") Integer id) throws BizException {
        if (siteId == null || id == null) {
            throw new BizException("参数不合法！");
        }
        frequencyPresetService.deleteFrequencyPresetBySiteIdAndId(siteId, id);
        return null;
    }
}
