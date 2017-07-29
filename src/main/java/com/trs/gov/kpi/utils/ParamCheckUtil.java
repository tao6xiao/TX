package com.trs.gov.kpi.utils;

import com.trs.gov.kpi.constant.Constants;
import com.trs.gov.kpi.entity.exception.BizException;
import com.trs.gov.kpi.entity.requestdata.IssueCountRequest;
import com.trs.gov.kpi.entity.requestdata.PageDataRequestParam;
import com.trs.gov.kpi.entity.requestdata.WorkOrderRequest;
import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by ranwei on 2017/5/22.
 */
@Slf4j
public class ParamCheckUtil {

    private ParamCheckUtil() {

    }

    public static void pagerCheck(Integer pageIndex, Integer pageSize) throws BizException {
        if (pageIndex != null && pageIndex < 1) {
            log.error("Invalid parameter: 参数param的pageIndex不合法");
            throw new BizException(Constants.INVALID_PARAMETER);
        }

        if (pageSize != null && pageSize < 1) {
            log.error("Invalid parameter: 参数param的pageSize不合法");
            throw new BizException(Constants.INVALID_PARAMETER);
        }
    }

    /**
     * 问题和预警的参数校验
     *
     * @param param
     * @throws BizException
     */
    public static void paramCheck(PageDataRequestParam param) throws BizException {

        if (param.getSiteId() == null) {
            log.error("Invalid parameter: 参数param的siteId为null");
            throw new BizException(Constants.INVALID_PARAMETER);
        }

        pagerCheck(param.getPageIndex(), param.getPageSize());
        checkCommonTime(param.getBeginDateTime());
        checkCommonTime(param.getEndDateTime());

    }

    /**
     * 工单的参数校验
     *
     * @param request
     * @throws BizException
     */
    public static void paramCheck(WorkOrderRequest request) throws BizException {

        pagerCheck(request.getPageIndex(), request.getPageSize());
        checkCommonTime(request.getBeginDateTime());
        checkCommonTime(request.getEndDateTime());

    }

    /**
     * 问题统计参数校验
     *
     * @param request
     * @throws BizException
     */
    public static void paramCheck(IssueCountRequest request) throws BizException {
        Integer[] siteIds = StringUtil.stringToIntegerArray(request.getSiteIds());
        if (siteIds == null || siteIds.length == 0) {
            log.error("Invalid parameter: 参数数组siteIds为null值");
            throw new BizException(Constants.INVALID_PARAMETER);
        }
        for (int i = 0; i < siteIds.length; i++) {
            if (siteIds[i] == null) {
                log.error("Invalid parameter: 参数数组siteIds中存在null值");
                throw new BizException(Constants.INVALID_PARAMETER);
            }
        }
        checkCommonTime(request.getBeginDateTime());
        checkCommonTime(request.getEndDateTime());

    }

    public static void checkCommonTime(String time) throws BizException {
        if (time != null && !time.trim().isEmpty()) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                sdf.setLenient(false);
                sdf.parse(time);
            } catch (ParseException e) {
                log.error("Invalid parameter: 日期格式不满足 yyyy-MM-dd HH:mm:ss");
                LogUtil.addSystemLog("Invalid parameter: 日期格式不满足 yyyy-MM-dd HH:mm:ss");
                throw new BizException(Constants.INVALID_PARAMETER);
            }
        }
    }

    public static void checkDayTime(String time) throws BizException {
        if (time != null && !time.trim().isEmpty()) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            try {
                sdf.setLenient(false);
                sdf.parse(time);
            } catch (ParseException e) {
                log.error("Invalid parameter: 日期格式不满足 yyyy-MM-dd");
                LogUtil.addSystemLog("Invalid parameter: 日期格式不满足 yyyy-MM-dd");
                throw new BizException(Constants.INVALID_PARAMETER);
            }
        }
    }

    /**
     * 用于判断Integer型数组中是否存在null值
     *
     * @param array
     * @throws BizException
     */
    public static void integerArrayParamCheck(Integer[] array) throws BizException {
        for (int i = 0; i < array.length; i++) {
            if (array[i] == null) {
                log.error("Invalid parameter: Integer型数组"+array+"中存在null值");
                throw new BizException(Constants.INVALID_PARAMETER);
            }
        }
    }
}
