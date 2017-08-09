package com.trs.gov.kpi.controller;

import com.trs.gov.kpi.constant.Authority;
import com.trs.gov.kpi.constant.Constants;
import com.trs.gov.kpi.constant.ErrorType;
import com.trs.gov.kpi.constant.OperationType;
import com.trs.gov.kpi.entity.FrequencyPreset;
import com.trs.gov.kpi.entity.exception.BizException;
import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.gov.kpi.entity.requestdata.FrequencyPresetRequest;
import com.trs.gov.kpi.entity.responsedata.ApiPageData;
import com.trs.gov.kpi.entity.responsedata.FrequencyPresetResponse;
import com.trs.gov.kpi.entity.responsedata.Pager;
import com.trs.gov.kpi.ids.ContextHelper;
import com.trs.gov.kpi.service.FrequencyPresetService;
import com.trs.gov.kpi.service.FrequencySetupService;
import com.trs.gov.kpi.service.outer.AuthorityService;
import com.trs.gov.kpi.service.outer.SiteApiService;
import com.trs.gov.kpi.utils.LogUtil;
import com.trs.gov.kpi.utils.PageInfoDeal;
import com.trs.gov.kpi.utils.ParamCheckUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
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

    @Resource
    private AuthorityService authorityService;

    @Resource
    SiteApiService siteApiService;

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
    public ApiPageData getPageDataBySiteId(@RequestParam("siteId") Integer siteId, Integer pageSize, Integer pageIndex) throws BizException, RemoteException {
        Date startTime = new Date();
        if (siteId == null) {
            log.error("Invalid parameter:  参数siteId存在null值");
            throw new BizException(Constants.INVALID_PARAMETER);
        }
        ParamCheckUtil.pagerCheck(pageIndex, pageSize);
        String logDesc = "查询更新频率及预警初设数据";
        if (!authorityService.hasRight(ContextHelper.getLoginUser().getUserName(), siteId, null, Authority.KPIWEB_INDEXSETUP_SEARCH) && !authorityService.hasRight(ContextHelper
                .getLoginUser().getUserName(), null, null, Authority.KPIWEB_INDEXSETUP_SEARCH)) {
            LogUtil.addOperationLog(OperationType.QUERY, LogUtil.buildFailOperationLogDesc(logDesc), LogUtil.getSiteNameForLog(siteApiService, siteId));
            throw new BizException(Authority.NO_AUTHORITY);
        }
        try {
            int itemCount = frequencyPresetService.getItemCountBySiteId(siteId);
            Pager pager = PageInfoDeal.buildResponsePager(pageIndex, pageSize, itemCount);
            List<FrequencyPresetResponse> responseList = frequencyPresetService.getPageDataBySiteId(
                    siteId, pager.getCurrPage() - 1, pager.getPageSize());
            Date endTime = new Date();
            LogUtil.addOperationLog(OperationType.QUERY, logDesc, LogUtil.getSiteNameForLog(siteApiService, siteId));
            LogUtil.addElapseLog(OperationType.QUERY, LogUtil.buildElapseLogDesc(siteApiService, siteId, logDesc), endTime.getTime() - startTime.getTime());
            return new ApiPageData(pager, responseList);
        } catch (Exception e) {
            LogUtil.addOperationLog(OperationType.QUERY, LogUtil.buildFailOperationLogDesc(logDesc), LogUtil.getSiteNameForLog(siteApiService, siteId));
            throw e;
        }
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
    public Object addFrequencyPreset(@ModelAttribute FrequencyPresetRequest request) throws BizException, RemoteException {
        if (request.getSiteId() == null || request.getUpdateFreq() == null || request.getAlertFreq() == null) {
            log.error("Invalid parameter:  参数siteId、updateFreq（更新频率）、alertFreq（预警频率）中至少一个存在null值");
            throw new BizException(Constants.INVALID_PARAMETER);
        }
        if (request.getAlertFreq() > request.getUpdateFreq()) {
            log.error("Invalid parameter:  参数alertFreq（预警频率)大于updateFreq（更新频率）");
            throw new BizException(Constants.INVALID_PARAMETER);
        }
        String logDesc = "添加预设记录";
        if (!authorityService.hasRight(ContextHelper.getLoginUser().getUserName(), request.getSiteId(), null, Authority.KPIWEB_INDEXSETUP_ADDPREFREQ) && !authorityService.hasRight
                (ContextHelper.getLoginUser().getUserName(), null, null, Authority.KPIWEB_INDEXSETUP_ADDPREFREQ)) {
            LogUtil.addOperationLog(OperationType.QUERY, LogUtil.buildFailOperationLogDesc(logDesc), LogUtil.getSiteNameForLog(siteApiService, request.getSiteId()));
            throw new BizException(Authority.NO_AUTHORITY);
        }
        int num;
        try {
            num  = frequencyPresetService.addFrequencyPreset(request);
            LogUtil.addOperationLog(OperationType.ADD, logDesc, siteApiService.getSiteById(request.getSiteId(), "").getSiteName());
        } catch (Exception e) {
            LogUtil.addOperationLog(OperationType.ADD, LogUtil.buildFailOperationLogDesc(logDesc), LogUtil.getSiteNameForLog(siteApiService, request.getSiteId()));
            throw e;
        }
        if (num == 0) {
            String message = "添加的预设记录已经存在";
            log.error("向站点siteId" + request.getSiteId() + message);
            LogUtil.addWarnLog(OperationType.ADD, ErrorType.BIZ_EXCEPTION, "向站点siteId" + request.getSiteId() + message);
            throw new BizException(message);
        }
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
    public Object updateFrequencyPresetBySiteIdAndId(@ModelAttribute FrequencyPreset preset) throws BizException, RemoteException {
        if (preset.getSiteId() == null || preset.getId() == null
                || preset.getUpdateFreq() == null || preset.getAlertFreq() == null) {
            log.error("Invalid parameter:  参数siteId、id（预设记录编号）、updateFreq（更新频率）、alertFreq（预警频率）中至少一个存在null值");
            throw new BizException(Constants.INVALID_PARAMETER);
        }
        if (preset.getAlertFreq() > preset.getUpdateFreq()) {
            log.error("Invalid parameter:  参数alertFreq（预警频率)大于updateFreq（更新频率）");
            throw new BizException(Constants.INVALID_PARAMETER);
        }
        String logDesc = "修改预设记录";
        if (!authorityService.hasRight(ContextHelper.getLoginUser().getUserName(), preset.getSiteId(), null, Authority.KPIWEB_INDEXSETUP_UPDATEPREFREQ) && !authorityService.hasRight
                (ContextHelper.getLoginUser().getUserName(), null, null, Authority.KPIWEB_INDEXSETUP_UPDATEPREFREQ)) {
            LogUtil.addOperationLog(OperationType.UPDATE, LogUtil.buildFailOperationLogDesc(logDesc), LogUtil.getSiteNameForLog(siteApiService, preset.getSiteId()));
            throw new BizException(Authority.NO_AUTHORITY);
        }
        int num;
        try {
            num = frequencyPresetService.updateBySiteIdAndId(preset);
            LogUtil.addOperationLog(OperationType.UPDATE, logDesc, LogUtil.getSiteNameForLog(siteApiService, preset.getSiteId()));
        }catch (Exception e){
            LogUtil.addOperationLog(OperationType.UPDATE, LogUtil.buildFailOperationLogDesc(logDesc), LogUtil.getSiteNameForLog(siteApiService, preset.getSiteId()));
            throw e;
        }
        if (num == 0) {
            String message = "修改的预设记录已经存在";
            log.error("在站点siteId" + preset.getSiteId() + message);
            LogUtil.addWarnLog(OperationType.ADD, ErrorType.BIZ_EXCEPTION, "在站点siteId" + preset.getSiteId() + message);
            throw new BizException(message);
        }
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
    public Object deleteFrequencyPreset(@RequestParam("siteId") Integer siteId, @RequestParam("id") Integer id) throws BizException, RemoteException {
        if (siteId == null || id == null) {
            log.error("Invalid parameter:  参数siteId或者id（预设记录编号）存在null值");
            throw new BizException(Constants.INVALID_PARAMETER);
        }
        String logDesc = "删除预设记录";
        if (!authorityService.hasRight(ContextHelper.getLoginUser().getUserName(), siteId, null, Authority.KPIWEB_INDEXSETUP_DELETEPREFREQ) && !authorityService.hasRight(ContextHelper
                .getLoginUser().getUserName(), null, null, Authority.KPIWEB_INDEXSETUP_DELETEPREFREQ)) {
            LogUtil.addOperationLog(OperationType.DELETE, LogUtil.buildFailOperationLogDesc(logDesc), LogUtil.getSiteNameForLog(siteApiService, siteId));
            throw new BizException(Authority.NO_AUTHORITY);
        }
        boolean state;
        try {
            state = frequencySetupService.isPresetFeqUsed(siteId, id);
        }catch (Exception e){
            LogUtil.addOperationLog(OperationType.QUERY, LogUtil.buildFailOperationLogDesc(logDesc+"：查询当前预设记录id["+id+"]所对应记录是否被某个栏目使用"), LogUtil.getSiteNameForLog(siteApiService, siteId));
            throw e;
        }
        if (!state) {
            try {
                frequencyPresetService.deleteBySiteIdAndId(siteId, id);
                LogUtil.addOperationLog(OperationType.DELETE, logDesc, LogUtil.getSiteNameForLog(siteApiService, siteId));
            }catch (Exception e){
                LogUtil.addOperationLog(OperationType.DELETE, LogUtil.buildFailOperationLogDesc(logDesc), LogUtil.getSiteNameForLog(siteApiService, siteId));
                throw e;
            }
        } else {
            log.error("Invalid parameter:  当前参数siteId["+siteId+"]和id["+id+"]下的记录已经被某一栏目所使用，无法删除");
            LogUtil.addWarnLog(OperationType.DELETE, ErrorType.BIZ_EXCEPTION, "Invalid parameter:  当前参数siteId["+siteId+"]和id["+id+"]下的记录已经被某一栏目所使用，无法删除");
            throw new BizException("当前预设记录已经被某一栏目所使用，无法删除");
        }
        return null;
    }
}
