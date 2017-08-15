package com.trs.gov.kpi.controller;

import com.trs.gov.kpi.constant.Authority;
import com.trs.gov.kpi.constant.Constants;
import com.trs.gov.kpi.constant.OperationType;
import com.trs.gov.kpi.constant.Status;
import com.trs.gov.kpi.entity.FrequencySetup;
import com.trs.gov.kpi.entity.exception.BizException;
import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.gov.kpi.entity.requestdata.FrequencySetupSelectRequest;
import com.trs.gov.kpi.entity.requestdata.FrequencySetupSetRequest;
import com.trs.gov.kpi.entity.requestdata.FrequencySetupUpdateRequest;
import com.trs.gov.kpi.entity.responsedata.ApiPageData;
import com.trs.gov.kpi.service.FrequencyPresetService;
import com.trs.gov.kpi.service.FrequencySetupService;
import com.trs.gov.kpi.service.outer.AuthorityService;
import com.trs.gov.kpi.service.outer.SiteApiService;
import com.trs.gov.kpi.utils.LogUtil;
import com.trs.gov.kpi.utils.ParamCheckUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

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

    @Resource
    private AuthorityService authorityService;

    @Resource
    SiteApiService siteApiService;

    private static final String SETREQUEST = "frequencySetupSetRequest";

    private static final String IDS = "ids";

    private static final String IS_OPEN = "isOpen";

    /**
     * 分页查询当前站点的数据（附带模糊查询）
     *
     * @param selectRequest
     * @return
     * @throws BizException
     */
    @RequestMapping(value = "/chnlfreq", method = RequestMethod.GET)
    @ResponseBody
    public ApiPageData getPageDataBySiteId(@ModelAttribute FrequencySetupSelectRequest selectRequest) throws BizException, RemoteException {
        String logDesc = "查询当前站点频率设置" + LogUtil.paramsToLogString("selectRequest", selectRequest);
        return LogUtil.controlleFunctionWrapper(() -> {
            if (selectRequest.getSiteId() == null) {
                log.error("Invalid parameter: 参数siteId存在null值");
                throw new BizException(Constants.INVALID_PARAMETER);
            }
            ParamCheckUtil.pagerCheck(selectRequest.getPageIndex(), selectRequest.getPageSize());
            authorityService.checkRight(Authority.KPIWEB_INDEXSETUP_SEARCH, selectRequest.getSiteId());
            return frequencySetupService.getPageData(selectRequest);
        }, OperationType.QUERY, logDesc, LogUtil.getSiteNameForLog(siteApiService, selectRequest.getSiteId()));

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
    public Object addOrUpdateFrequencySetup(@RequestBody FrequencySetupSetRequest frequencySetupSetRequest) throws BizException, RemoteException {
        String logDesc = "添加更新频率（添加和修改）" + LogUtil.paramsToLogString(SETREQUEST, frequencySetupSetRequest);
        return LogUtil.controlleFunctionWrapper(() -> {
            checkSetupParam(frequencySetupSetRequest);
            authorityService.checkRight(Authority.KPIWEB_INDEXSETUP_ADDMONITORCHNL, frequencySetupSetRequest.getSiteId());
            int siteId = frequencySetupSetRequest.getSiteId();
            int presetFeqId = frequencySetupSetRequest.getPresetFeqId();
            if (!frequencyPresetService.isPresetFeqIdExist(siteId, presetFeqId)) {
                log.error("Invalid parameter: 参数FrequencySetupSetRequest对象中presetFeqId不在数据库表预设记录中");
                throw new BizException(Constants.INVALID_PARAMETER);
            }
            addOrUpdateFrequency(frequencySetupSetRequest);
            return null;
        }, OperationType.ADD + "," + OperationType.UPDATE, logDesc, LogUtil.getSiteNameForLog(siteApiService, frequencySetupSetRequest.getSiteId()));
    }

    private void addOrUpdateFrequency(FrequencySetupSetRequest frequencySetupSetRequest) {
        Integer[] chnlIds = frequencySetupSetRequest.getChnlIds();
        for (int i = 0; i < chnlIds.length; i++) {
            FrequencySetup frequencySetup = frequencySetupService.getFrequencySetupBySiteIdAndChnlId(frequencySetupSetRequest.getSiteId(), chnlIds[i]);
            if (frequencySetup == null) {//当前站点的当前栏目未设置过更新频率，需要新增
                frequencySetup = frequencySetupService.toFrequencySetupBySetupRequest(frequencySetupSetRequest, chnlIds[i]);
                frequencySetupService.insert(frequencySetup);
                LogUtil.addOperationLog(OperationType.ADD, "添加更新频率" + LogUtil.paramsToLogString(SETREQUEST, frequencySetupSetRequest), LogUtil.getSiteNameForLog(siteApiService, frequencySetupSetRequest.getSiteId()));
            } else {//当前站点的当前栏目设置过更新频率，需要修改

                frequencySetup.setPresetFeqId(frequencySetupSetRequest.getPresetFeqId());
                frequencySetupService.updateFrequencySetupById(frequencySetup);
                LogUtil.addOperationLog(OperationType.UPDATE, "修改更新频率" + LogUtil.paramsToLogString(SETREQUEST, frequencySetupSetRequest), LogUtil.getSiteNameForLog(siteApiService, frequencySetupSetRequest.getSiteId()));
            }
        }
    }

    private void checkSetupParam(FrequencySetupSetRequest frequencySetupSetRequest) throws BizException {
        if (frequencySetupSetRequest.getSiteId() == null || frequencySetupSetRequest.getPresetFeqId() == null || frequencySetupSetRequest.getChnlIds() == null || frequencySetupSetRequest
                .getChnlIds().length == 0) {
            log.error("Invalid parameter: 参数FrequencySetupSetRequest对象中siteId、presetFeqId、chilIds[]三个属性中至少有一个存在null值");
            throw new BizException(Constants.INVALID_PARAMETER);
        }
        Integer[] chnlIds = frequencySetupSetRequest.getChnlIds();
        for (int i = 0; i < chnlIds.length; i++) {
            if (chnlIds[i] == null) {
                log.error("Invalid parameter: 参数FrequencySetupSetRequest对象中chilIds[]（栏目id数组）中存在null值");
                throw new BizException(Constants.INVALID_PARAMETER);
            }
        }
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
    public Object pdateFrequencySetup(@ModelAttribute FrequencySetupUpdateRequest frequencySetupUpdateRequest) throws BizException, RemoteException {
        String logDesc = "直接修改更新频率" + LogUtil.paramsToLogString("frequencySetupUpdateRequest", frequencySetupUpdateRequest);
        return LogUtil.controlleFunctionWrapper(() -> {
            if (frequencySetupUpdateRequest.getSiteId() == null || frequencySetupUpdateRequest.getId() == null || frequencySetupUpdateRequest.getPresetFeqId() == null ||
                    frequencySetupUpdateRequest.getChnlId() == null) {
                log.error("Invalid parameter:  参数FrequencySetupSetRequest对象中siteId、presetFeqId、chilIds[]三个属性中至少有一个存在null值");
                throw new BizException(Constants.INVALID_PARAMETER);
            }
            authorityService.checkRight(Authority.KPIWEB_INDEXSETUP_UPDATEMONITORCHNL, frequencySetupUpdateRequest.getSiteId());
            frequencySetupService.updateFrequencySetupById(frequencySetupUpdateRequest);
            return null;

        }, OperationType.UPDATE, logDesc, LogUtil.getSiteNameForLog(siteApiService, frequencySetupUpdateRequest.getSiteId()));
    }

    /**
     * 删除更新频率记录
     *
     * @param siteId
     * @param ids
     * @return
     * @throws BizException
     */
    @RequestMapping(value = "/chnlfreq", method = RequestMethod.DELETE)
    @ResponseBody
    public Object deleteFrequencySetupBySiteIdAndId(@RequestParam("siteId") Integer siteId, @RequestParam("ids") Integer[] ids) throws BizException, RemoteException {
        String logDesc = "删除更新频率" + LogUtil.paramsToLogString(Constants.DB_FIELD_SITE_ID, siteId, IDS, ids);
        return LogUtil.controlleFunctionWrapper(() -> {
            if (siteId == null || ids == null) {
                log.error("Invalid parameter: 参数siteId或者数组ids（设置的更新频率记录对应id数组）存在null值");
                throw new BizException(Constants.INVALID_PARAMETER);
            }
            for (Integer id : ids) {
                if (id == null) {
                    log.error("Invalid parameter: 参数数组ids（设置的更新频率记录对应id数组）中存在某一或者多个id为null值");
                    throw new BizException(Constants.INVALID_PARAMETER);
                }
            }
            authorityService.checkRight(Authority.KPIWEB_INDEXSETUP_DELMONITORCHNL, siteId);

            for (Integer id : ids) {
                frequencySetupService.deleteFrequencySetupBySiteIdAndId(siteId, id);
            }
            return null;

        }, OperationType.DELETE, logDesc, LogUtil.getSiteNameForLog(siteApiService, siteId));
    }

    /**
     * 关闭或者生效更新频率记录
     *
     * @param siteId
     * @param ids
     * @return
     * @throws BizException
     */
    @RequestMapping(value = "/chnlfreq/open", method = RequestMethod.PUT)
    @ResponseBody
    public Object closeOrOpen(@RequestParam("siteId") Integer siteId, @RequestParam("ids") Integer[] ids, @RequestParam("isOpen") Integer isOpen) throws BizException, RemoteException {
        String logDesc = "打开/关闭更新频率" + LogUtil.paramsToLogString(Constants.DB_FIELD_SITE_ID, siteId, IDS, ids, IS_OPEN, isOpen);
        return LogUtil.controlleFunctionWrapper(() -> {
            checkCloseOrOpenParam(siteId, ids, isOpen);
            authorityService.checkRight(Authority.KPIWEB_INDEXSETUP_ENABLEDMONITORCHNL, siteId);
            if (isOpen == Status.Open.OPEN.value) {
                open(siteId, ids, isOpen);
            } else if (isOpen == Status.Open.CLOSE.value) {
                close(siteId, ids, isOpen);
            } else {
                log.error("Invalid parameter: 参数isOpen不存在对应数值的请求");
                throw new BizException(Constants.INVALID_PARAMETER);
            }
            return null;
        }, OperationType.ADD + "," + OperationType.UPDATE, logDesc, LogUtil.getSiteNameForLog(siteApiService, siteId));
    }

    private Integer open(Integer siteId, Integer[] ids, Integer isOpen) throws RemoteException, BizException {
        String logDesc = "打开更新频率" + LogUtil.paramsToLogString(Constants.DB_FIELD_SITE_ID, siteId, IDS, ids, IS_OPEN, isOpen);
        return LogUtil.controlleFunctionWrapper(() -> {
            frequencySetupService.closeOrOpen(siteId, ids, isOpen);
            return null;
        }, OperationType.UPDATE, logDesc, LogUtil.getSiteNameForLog(siteApiService, siteId));
    }

    private Integer close(Integer siteId, Integer[] ids, Integer isOpen) throws RemoteException, BizException {
        String logDesc = "关闭更新频率" + LogUtil.paramsToLogString(Constants.DB_FIELD_SITE_ID, siteId, IDS, ids, IS_OPEN, isOpen);
        return LogUtil.controlleFunctionWrapper(() -> {
            frequencySetupService.closeOrOpen(siteId, ids, isOpen);
            return null;
        }, OperationType.UPDATE, logDesc, LogUtil.getSiteNameForLog(siteApiService, siteId));
    }

    private void checkCloseOrOpenParam(Integer siteId, Integer[] ids, Integer isOpen) throws BizException {
        if (siteId == null || ids == null || isOpen == null) {
            log.error("Invalid parameter: 参数siteId、数组ids（设置的更新频率记录对应id数组）、isOpen(关闭/开启请求标识)存在null值");
            throw new BizException(Constants.INVALID_PARAMETER);
        }
        for (Integer id : ids) {
            if (id == null) {
                log.error("Invalid parameter: 参数数组ids（设置的更新频率记录对应id数组）中存在某一或者多个id为null值");
                throw new BizException(Constants.INVALID_PARAMETER);
            } else {
                if (!frequencySetupService.isIdExist(siteId, id)) {
                    log.error("Invalid parameter: 参数数组ids（设置的更新频率记录对应id数组）中存在id不是数据库表指定站点中对应某一记录的id");
                    throw new BizException(Constants.INVALID_PARAMETER);
                }
            }
        }
    }


}
