package com.trs.gov.kpi.controller;

import com.trs.gov.kpi.constant.Constants;
import com.trs.gov.kpi.entity.FrequencySetup;
import com.trs.gov.kpi.entity.exception.BizException;
import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.gov.kpi.entity.requestdata.FrequencySetupSetRequest;
import com.trs.gov.kpi.entity.requestdata.FrequencySetupUpdateRequest;
import com.trs.gov.kpi.entity.responsedata.ApiPageData;
import com.trs.gov.kpi.entity.responsedata.FrequencySetupResponse;
import com.trs.gov.kpi.service.FrequencyPresetService;
import com.trs.gov.kpi.service.FrequencySetupService;
import com.trs.gov.kpi.utils.PageInfoDeal;
import com.trs.gov.kpi.utils.ParamCheckUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.List;

/**
 * 栏目更新频率Controller
 * Created by he.lang on 2017/5/16.
 */
@Slf4j
@RestController
@RequestMapping("/gov/kpi/setting")
public class FrequencySetupController {
    @Resource
    FrequencySetupService frequencySetupService;

    @Resource
    FrequencyPresetService frequencyPresetService;

    /**
     * 分页查询当前站点的数数据
     *
     * @param siteId
     * @param pageSize
     * @param pageIndex
     * @return
     * @throws BizException
     */
    @RequestMapping(value = "/chnlfreq", method = RequestMethod.GET)
    @ResponseBody
    public ApiPageData getPageDataBySiteId(@RequestParam("siteId") Integer siteId, Integer pageSize, Integer pageIndex) throws BizException, RemoteException {
        if (siteId == null) {
            log.error("Invalid parameter: 参数siteId存在null值");
            throw new BizException(Constants.INVALID_PARAMETER);
        }
        ParamCheckUtil.pagerCheck(pageIndex, pageSize);
        int itemCount = frequencySetupService.getCountFrequencySetupBySite(siteId);
        ApiPageData apiPageData = PageInfoDeal.buildApiPageData(pageIndex, pageSize, itemCount);
        List<FrequencySetupResponse> frequencySetupResponses = frequencySetupService.getPageDataFrequencySetupList(siteId, apiPageData.getPager().getCurrPage() - 1, apiPageData.getPager().getPageSize());
        apiPageData.setData(frequencySetupResponses);
        return apiPageData;
    }

    /**
     * 添加更新频率（特殊：存在插入和修改操作）
     *
     * @param frequencySetupSetRequest
     * @return
     * @throws BizException
     */
    @RequestMapping(value = "/chnlfreq", method = RequestMethod.POST)
    @ResponseBody
    public Object addOrUpdateFrequencySetup(@RequestBody FrequencySetupSetRequest frequencySetupSetRequest) throws BizException, ParseException {
        if (frequencySetupSetRequest.getSiteId() == null || frequencySetupSetRequest.getPresetFeqId() == null || frequencySetupSetRequest.getChnlIds() == null || frequencySetupSetRequest.getChnlIds().length == 0) {
            log.error("Invalid parameter: 参数FrequencySetupSetRequest对象中siteId、presetFeqId、chilIds[]三个属性中至少有一个存在null值");
            throw new BizException(Constants.INVALID_PARAMETER);
        }
        Integer[] chnlIds = frequencySetupSetRequest.getChnlIds();
        for (int i = 0; i < chnlIds.length; i++) {
            if(chnlIds[i] == null){
                log.error("Invalid parameter: 参数FrequencySetupSetRequest对象中chilIds[]（栏目id数组）中存在null值");
                throw new BizException(Constants.INVALID_PARAMETER);
            }
        }
        int siteId = frequencySetupSetRequest.getSiteId();
        int presetFeqId = frequencySetupSetRequest.getPresetFeqId();
        if(!frequencyPresetService.IsPresetFeqIdExist(siteId, presetFeqId)){
            log.error("Invalid parameter: 参数FrequencySetupSetRequest对象中presetFeqId不在数据库表预设记录中");
            throw new BizException(Constants.INVALID_PARAMETER);
        }
        for (int i = 0; i < chnlIds.length; i++) {
            FrequencySetup frequencySetup = frequencySetupService.getFrequencySetupBySiteIdAndChnlId(siteId, chnlIds[i]);
            if (frequencySetup == null) {//当前站点的当前栏目未设置过更新频率，需要新增
                frequencySetup = frequencySetupService.toFrequencySetupBySetupRequest(frequencySetupSetRequest, chnlIds[i]);
                frequencySetupService.insert(frequencySetup);
            } else {//当前站点的当前栏目设置过更新频率，需要修改
                frequencySetup.setPresetFeqId(frequencySetupSetRequest.getPresetFeqId());
                frequencySetupService.updateFrequencySetupById(frequencySetup);
            }
        }
        return null;
    }

    /**
     * 修改更新频率记录
     *
     * @param frequencySetupUpdateRequest
     * @return
     * @throws BizException
     */
    @RequestMapping(value = "/chnlfreq", method = RequestMethod.PUT)
    @ResponseBody
    public Object pdateFrequencySetup(@ModelAttribute FrequencySetupUpdateRequest frequencySetupUpdateRequest) throws BizException {
        if (frequencySetupUpdateRequest.getSiteId() == null || frequencySetupUpdateRequest.getId() == null || frequencySetupUpdateRequest.getPresetFeqId() == null || frequencySetupUpdateRequest.getChnlId() == null) {
            log.error("Invalid parameter:  参数FrequencySetupSetRequest对象中siteId、presetFeqId、chilIds[]三个属性中至少有一个存在null值");
            throw new BizException(Constants.INVALID_PARAMETER);
        }
        frequencySetupService.updateFrequencySetupById(frequencySetupUpdateRequest);
        return null;
    }

    /**
     * 删除更新频率记录
     *
     * @param siteId
     * @param id
     * @return
     * @throws BizException
     */
    @RequestMapping(value = "/chnlfreq", method = RequestMethod.DELETE)
    @ResponseBody
    public Object deleteFrequencySetupBySiteIdAndId(@RequestParam("siteId") Integer siteId, @RequestParam("id") Integer id) throws BizException {
        if (siteId == null || id == null) {
            log.error("Invalid parameter: 参数siteId或者id（设置的更新频率记录对应id）存在null值");
            throw new BizException(Constants.INVALID_PARAMETER);
        }
        frequencySetupService.deleteFrequencySetupBySiteIdAndId(siteId, id);
        return null;
    }


}
