package com.trs.gov.kpi.service.impl.outer;

import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.gov.kpi.entity.outerapi.Dept;
import com.trs.gov.kpi.service.outer.DeptApiService;
import com.trs.gov.kpi.utils.StringUtil;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by he.lang on 2017/6/28.
 */
public class DeptApiServiceImplTest {
    @Test
    public void findDeptById() throws Exception {
        MockDeptApiService mockDeptApiService = new MockDeptApiService();
        Dept dept = mockDeptApiService.findDeptById("", 1);
        int count = 0;
        if (dept.getGroupId() != null) {
            count++;
        }
        if (dept.getGName() != null) {
            count++;
        }
        assertEquals(2, count);
        assertEquals(1, (int) dept.getGroupId());
        assertEquals("部门1", dept.getGName());
    }

    @Test
    public void findDeptById_return_gName_null() throws Exception {
        MockDeptApiService mockDeptApiService = new MockDeptApiService();
        Dept dept = mockDeptApiService.findDeptById("", 2);
        assertFalse(dept == null);
    }

    @Test
    public void queryDeptsByName() throws Exception {
        MockDeptApiService mockDeptApiService = new MockDeptApiService();
        List<Integer> list = mockDeptApiService.queryDeptsByName("", "部门");
        assertEquals(2, list.size());

        list = mockDeptApiService.queryDeptsByName("", "234");
        assertEquals(0, list.size());

        list = mockDeptApiService.queryDeptsByName("", "null");
        assertEquals(0, list.size());

    }

    @Test
    public void queryDeptsByName_gName_null() throws Exception {
        MockDeptApiService mockDeptApiService = new MockDeptApiService();
        List<Integer> list = mockDeptApiService.queryDeptsByName("", null);
        assertEquals(0, list.size());

        list = mockDeptApiService.queryDeptsByName("", "");
        assertEquals(0, list.size());

        list = mockDeptApiService.queryDeptsByName("", "  ");
        assertEquals(0, list.size());
    }


    public class MockDeptApiService implements DeptApiService {
        @Override
        public Dept findDeptById(String userName, int groupId) throws RemoteException {
            Dept dept = null;
            if (groupId == 1) {
                dept = new Dept();
                dept.setGName("部门1");
                dept.setGroupId(groupId);
                return dept;
            } else if (groupId == 2) {
                dept = new Dept();
                dept.setGName(null);
            }
            return new Dept();
        }

        @Override
        public List<Integer> queryDeptsByName(String userName, String deptName) throws IOException, RemoteException {
            if(StringUtil.isEmpty(deptName)){
                return new ArrayList<>();
            }
            List<Integer> list = null;
            if ("部门1".contains(deptName)) {
                list = new ArrayList<>();
                list.add(1);
                list.add(2);
                return list;
            }
            return new ArrayList<>();
        }
    }

}