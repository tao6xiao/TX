package com.trs.gov.kpi.service.outer;

import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.gov.kpi.entity.outerapi.Dept;

import java.io.IOException;
import java.util.List;

/**
 * 采编中心->部门（组织）相关接口
 * Created by he.lang on 2017/6/28.
 */
public interface DeptApiService {
    /**
     * 通过部门id查询部门信息
     *
     * @param userName
     * @param groupId
     * @return
     */
    Dept findDeptById(String userName, int groupId) throws RemoteException;

    /**
     * 通过部门名称模糊查询部门id集合
     *
     * @param userName
     * @param deptName
     * @return
     */
    List<Integer> queryDeptsByName(String userName, String deptName) throws IOException, RemoteException;

}
