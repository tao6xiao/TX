package com.trs.gov.kpi.controller;

import com.trs.gov.kpi.entity.FrequencySetup;
import com.trs.gov.kpi.entity.exception.BizException;
import com.trs.gov.kpi.entity.requestdata.FrequencySetupSetRequestDetail;
import com.trs.gov.kpi.entity.responsedata.ApiPageData;
import com.trs.gov.kpi.entity.responsedata.FrequencySetupResponseDetail;
import com.trs.gov.kpi.service.FrequencySetupService;
import com.trs.gov.kpi.utils.PageInfoDeal;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 栏目更新频率Controller
 * Created by he.lang on 2017/5/16.
 */
@RestController
@RequestMapping("/gov/kpi/setting")
public class FrequencySetupController {
    @Resource
    FrequencySetupService frequencySetupService;

    /**
     * 分页查询当前站点的数数据
     * @param siteId
     * @param pageSize
     * @param pageIndex
     * @return
     * @throws BizException
     */
    @RequestMapping(value = "/chnlfreq", method = RequestMethod.GET)
    @ResponseBody
    public ApiPageData getPageDataBySiteId(@RequestParam("siteId") Integer siteId, @RequestParam("pageSize") Integer pageSize, @RequestParam("pageIndex") Integer pageIndex) throws BizException {
        if (siteId == null) {
            throw new BizException("站点编号不能为null值");
        }
        int itemCount = frequencySetupService.getCountFrequencySetupBySite(siteId);
        ApiPageData apiPageData = PageInfoDeal.getApiPageData(pageIndex, pageSize, itemCount);
        List<FrequencySetupResponseDetail> frequencySetupResponseDetails = frequencySetupService.getPageDataFrequencySetupList(siteId, apiPageData.getPager().getCurrPage() - 1, apiPageData.getPager().getPageSize());
        apiPageData.setData(frequencySetupResponseDetails);
        return apiPageData;
    }

    /**
     * 添加更新频率（特殊：存在插入和修改操作）
     * @param frequencySetupSetRequestDetail
     * @return
     * @throws BizException
     */
    @RequestMapping(value = "/chnlfreq", method = RequestMethod.POST)
    @ResponseBody
    public Object addOrUpdateFrequencySetup(@RequestBody FrequencySetupSetRequestDetail frequencySetupSetRequestDetail) throws BizException {
        if (frequencySetupSetRequestDetail.getSiteId() == null || frequencySetupSetRequestDetail.getPresetFeqId() == null || frequencySetupSetRequestDetail.getChnlIds() == null || frequencySetupSetRequestDetail.getChnlIds().length == 0) {
            throw new BizException("参数存在null值");
        }
        Integer[] chnlIds = frequencySetupSetRequestDetail.getChnlIds();
        int siteId = frequencySetupSetRequestDetail.getSiteId();
        for (int i = 0; i < chnlIds.length; i++){
            FrequencySetup frequencySetup = frequencySetupService.getFrequencySetupBySiteIdAndChnlId(siteId, chnlIds[i]);
            int num = 0;
            if(frequencySetup == null){//当前站点的当前栏目未设置过更新频率，需要新增
                frequencySetup = frequencySetupService.getFrequencySetupByFrequencySetupSetRequestDetail(frequencySetupSetRequestDetail, chnlIds[i]);
                num = frequencySetupService.insert(frequencySetup);
            }else{//当前站点的当前栏目设置过更新频率，需要修改
                frequencySetup.setPresetFeqId(frequencySetupSetRequestDetail.getPresetFeqId());
                num = frequencySetupService.updateFrequencySetupById(frequencySetup);
            }
        }
        return null;
    }

    /**
     * 修改更新频率
     * @param frequencySetupSetRequestDetail
     * @return
     * @throws BizException
     */
    @RequestMapping(value = "/chnlfreq", method = RequestMethod.POST)
    @ResponseBody
    public Object UpdateFrequencySetup(@RequestBody FrequencySetupSetRequestDetail frequencySetupSetRequestDetail) throws BizException {
        if (frequencySetupSetRequestDetail.getSiteId() == null || frequencySetupSetRequestDetail.getPresetFeqId() == null || frequencySetupSetRequestDetail.getChnlIds() == null || frequencySetupSetRequestDetail.getChnlIds().length == 0) {
            throw new BizException("参数存在null值");
        }
        Integer[] chnlIds = frequencySetupSetRequestDetail.getChnlIds();
        int siteId = frequencySetupSetRequestDetail.getSiteId();
        for (int i = 0; i < chnlIds.length; i++){
            FrequencySetup frequencySetup = frequencySetupService.getFrequencySetupBySiteIdAndChnlId(siteId, chnlIds[i]);
            int num = 0;
            if(frequencySetup == null){//当前站点的当前栏目未设置过更新频率，需要新增
                frequencySetup = frequencySetupService.getFrequencySetupByFrequencySetupSetRequestDetail(frequencySetupSetRequestDetail, chnlIds[i]);
                num = frequencySetupService.insert(frequencySetup);
            }else{//当前站点的当前栏目设置过更新频率，需要修改
                frequencySetup.setPresetFeqId(frequencySetupSetRequestDetail.getPresetFeqId());
                num = frequencySetupService.updateFrequencySetupById(frequencySetup);
            }
        }
        return null;
    }

}
