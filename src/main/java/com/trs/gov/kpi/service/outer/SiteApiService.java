package com.trs.gov.kpi.service.outer;

import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.gov.kpi.entity.outerapi.Channel;
import com.trs.gov.kpi.entity.outerapi.Site;

import java.util.List;

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
    public Site getSiteById(int siteId, String userName) throws RemoteException;

    /**
     * 获取子栏目，如果parentId为0，查询的是站点下的第一级子栏目，
     * 如果parentId不为0，表示查询的是parent栏目下的第一级子栏目
     * @param siteId
     * @param parentId
     * @param userName
     * @return
     * @throws RemoteException
     */
    public List<Channel> getChildChannel(int siteId, int parentId, String userName) throws RemoteException;

    /**
     * 获取栏目详情
     *
     * @param channelId
     * @param userName
     * @return
     * @throws RemoteException
     */
    public Channel getChannelById(int channelId, String userName) throws RemoteException;
}
