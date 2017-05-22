package com.trs.gov.kpi.utils;

import com.trs.gov.kpi.entity.IssueBase;
import com.trs.gov.kpi.entity.exception.BizException;

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

        if (issueBase.getEndDateTime() != null && !issueBase.getEndDateTime().trim().isEmpty()) {
            issueBase.setEndDateTime(InitTime.initTime(issueBase.getEndDateTime()));//结束日期加一，避免查询不到结束日期当天数据的情况
        }

        if (issueBase.getSearchText() == null) {
            issueBase.setSearchText("");
        }

        if (issueBase.getEndDateTime() == null || issueBase.getEndDateTime().trim().isEmpty()) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            issueBase.setEndDateTime(sdf.format(new Date()));
        }
        if (issueBase.getIds() == null || issueBase.getIds().size() == 0) {
            //初始化ids查询条件，默认有一个0，防止sql报错
            List list = new ArrayList();
            list.add(0);
            issueBase.setIds(list);
        }

    }
}
