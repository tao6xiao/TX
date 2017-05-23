package com.trs.gov.kpi.utils;

import com.trs.gov.kpi.entity.IssueBase;
import com.trs.gov.kpi.entity.exception.BizException;
import com.trs.gov.kpi.entity.requestdata.PageDataRequestParam;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by ranwei on 2017/5/22.
 */
public class ParamCheckUtil {

    public static void paramCheck(IssueBase issueBase) throws BizException {

        if (issueBase.getSiteId() == null) {
            throw new BizException("站点编号为空");
        }

        if (issueBase.getSearchText() == null) {
            issueBase.setSearchText("");
        }

//        if (issueBase.getEndDateTime() == null || issueBase.getEndDateTime().trim().isEmpty()) {
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//            issueBase.setEndDateTime(sdf.format(new Date()));
//        }

        if (issueBase.getBeginDateTime() != null && !issueBase.getBeginDateTime().trim().isEmpty()) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date d = null;
            String da = null;
            try {
                d = sdf.parse(issueBase.getBeginDateTime());
                da = sdf.format(new Date(issueBase.getBeginDateTime()));
            } catch (ParseException e) {
                throw new BizException("参数不合法");
            }
        }

        if (issueBase.getEndDateTime() != null && !issueBase.getEndDateTime().trim().isEmpty()) {
            issueBase.setEndDateTime(InitTime.initTime(issueBase.getEndDateTime()));//结束日期加一，避免查询不到结束日期当天数据的情况
        }

        if (issueBase.getIds() == null || issueBase.getIds().size() == 0) {
            //初始化ids查询条件，默认有一个0，防止sql报错
            List list = new ArrayList();
            list.add(0);
            issueBase.setIds(list);
        }

    }

    public static void pagerCheck(Integer pageIndex, Integer pageSize) throws BizException{
        if (pageIndex != null && pageIndex < 1) {
            throw new BizException("参数不合法");
        }

        if (pageSize != null && pageSize < 1) {
            throw new BizException("参数不合法");
        }
    }

    public static void paramCheck(PageDataRequestParam param) throws BizException {

        if (param.getSiteId() == null) {
            throw new BizException("参数不合法");
        }

        if (param.getPageSize() != null && param.getPageSize() <= 0) {
            throw new BizException("参数不合法");
        }
        if (param.getPageIndex() != null && param.getPageIndex() <= 0) {
            throw new BizException("参数不合法");
        }

    }

    /**
     * 用于判断Integer型数组中是否存在null值
     * @param array
     * @throws BizException
     */
    public static void integerArrayParamCheck(Integer[] array) throws BizException {
        for (int i= 0; i < array.length; i++){
            if(array[i] == null){
                throw new BizException("参数不合法");
            }
        }
    }
}
