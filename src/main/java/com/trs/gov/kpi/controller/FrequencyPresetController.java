package com.trs.gov.kpi.controller;

import com.trs.gov.kpi.constant.Constants;
import com.trs.gov.kpi.entity.FrequencyPreset;
import com.trs.gov.kpi.entity.exception.BizException;
import com.trs.gov.kpi.entity.requestdata.FrequencyPresetRequest;
import com.trs.gov.kpi.entity.responsedata.ApiPageData;
import com.trs.gov.kpi.entity.responsedata.FrequencyPresetResponse;
import com.trs.gov.kpi.service.FrequencyPresetService;
import com.trs.gov.kpi.service.FrequencySetupService;
import com.trs.gov.kpi.utils.PageInfoDeal;
import com.trs.gov.kpi.utils.ParamCheckUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 更新频率及预警初设Controller
 * Created by he.lang on 2017/5/15.
 */
@Slf4j
@RestController
@RequestMapping("/gov/kpi/setting")
public class FrequencyPresetController {
    @Resource
    FrequencyPresetService frequencyPresetService;

    @Resource
    FrequencySetupService frequencySetupService;

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
            log.error("Invalid parameter:  参数siteId存在null值");
            throw new BizException(Constants.INVALID_PARAMETER);
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
            log.error("Invalid parameter:  参数siteId、updateFreq（更新频率）、alertFreq（预警频率）中至少一个存在null值");
            throw new BizException(Constants.INVALID_PARAMETER);
        }
        if(request.getAlertFreq() > request.getUpdateFreq()){
            log.error("Invalid parameter:  参数alertFreq（预警频率)大于updateFreq（更新频率）");
            throw new BizException(Constants.INVALID_PARAMETER);
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
            log.error("Invalid parameter:  参数siteId、id（预设记录编号）、updateFreq（更新频率）、alertFreq（预警频率）中至少一个存在null值");
            throw new BizException(Constants.INVALID_PARAMETER);
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
            log.error("Invalid parameter:  参数siteId或者id（预设记录编号）存在null值");
            throw new BizException(Constants.INVALID_PARAMETER);
        }
        if(frequencySetupService.checkPresetFeqIsUsedOrNot(siteId, id)){
            frequencyPresetService.deleteFrequencyPresetBySiteIdAndId(siteId, id);
        }else{
            log.error("Invalid parameter:  当前参数siteId和id下的记录已经被某一栏目所使用，无法删除");
            throw new BizException("当前预设记录已经被某一栏目所使用，无法删除");
        }
        return null;
    }
}
