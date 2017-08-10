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
import java.text.ParseException;
import java.util.Date;

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
        Date startTime = new Date();
        if (selectRequest.getSiteId() == null) {
            log.error("Invalid parameter: 参数siteId存在null值");
            throw new BizException(Constants.INVALID_PARAMETER);
        }
        ParamCheckUtil.pagerCheck(selectRequest.getPageIndex(), selectRequest.getPageSize());
        String logDesc = "查询当前站点频率设置";
        authorityService.checkRight(Authority.KPIWEB_INDEXSETUP_SEARCH, selectRequest.getSiteId());
        try {
            ApiPageData apiPageData = frequencySetupService.getPageData(selectRequest);
            Date endTime = new Date();
            LogUtil.addOperationLog(OperationType.QUERY, logDesc, LogUtil.getSiteNameForLog(siteApiService, selectRequest.getSiteId()));
            LogUtil.addElapseLog(OperationType.QUERY, LogUtil.buildElapseLogDesc(siteApiService, selectRequest.getSiteId(), logDesc), endTime.getTime() - startTime.getTime());
            return apiPageData;
        } catch (Exception e) {
            LogUtil.addOperationLog(OperationType.QUERY, LogUtil.buildFailOperationLogDesc(logDesc), LogUtil.getSiteNameForLog(siteApiService, selectRequest.getSiteId()));
            throw e;
        }

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
    public Object addOrUpdateFrequencySetup(@RequestBody FrequencySetupSetRequest frequencySetupSetRequest) throws BizException, ParseException, RemoteException {
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
        String logDesc = "添加更新频率（添加和修改）";
        authorityService.checkRight(Authority.KPIWEB_INDEXSETUP_ADDMONITORCHNL, frequencySetupSetRequest.getSiteId());
        int siteId = frequencySetupSetRequest.getSiteId();
        int presetFeqId = frequencySetupSetRequest.getPresetFeqId();
        boolean state;
        try {
            state = frequencyPresetService.isPresetFeqIdExist(siteId, presetFeqId);
        } catch (Exception e) {
            LogUtil.addOperationLog(OperationType.QUERY, LogUtil.buildFailOperationLogDesc(logDesc + "：查询id[" + presetFeqId + "]所对应栏目更新频率预设记录是否存在"), LogUtil.getSiteNameForLog
                    (siteApiService, frequencySetupSetRequest.getSiteId()));
            throw e;
        }
        if (!state) {
            log.error("Invalid parameter: 参数FrequencySetupSetRequest对象中presetFeqId不在数据库表预设记录中");
            throw new BizException(Constants.INVALID_PARAMETER);
        }
        for (int i = 0; i < chnlIds.length; i++) {
            FrequencySetup frequencySetup;
            try {
                frequencySetup = frequencySetupService.getFrequencySetupBySiteIdAndChnlId(siteId, chnlIds[i]);
            } catch (Exception e) {
                LogUtil.addOperationLog(OperationType.QUERY, LogUtil.buildFailOperationLogDesc(logDesc + "：查询站点site[" + siteId + "]下chnlId[" + chnlIds[i] + "]所对应栏目更新频率设置记录是否存在"), LogUtil
                        .getSiteNameForLog(siteApiService, frequencySetupSetRequest.getSiteId()));
                throw e;
            }

            if (frequencySetup == null) {//当前站点的当前栏目未设置过更新频率，需要新增
                try {
                    frequencySetup = frequencySetupService.toFrequencySetupBySetupRequest(frequencySetupSetRequest, chnlIds[i]);
                    frequencySetupService.insert(frequencySetup);
                    LogUtil.addOperationLog(OperationType.ADD, "添加更新频率", LogUtil.getSiteNameForLog(siteApiService, siteId));
                } catch (Exception e) {
                    LogUtil.addOperationLog(OperationType.ADD, LogUtil.buildFailOperationLogDesc("添加更新频率"), LogUtil.getSiteNameForLog(siteApiService, frequencySetupSetRequest.getSiteId()));
                    throw e;
                }
            } else {//当前站点的当前栏目设置过更新频率，需要修改
                try {
                    frequencySetup.setPresetFeqId(frequencySetupSetRequest.getPresetFeqId());
                    frequencySetupService.updateFrequencySetupById(frequencySetup);
                    LogUtil.addOperationLog(OperationType.UPDATE, "添加更新频率时，当前更新频率存在，那么修改这个更新频率", LogUtil.getSiteNameForLog(siteApiService, siteId));
                } catch (Exception e) {
                    LogUtil.addOperationLog(OperationType.UPDATE, LogUtil.buildFailOperationLogDesc("添加更新频率时，当前更新频率存在，那么修改这个更新频率"), LogUtil.getSiteNameForLog(siteApiService,
                            frequencySetupSetRequest.getSiteId()));
                    throw e;
                }
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
    public Object pdateFrequencySetup(@ModelAttribute FrequencySetupUpdateRequest frequencySetupUpdateRequest) throws BizException, RemoteException {
        if (frequencySetupUpdateRequest.getSiteId() == null || frequencySetupUpdateRequest.getId() == null || frequencySetupUpdateRequest.getPresetFeqId() == null ||
                frequencySetupUpdateRequest.getChnlId() == null) {
            log.error("Invalid parameter:  参数FrequencySetupSetRequest对象中siteId、presetFeqId、chilIds[]三个属性中至少有一个存在null值");
            throw new BizException(Constants.INVALID_PARAMETER);
        }
        String logDesc = "直接修改更新频率";
        authorityService.checkRight(Authority.KPIWEB_INDEXSETUP_UPDATEMONITORCHNL, frequencySetupUpdateRequest.getSiteId());
        try {
            frequencySetupService.updateFrequencySetupById(frequencySetupUpdateRequest);
            LogUtil.addOperationLog(OperationType.UPDATE, logDesc, LogUtil.getSiteNameForLog(siteApiService, frequencySetupUpdateRequest.getSiteId()));
            return null;
        } catch (Exception e) {
            LogUtil.addOperationLog(OperationType.UPDATE, LogUtil.buildFailOperationLogDesc(logDesc), LogUtil.getSiteNameForLog(siteApiService, frequencySetupUpdateRequest.getSiteId()));
            throw e;
        }
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
        String logDesc = "删除更新频率";
        authorityService.checkRight(Authority.KPIWEB_INDEXSETUP_DELMONITORCHNL, siteId);
        try {
            for (Integer id : ids) {
                frequencySetupService.deleteFrequencySetupBySiteIdAndId(siteId, id);
            }
            LogUtil.addOperationLog(OperationType.DELETE, logDesc, LogUtil.getSiteNameForLog(siteApiService, siteId));
            return null;
        } catch (Exception e) {
            LogUtil.addOperationLog(OperationType.DELETE, LogUtil.buildFailOperationLogDesc(logDesc), LogUtil.getSiteNameForLog(siteApiService, siteId));
            throw e;
        }
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
        if (siteId == null || ids == null || isOpen == null) {
            log.error("Invalid parameter: 参数siteId、数组ids（设置的更新频率记录对应id数组）、isOpen(关闭/开启请求标识)存在null值");
            throw new BizException(Constants.INVALID_PARAMETER);
        }
        for (Integer id : ids) {
            if (id == null) {
                log.error("Invalid parameter: 参数数组ids（设置的更新频率记录对应id数组）中存在某一或者多个id为null值");
                throw new BizException(Constants.INVALID_PARAMETER);
            } else {
                boolean state;
                try {
                    state = frequencySetupService.isIdExist(siteId, id);
                } catch (Exception e) {
                    LogUtil.addOperationLog(OperationType.DELETE, LogUtil.buildFailOperationLogDesc("关闭或者生效更新频率记录：查询id[" + id + "]对应栏目更新频率记录是否存在"), LogUtil.getSiteNameForLog
                            (siteApiService, siteId));
                    throw e;
                }
                if (!state) {
                    log.error("Invalid parameter: 参数数组ids（设置的更新频率记录对应id数组）中存在id不是数据库表指定站点中对应某一记录的id");
                    throw new BizException(Constants.INVALID_PARAMETER);
                }
            }
        }
        authorityService.checkRight(Authority.KPIWEB_INDEXSETUP_ENABLEDMONITORCHNL, siteId);
        if (isOpen == Status.Open.OPEN.value) {
            try {
                frequencySetupService.closeOrOpen(siteId, ids, isOpen);
                LogUtil.addOperationLog(OperationType.UPDATE, "生效更新频率记录", LogUtil.getSiteNameForLog(siteApiService, siteId));
            } catch (Exception e) {
                LogUtil.addOperationLog(OperationType.UPDATE, LogUtil.buildFailOperationLogDesc("生效更新频率记录"), LogUtil.getSiteNameForLog(siteApiService, siteId));
                throw e;
            }
        } else if (isOpen == Status.Open.CLOSE.value) {
            try {
                frequencySetupService.closeOrOpen(siteId, ids, isOpen);
                LogUtil.addOperationLog(OperationType.UPDATE, "关闭更新频率记录", LogUtil.getSiteNameForLog(siteApiService, siteId));
            } catch (Exception e) {
                LogUtil.addOperationLog(OperationType.UPDATE, LogUtil.buildFailOperationLogDesc("关闭更新频率记录"), LogUtil.getSiteNameForLog(siteApiService, siteId));
                throw e;
            }
        } else {
            log.error("Invalid parameter: 参数isOpen不存在对应数值的请求");
            throw new BizException(Constants.INVALID_PARAMETER);
        }
        return null;
    }


}
