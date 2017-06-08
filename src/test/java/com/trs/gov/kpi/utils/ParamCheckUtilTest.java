package com.trs.gov.kpi.utils;

import com.trs.gov.kpi.entity.exception.BizException;
import com.trs.gov.kpi.entity.requestdata.IssueCountRequest;
import com.trs.gov.kpi.entity.requestdata.PageDataRequestParam;
import com.trs.gov.kpi.entity.requestdata.WorkOrderRequest;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Created by ranwei on 2017/6/8.
 */
public class ParamCheckUtilTest {

    @Test(expected = BizException.class)
    public void paramCheck_SiteId_Null() throws Exception {
        PageDataRequestParam param = new PageDataRequestParam();
        ParamCheckUtil.paramCheck(param);
    }

    @Test(expected = BizException.class)
    public void paramCheck_Invalid_PageIndex() throws Exception {
        PageDataRequestParam param = new PageDataRequestParam();
        param.setSiteId(11);
        param.setPageIndex(0);
        ParamCheckUtil.paramCheck(param);
    }

    @Test(expected = BizException.class)
    public void paramCheck_Invalid_PageSize() throws Exception {
        PageDataRequestParam param = new PageDataRequestParam();
        param.setSiteId(11);
        param.setPageIndex(0);
        ParamCheckUtil.paramCheck(param);
    }


    @Test(expected = BizException.class)
    public void paramCheck_Invalid_Time() throws Exception {
        PageDataRequestParam param = new PageDataRequestParam();
        param.setSiteId(11);
        param.setBeginDateTime("2017/05/01 12:00:00");
        ParamCheckUtil.paramCheck(param);
    }

    @Test
    public void paramCheck_All() throws Exception {
        PageDataRequestParam param = new PageDataRequestParam();
        param.setSiteId(11);
        param.setPageIndex(1);
        param.setPageSize(1);
        param.setBeginDateTime("2017-05-05 12:00:00");
        param.setEndDateTime("2017-05-05 12:00:00");
        ParamCheckUtil.paramCheck(param);
    }

    @Test
    public void orderParamCheck() throws Exception {
        WorkOrderRequest request = new WorkOrderRequest();
        request.setPageIndex(1);
        request.setPageSize(1);
        request.setBeginDateTime("2017-05-05 12:00:00");
        request.setEndDateTime("2017-05-05 12:00:00");
        ParamCheckUtil.paramCheck(request);
    }

    @Test(expected = BizException.class)
    public void integerArrayParamCheck_Null() throws Exception {
        Integer[] array = new Integer[]{1, null};
        ParamCheckUtil.integerArrayParamCheck(array);
    }

    @Test
    public void integerArrayParamCheck_Not_Null() throws Exception {
        Integer[] array = new Integer[]{1, 2};
        ParamCheckUtil.integerArrayParamCheck(array);
    }
	
	@Test
    public void paramCheck() throws Exception {
        IssueCountRequest request = new IssueCountRequest();
        request.setBeginDateTime("2017-06-07 00:00:00");
        request.setEndDateTime("2017-06-08 00:00:00");
        request.setSiteIds("1,2,3");
        ParamCheckUtil.paramCheck(request);
        assertTrue(true);
    }

}