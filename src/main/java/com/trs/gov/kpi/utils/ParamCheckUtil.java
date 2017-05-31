package com.trs.gov.kpi.utils;

import com.trs.gov.kpi.constant.Constants;
import com.trs.gov.kpi.entity.exception.BizException;
import com.trs.gov.kpi.entity.requestdata.PageDataRequestParam;
import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by ranwei on 2017/5/22.
 */
@Slf4j
public class ParamCheckUtil {

//    public static void paramCheck(IssueBase issueBase) throws BizException {
//
//        if (issueBase.getSiteId() == null) {
//            throw new BizException("站点编号为空");
//        }
//
//        if (issueBase.getSearchText() == null) {
//            issueBase.setSearchText("");
//        }
//
//        if (issueBase.getBeginDateTime() != null && !issueBase.getBeginDateTime().trim().isEmpty()) {
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            try {
//                sdf.setLenient(false);
//                sdf.parse(issueBase.getBeginDateTime());
//            } catch (ParseException e) {
//                throw new BizException(Constants.INVALID_PARAMETER);
//            }
//        }
//
//        if (issueBase.getEndDateTime() != null && !issueBase.getEndDateTime().trim().isEmpty()) {
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            try {
//                sdf.setLenient(false);
//                sdf.parse(issueBase.getEndDateTime());
//            } catch (ParseException e) {
//                throw new BizException(Constants.INVALID_PARAMETER);
//            }
//        }
//
//        if (issueBase.getIds() == null || issueBase.getIds().size() == 0) {
//            //初始化ids查询条件，默认有一个0，防止sql报错
//            List list = new ArrayList();
//            list.add(0);
//            issueBase.setIds(list);
//        }
//
//    }

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

    public static void paramCheck(PageDataRequestParam param) throws BizException {

        if (param.getSiteId() == null) {
            log.error("Invalid parameter: 参数param的siteId为null");
            throw new BizException(Constants.INVALID_PARAMETER);
        }

        pagerCheck(param.getPageIndex(), param.getPageSize());
        checkTime(param.getBeginDateTime());
        checkTime(param.getEndDateTime());

    }

    private static void checkTime(String time) throws BizException {
        if (time != null && !time.trim().isEmpty()) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                sdf.setLenient(false);
                sdf.parse(time);
            } catch (ParseException e) {
                log.error("Invalid parameter: 日期格式不满足 yyyy-MM-dd HH:mm:ss");
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
                log.error("Invalid parameter: Integer型数组中存在null值");
                throw new BizException(Constants.INVALID_PARAMETER);
            }
        }
    }
}
