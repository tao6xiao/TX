package com.trs.gov.kpi.service.helper;

import com.trs.gov.kpi.dao.FrequencyPresetMapper;
import com.trs.gov.kpi.entity.FrequencyPreset;
import com.trs.gov.kpi.entity.dao.OrCondDBFields;
import com.trs.gov.kpi.entity.dao.QueryFilter;
import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.gov.kpi.entity.outerapi.Channel;
import com.trs.gov.kpi.entity.outerapi.Dept;
import com.trs.gov.kpi.entity.outerapi.Site;
import com.trs.gov.kpi.entity.requestdata.FrequencySetupSelectRequest;
import com.trs.gov.kpi.entity.requestdata.IssueCountRequest;
import com.trs.gov.kpi.entity.requestdata.PageDataRequestParam;
import com.trs.gov.kpi.entity.requestdata.WorkOrderRequest;
import com.trs.gov.kpi.service.outer.DeptApiService;
import com.trs.gov.kpi.service.outer.SiteApiService;
import lombok.Setter;
import org.apache.ibatis.annotations.Param;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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
        QueryFilter filter = QueryFilterHelper.toFilter(request, new MockDeptApiService());
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
        QueryFilter filter = QueryFilterHelper.toFilter(request, new MockDeptApiService());
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
        QueryFilter filter = QueryFilterHelper.toFilter(request, new MockDeptApiService());
        assertEquals(4, filter.getCondFields().size());
    }

    @Test
    public void toPageFilter_SearchField_Null_Sort() throws Exception {
        PageDataRequestParam param = new PageDataRequestParam();
        param.setSiteId(15);
        param.setBeginDateTime("2017-05-05 12:00:00");
        param.setEndDateTime("2017-06-05 12:00:00");
        param.setSearchText("123");
        param.setSortFields("id,asc");
        QueryFilter filter = QueryFilterHelper.toPageFilter(param, new MockSiteApiService());
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
        QueryFilter filter = QueryFilterHelper.toPageFilter(param, new MockSiteApiService());
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
        QueryFilter filter = QueryFilterHelper.toPageFilter(param, new MockSiteApiService());
        assertEquals(4, filter.getCondFields().size());
    }

    @Test
    public void toFilter() throws Exception {
        IssueCountRequest request = new IssueCountRequest();
        request.setBeginDateTime("2017-06-07 00:00:00");
        request.setEndDateTime("2017-06-08 00:00:00");
        QueryFilter filter = QueryFilterHelper.toFilter(request);
        assertTrue(!filter.getCondFields().isEmpty());

    }

    @Test
    public void toFilter_freqSetup_siteId() throws Exception {

        MockSiteApiService siteApiService = new MockSiteApiService();
        FrequencySetupSelectRequest request = new FrequencySetupSelectRequest();
        request.setSiteId(11);
        QueryFilter filter = QueryFilterHelper.toFilter(request, siteApiService, new MockFreqPreset());
        assertEquals(1, filter.getCondFields().size());
        assertEquals("siteId", filter.getCondFields().get(0).getFieldName());
    }

    @Test
    public void toFilter_freqSetup_chnlName() throws Exception {
        MockSiteApiService siteApiService = new MockSiteApiService();

        FrequencySetupSelectRequest request = new FrequencySetupSelectRequest();
        request.setSiteId(11);
        request.setSearchField("chnlName");
        request.setSearchText("123");
        QueryFilter filter = QueryFilterHelper.toFilter(request, siteApiService, new MockFreqPreset());
        assertEquals(2, filter.getCondFields().size());
        assertEquals("chnlId", filter.getCondFields().get(1).getFieldName());
        assertEquals(new Integer(-1), filter.getCondFields().get(1).getCondValue());

        siteApiService.setChnlIds(Arrays.asList(1, 2, 3));
        filter = QueryFilterHelper.toFilter(request, siteApiService, new MockFreqPreset());
        assertEquals(2, filter.getCondFields().size());
        assertEquals("chnlId", filter.getCondFields().get(1).getFieldName());
        Collection value = (Collection) filter.getCondFields().get(1).getCondValue();
        assertEquals(3, value.size());
    }

    @Test
    public void toFilter_freqSetup_chnlId() throws Exception {
        MockSiteApiService siteApiService = new MockSiteApiService();

        FrequencySetupSelectRequest request = new FrequencySetupSelectRequest();
        request.setSiteId(11);
        request.setSearchField("chnlId");
        request.setSearchText("123");
        QueryFilter filter = QueryFilterHelper.toFilter(request, siteApiService, new MockFreqPreset());
        assertEquals(2, filter.getCondFields().size());
        assertEquals("chnlId", filter.getCondFields().get(1).getFieldName());
        assertEquals("%123%", filter.getCondFields().get(1).getCondValue());
    }

    @Test
    public void toFilter_freqSetup_updateFreq() throws Exception {
        MockSiteApiService siteApiService = new MockSiteApiService();
        MockFreqPreset preset = new MockFreqPreset();

        FrequencySetupSelectRequest request = new FrequencySetupSelectRequest();
        request.setSiteId(11);
        request.setSearchField("updateFreq");
        request.setSearchText("123");
        QueryFilter filter = QueryFilterHelper.toFilter(request, siteApiService, new MockFreqPreset());
        assertEquals(2, filter.getCondFields().size());
        assertEquals("presetFeqId", filter.getCondFields().get(1).getFieldName());
        assertEquals(new Integer(-1), filter.getCondFields().get(1).getCondValue());

        List<FrequencyPreset> presets = new ArrayList<>();
        FrequencyPreset preset1 = new FrequencyPreset();
        preset1.setId(11);
        presets.add(preset1);
        preset.setPresets(presets);
        filter = QueryFilterHelper.toFilter(request, siteApiService, preset);
        assertEquals(2, filter.getCondFields().size());
        assertEquals("presetFeqId", filter.getCondFields().get(1).getFieldName());
        Collection ids = (Collection) filter.getCondFields().get(1).getCondValue();
        assertEquals(1, ids.size());
        assertEquals(11, ids.iterator().next());
    }

    @Test
    public void toFilter_freqSetup_all_cond() throws Exception {
        MockSiteApiService siteApiService = new MockSiteApiService();
        MockFreqPreset preset = new MockFreqPreset();

        FrequencySetupSelectRequest request = new FrequencySetupSelectRequest();
        request.setSiteId(11);
        request.setSearchText("123");
        QueryFilter filter = QueryFilterHelper.toFilter(request, siteApiService, new MockFreqPreset());
        assertEquals(2, filter.getCondFields().size());
        assertEquals("OR_COMPLEX_FIELD", filter.getCondFields().get(1).getFieldName());
        OrCondDBFields orFields = (OrCondDBFields) filter.getCondFields().get(1).getCondValue();
        assertEquals(1, orFields.getFields().size());

        request.setSearchField(null);
        siteApiService.setChnlIds(Arrays.asList(1, 2));
        filter = QueryFilterHelper.toFilter(request, siteApiService, new MockFreqPreset());
        assertEquals(2, filter.getCondFields().size());
        assertEquals("OR_COMPLEX_FIELD", filter.getCondFields().get(1).getFieldName());
        orFields = (OrCondDBFields) filter.getCondFields().get(1).getCondValue();
        assertEquals(2, orFields.getFields().size());

        request.setSearchField("");
        List<FrequencyPreset> presets = new ArrayList<>();
        FrequencyPreset preset1 = new FrequencyPreset();
        preset1.setId(11);
        presets.add(preset1);
        preset.setPresets(presets);
        filter = QueryFilterHelper.toFilter(request, siteApiService, preset);
        assertEquals(2, filter.getCondFields().size());
        assertEquals("OR_COMPLEX_FIELD", filter.getCondFields().get(1).getFieldName());
        orFields = (OrCondDBFields) filter.getCondFields().get(1).getCondValue();
        assertEquals(3, orFields.getFields().size());
    }

    @Test
    public void toSetDeptFilter(){
        PageDataRequestParam param = new PageDataRequestParam();
        param.setSiteId(1);
        param.setSearchField(null);
        param.setSearchText("zhangsan");
    }
    private class MockFreqPreset implements FrequencyPresetMapper {

        @Setter
        List<FrequencyPreset> presets = new ArrayList<>();

        @Override
        public int deleteByPrimaryKey(Integer id) {
            return 0;
        }

        @Override
        public int insert(FrequencyPreset record) {
            return 0;
        }

        @Override
        public int insertSelective(FrequencyPreset record) {
            return 0;
        }

        @Override
        public FrequencyPreset selectByPrimaryKey(Integer id) {
            return null;
        }

        @Override
        public int updateByPrimaryKeySelective(FrequencyPreset record) {
            return 0;
        }

        @Override
        public int updateByPrimaryKey(FrequencyPreset record) {
            return 0;
        }


        @Override
        public int updateBySiteIdAndId(FrequencyPreset frequencyPreset) {
            return 0;
        }

        @Override
        public int deleteBySiteIdAndId(@Param("siteId") int siteId, @Param("id") int id) {
            return 0;
        }

        @Override
        public int selectItemCountBySiteId(int siteId) {
            return 0;
        }

        @Override
        public FrequencyPreset selectById(@Param("siteId") int siteId, @Param("id") int id) {
            return null;
        }

        @Override
        public FrequencyPreset selectBySiteIdAndId(@Param("siteId") int siteId, @Param("id") int id) {
            return null;
        }

        @Override
        public List<FrequencyPreset> selectPageDataBySiteId(@Param("siteId") int siteId, @Param("pageCalculate") int pageCalculate, @Param("pageSize") int pageSize) {
            return null;
        }

        @Override
        public List<FrequencyPreset> selectBySiteIdAndUpdateFreq(@Param("siteId") Integer siteId, @Param("updateFreq") String updateFreq) {
            return presets;
        }

        @Override
        public FrequencyPreset selectByPreset(@Param("preset") FrequencyPreset preset) {
            return null;
        }
    }

    private class MockSiteApiService implements SiteApiService {

        private List<Integer> chnlIds = new ArrayList<>();

        public void setChnlIds(List<Integer> ids) {
            this.chnlIds = ids;
        }

        @Override
        public Site getSiteById(int siteId, String userName) throws RemoteException {
            return null;
        }

        @Override
        public List<Channel> getChildChannel(int siteId, int parentId, String userName) throws RemoteException {
            return null;
        }

        @Override
        public Channel getChannelById(int channelId, String userName) throws RemoteException {
            return null;
        }

        @Override
        public String getChannelPublishUrl(String userName, int siteId, int channelId) throws RemoteException {
            return null;
        }

        @Override
        public Set<Integer> getAllChildChnlIds(String userName, int siteId, int channelId, Set<Integer> chnlIdSet) throws RemoteException {
            return null;
        }

        @Override
        public Set<Integer> getAllLeafChnlIds(String userName, int siteId, int channelId, Set<Integer> chnlIdSet) throws RemoteException {
            return null;
        }

        @Override
        public List<Integer> findChnlIds(String userName, int siteId, String chnlName) throws RemoteException {
            return chnlIds;
        }

        @Override
        public List<Integer> findChnlIdsByDepartment(String userName, List<Integer> siteIds, String departmentName) throws RemoteException {
            return null;
        }
    }

    private class MockDeptApiService implements DeptApiService {
        @Override
        public Dept findDeptById(String userName, int groupId) throws RemoteException {
            return null;
        }

        @Override
        public List<Integer> queryDeptsByName(String userName, String deptName) throws RemoteException {
            return null;
        }
    }

}