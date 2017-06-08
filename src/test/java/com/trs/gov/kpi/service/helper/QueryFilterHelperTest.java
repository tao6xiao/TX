package com.trs.gov.kpi.service.helper;

import com.trs.gov.kpi.entity.dao.QueryFilter;
import com.trs.gov.kpi.entity.requestdata.PageDataRequestParam;
import com.trs.gov.kpi.entity.requestdata.WorkOrderRequest;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by ranwei on 2017/6/8.
 */
public class QueryFilterHelperTest {

    @Test
    public void toFilter_SearchField_Null_Sort() throws Exception {
        PageDataRequestParam param = new PageDataRequestParam();
        param.setSiteId(11);
        param.setBeginDateTime("2017-05-05 12:00:00");
        param.setEndDateTime("2017-06-05 12:00:00");
        param.setSearchText("123");
        param.setSortFields("id,asc");
        QueryFilter filter = QueryFilterHelper.toFilter(param);
        assertEquals(4, filter.getCondFields().size());
        assertEquals(1, filter.getSortFields().size());
    }

    @Test
    public void toFilter_SearchField_Id() throws Exception {
        PageDataRequestParam param = new PageDataRequestParam();
        param.setSiteId(11);
        param.setBeginDateTime("2017-05-05 12:00:00");
        param.setEndDateTime("2017-06-05 12:00:00");
        param.setSearchField("id");
        param.setSearchText("123");
        QueryFilter filter = QueryFilterHelper.toFilter(param);
        assertEquals(4, filter.getCondFields().size());
    }

    @Test
    public void toFilter_SearchField_IssueType() throws Exception {
        PageDataRequestParam param = new PageDataRequestParam();
        param.setSiteId(11);
        param.setBeginDateTime("2017-05-05 12:00:00");
        param.setEndDateTime("2017-06-05 12:00:00");
        param.setSearchField("issueType");
        param.setSearchText("123");
        QueryFilter filter = QueryFilterHelper.toFilter(param);
        assertEquals(4, filter.getCondFields().size());
    }

    @Test
    public void toOrderFilter_SiteIdAndSearchField_Null_Sort() throws Exception {
        WorkOrderRequest request = new WorkOrderRequest();
        request.setBeginDateTime("2017-05-05 12:00:00");
        request.setEndDateTime("2017-06-05 12:00:00");
        request.setSearchText("123");
        request.setSortFields("id,asc");
        QueryFilter filter = QueryFilterHelper.toFilter(request);
        assertEquals(3, filter.getCondFields().size());
        assertEquals(1, filter.getSortFields().size());
    }

    @Test
    public void toOrderFilter_SearchField_Id() throws Exception {
        WorkOrderRequest request = new WorkOrderRequest();
        request.setSiteId(new Integer[]{11, 12});
        request.setBeginDateTime("2017-05-05 12:00:00");
        request.setEndDateTime("2017-06-05 12:00:00");
        request.setSearchField("id");
        request.setSearchText("123");
        QueryFilter filter = QueryFilterHelper.toFilter(request);
        assertEquals(4, filter.getCondFields().size());
    }

    @Test
    public void toOrderFilter_SearchField_Department() throws Exception {
        WorkOrderRequest request = new WorkOrderRequest();
        request.setSiteId(new Integer[]{11, 12});
        request.setBeginDateTime("2017-05-05 12:00:00");
        request.setEndDateTime("2017-06-05 12:00:00");
        request.setSearchField("department");
        request.setSearchText("123");
        QueryFilter filter = QueryFilterHelper.toFilter(request);
        assertEquals(3, filter.getCondFields().size());//TODO 等获取部门接口，有了之后,期望条件数+1
    }

    @Test
    public void toPageFilter_SearchField_Null_Sort() throws Exception {
        PageDataRequestParam param = new PageDataRequestParam();
        param.setSiteId(15);
        param.setBeginDateTime("2017-05-05 12:00:00");
        param.setEndDateTime("2017-06-05 12:00:00");
        param.setSearchText("123");
        param.setSortFields("id,asc");
        QueryFilter filter = QueryFilterHelper.toPageFilter(param);
        assertEquals(4, filter.getCondFields().size());
        assertEquals(1, filter.getSortFields().size());
    }

    @Test
    public void toPageFilter_SearchField_Id() throws Exception {
        PageDataRequestParam param = new PageDataRequestParam();
        param.setSiteId(15);
        param.setBeginDateTime("2017-05-05 12:00:00");
        param.setEndDateTime("2017-06-05 12:00:00");
        param.setSearchField("id");
        param.setSearchText("123");
        QueryFilter filter = QueryFilterHelper.toPageFilter(param);
        assertEquals(4, filter.getCondFields().size());
    }

    @Test
    public void toPageFilter_SearchField_ChnlName() throws Exception {
        PageDataRequestParam param = new PageDataRequestParam();
        param.setSiteId(15);
        param.setBeginDateTime("2017-05-05 12:00:00");
        param.setEndDateTime("2017-06-05 12:00:00");
        param.setSearchField("chnlName");
        param.setSearchText("123");
        QueryFilter filter = QueryFilterHelper.toPageFilter(param);
        assertEquals(3, filter.getCondFields().size());//TODO 等获取栏目接口，有了之后,期望条件数+1
    }

}