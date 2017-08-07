package com.trs.gov.kpi.service.outer;

import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.gov.kpi.entity.outerapi.Channel;
import com.trs.gov.kpi.entity.outerapi.Site;

import java.util.List;
import java.util.Set;

/**
 * Created by linwei on 2017/5/18.
 */
public interface SiteApiService extends OuterApiService {

    /**
     * 根据站点id获取站点详情
     *
     * @param siteId
     * @param userName
     * @return
     * @throws RemoteException
     */
    Site getSiteById(int siteId, String userName) throws RemoteException;

    /**
     * 获取子栏目，如果parentId为0，查询的是站点下的第一级子栏目，
     * 如果parentId不为0，表示查询的是parent栏目下的第一级子栏目
     *
     * @param siteId
     * @param parentId
     * @param userName
     * @return
     * @throws RemoteException
     */
    List<Channel> getChildChannel(int siteId, int parentId, String userName) throws RemoteException;

    /**
     * 获取栏目详情
     *
     * @param channelId
     * @param userName
     * @return
     * @throws RemoteException
     */
    Channel getChannelById(int channelId, String userName) throws RemoteException;


    /**
     * 获取站点或者栏目的发布地址
     * 如果chnnelId为0，则使用siteId获取站点的发布地址，否则查询的是栏目的发布地址
     *
     * @param siteId
     * @param channelId
     * @return
     * @throws RemoteException
     */
    String getChannelPublishUrl(String userName, int siteId, int channelId) throws RemoteException;


    /**
     * 获取从第一层子栏目到最底层的栏目在内的所有子栏目的id
     *
     * @param userName
     * @param siteId
     * @param channelId
     * @param chnlIdSet
     * @return
     * @throws RemoteException
     */
    Set<Integer> getAllChildChnlIds(String userName, int siteId, int channelId, Set<Integer> chnlIdSet) throws RemoteException;


    /**
     * 获取所有最底层的栏目
     *
     * @param userName
     * @param siteId
     * @param channelId
     * @param chnlIdSet
     * @return
     * @throws RemoteException
     */
    Set<Integer> getAllLeafChnlIds(String userName, int siteId, int channelId, Set<Integer> chnlIdSet) throws RemoteException;


    /**
     * 使用栏目名称模糊查找所有的栏目id
     *
     * @param userName
     * @param siteId
     * @param chnlName
     * @return
     * @throws RemoteException
     */
    List<Integer> findChnlIds(String userName, int siteId, String chnlName) throws RemoteException;


    /**
     * 根据部门名称找栏目id
     *
     * @param userName
     * @param siteIds
     * @param departmentName
     * @return
     * @throws RemoteException
     */
    List<Integer> findChnlIdsByDepartment(String userName, List<Integer> siteIds, String departmentName) throws RemoteException;

    /**
     * 通过url查找栏目
     *
     * @param userName
     * @param url
     * @return
     * @throws RemoteException
     */
    Channel findChannelByUrl(String userName, String url, int siteId) throws RemoteException;

}
