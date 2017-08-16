package com.trs.gov.kpi.service.outer;

import com.trs.gov.kpi.constant.ErrorType;
import com.trs.gov.kpi.constant.OperationType;
import com.trs.gov.kpi.entity.DutyDept;
import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.gov.kpi.service.DutyDeptService;
import com.trs.gov.kpi.utils.LogUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by linwei on 2017/5/24.
 */
@Slf4j
@Component
public class SiteChannelServiceHelper {

    @Resource
    private SiteApiService siteApiService;

    @Resource
    private DocumentApiService documentApiService;

    @Resource
    private DutyDeptService dutyDeptService;

    /**
     * 获取所有空栏目
     *
     * @param siteId
     * @return
     * @throws RemoteException
     */
    public List<Integer> getEmptyChannel(int siteId) throws RemoteException {

        Set<Integer> allLeafChannels = new HashSet<>();
        allLeafChannels = siteApiService.getAllLeafChnlIds("", siteId, 0, allLeafChannels);

        List<Integer> emptyChannels = new ArrayList<>();
        if (allLeafChannels == null || allLeafChannels.isEmpty()) {
            return emptyChannels;
        }

        for (Integer channelId : allLeafChannels) {
            try {
                final List<Integer> publishDocIds = documentApiService.getPublishDocIds("", siteId,
                        channelId, null);
                if (publishDocIds == null || publishDocIds.isEmpty()) {
                    emptyChannels.add(channelId);
                }
            } catch (RemoteException e) {
                log.error("", e);
                LogUtil.addErrorLog(OperationType.REMOTE, ErrorType.REMOTE_FAILED, "获取空栏目，siteId[" + siteId + "]", e);
            }
        }

        return emptyChannels;
    }

    /**
     * 根据栏目ID查找所属部门
     * @param chnlId
     * @param userName
     * @return
     * @throws RemoteException
     */
    public Integer findRelatedDept(Integer chnlId, String userName) throws RemoteException {

        // 先直接查找一下当前栏目是否对应了部门
        final DutyDept dept = dutyDeptService.getByChnlId(chnlId, DutyDept.ALL_CONTAIN_COND);
        if (dept != null) {
            return dept.getDeptId();
        }

        // 查找父栏目所属部门
        final List<Integer> channelPath = siteApiService.findChannelPath(chnlId, userName);
        // 逆向查找
        if (channelPath.isEmpty()) {
            return null;
        } else {
            // 不包含自己
            int index = channelPath.size() - 2;
            for (; index >= 0; index--) {
                final DutyDept dutyDept = dutyDeptService.getByChnlId(channelPath.get(index), DutyDept.CONTAIN_CHILD);
                if (dutyDept != null) {
                    return dutyDept.getDeptId();
                }
            }
            return null;
        }
    }

}
