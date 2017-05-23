package com.trs.gov.kpi.controller;

import com.trs.gov.kpi.entity.FrequencySetup;
import com.trs.gov.kpi.entity.exception.BizException;
import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.gov.kpi.entity.requestdata.FrequencySetupSetRequestDetail;
import com.trs.gov.kpi.entity.requestdata.FrequencySetupUpdateRequestDetail;
import com.trs.gov.kpi.entity.responsedata.ApiPageData;
import com.trs.gov.kpi.entity.responsedata.FrequencySetupResponseDetail;
import com.trs.gov.kpi.service.FrequencySetupService;
import com.trs.gov.kpi.utils.PageInfoDeal;
import com.trs.gov.kpi.utils.ParamCheckUtil;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.text.ParseException;
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
            throw new BizException("站点编号不能为null值");
        }
        ParamCheckUtil.pagerCheck(pageIndex, pageSize);
        int itemCount = frequencySetupService.getCountFrequencySetupBySite(siteId);
        ApiPageData apiPageData = PageInfoDeal.buildApiPageData(pageIndex, pageSize, itemCount);
        List<FrequencySetupResponseDetail> frequencySetupResponseDetails = frequencySetupService.getPageDataFrequencySetupList(siteId, apiPageData.getPager().getCurrPage() - 1, apiPageData.getPager().getPageSize());
        apiPageData.setData(frequencySetupResponseDetails);
        return apiPageData;
    }

    /**
     * 添加更新频率（特殊：存在插入和修改操作）
     *
     * @param frequencySetupSetRequestDetail
     * @return
     * @throws BizException
     */
    @RequestMapping(value = "/chnlfreq", method = RequestMethod.POST)
    @ResponseBody
    public Object addOrUpdateFrequencySetup(@RequestBody FrequencySetupSetRequestDetail frequencySetupSetRequestDetail) throws BizException, ParseException {
        if (frequencySetupSetRequestDetail.getSiteId() == null || frequencySetupSetRequestDetail.getPresetFeqId() == null || frequencySetupSetRequestDetail.getChnlIds() == null || frequencySetupSetRequestDetail.getChnlIds().length == 0) {
            throw new BizException("参数存在null值");
        }
        Integer[] chnlIds = frequencySetupSetRequestDetail.getChnlIds();
        for (int i = 0; i < chnlIds.length; i++) {
            if(chnlIds[i] == null){
                throw new BizException("参数chnlIds[]中存在null值");
            }
        }
        int siteId = frequencySetupSetRequestDetail.getSiteId();
        for (int i = 0; i < chnlIds.length; i++) {
            FrequencySetup frequencySetup = frequencySetupService.getFrequencySetupBySiteIdAndChnlId(siteId, chnlIds[i]);
            if (frequencySetup == null) {//当前站点的当前栏目未设置过更新频率，需要新增
                frequencySetup = frequencySetupService.getFrequencySetupByFrequencySetupSetRequestDetail(frequencySetupSetRequestDetail, chnlIds[i]);
                frequencySetupService.insert(frequencySetup);
            } else {//当前站点的当前栏目设置过更新频率，需要修改
                frequencySetup.setPresetFeqId(frequencySetupSetRequestDetail.getPresetFeqId());
                frequencySetupService.updateFrequencySetupById(frequencySetup);
            }
        }
        return null;
    }

    /**
     * 修改更新频率记录
     *
     * @param frequencySetupUpdateRequestDetail
     * @return
     * @throws BizException
     */
    @RequestMapping(value = "/chnlfreq", method = RequestMethod.PUT)
    @ResponseBody
    public Object pdateFrequencySetup(@ModelAttribute FrequencySetupUpdateRequestDetail frequencySetupUpdateRequestDetail) throws BizException {
        if (frequencySetupUpdateRequestDetail.getSiteId() == null || frequencySetupUpdateRequestDetail.getId() == null || frequencySetupUpdateRequestDetail.getPresetFeqId() == null || frequencySetupUpdateRequestDetail.getChnlId() == null) {
            throw new BizException("参数存在null值");
        }
        frequencySetupService.updateFrequencySetupById(frequencySetupUpdateRequestDetail);
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
            throw new BizException("参数存在null值");
        }
        frequencySetupService.deleteFrequencySetupBySiteIdAndId(siteId, id);
        return null;
    }


}
