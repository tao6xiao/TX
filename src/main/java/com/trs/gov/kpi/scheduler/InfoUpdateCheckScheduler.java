package com.trs.gov.kpi.scheduler;

import com.trs.gov.kpi.constant.Types;
import com.trs.gov.kpi.dao.FrequencyPresetMapper;
import com.trs.gov.kpi.dao.FrequencySetupMapper;
import com.trs.gov.kpi.dao.InfoUpdateMapper;
import com.trs.gov.kpi.entity.DefaultUpdateFreq;
import com.trs.gov.kpi.entity.FrequencyPreset;
import com.trs.gov.kpi.entity.FrequencySetup;
import com.trs.gov.kpi.entity.SimpleTree;
import com.trs.gov.kpi.entity.check.CheckingChannel;
import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.gov.kpi.entity.outerapi.Channel;
import com.trs.gov.kpi.entity.responsedata.InfoUpdateResponse;
import com.trs.gov.kpi.service.DefaultUpdateFreqService;
import com.trs.gov.kpi.service.MonitorSiteService;
import com.trs.gov.kpi.service.outer.DocumentApiService;
import com.trs.gov.kpi.service.outer.SiteApiService;
import com.trs.gov.kpi.utils.DateUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by linwei on 2017/5/23.
 */

@Slf4j
@Component
@Scope("prototype")
public class InfoUpdateCheckScheduler implements SchedulerTask {

    // 开始检查的第一个周期起始点
    private static final String BEGIN_CHECK_DAY = "2017-05-01 00:00:00";

    @Resource
    SiteApiService siteApiService;

    @Resource
    DocumentApiService documentApiService;

    @Resource
    FrequencySetupMapper frequencySetupMapper;

    @Resource
    FrequencyPresetMapper frequencyPresetMapper;

    @Resource
    MonitorSiteService monitorSiteService;

    @Resource
    DefaultUpdateFreqService defaultUpdateFreqService;

    @Resource
    InfoUpdateMapper infoUpdateMapper;

    @Getter @Setter
    private Integer siteId;

    @Getter @Setter
    private String baseUrl;

    // 检测时缓存频率设置
    private Map<Integer, FrequencySetup> setupCache;

    // 检测时缓存频率预设
    private Map<Integer, FrequencyPreset> presetCache;

    // 缓存自查更新频率
    private DefaultUpdateFreq defaultUpdateFreq;

    @Getter
    private final Runnable task = new Runnable() {

        @Override
        public void run() {

            log.info("InfoUpdateCheckScheduler " + siteId + " start...");
            try {
                List<SimpleTree<CheckingChannel>> siteTrees = buildChannelTree();

                // 优先从最下面的子栏目开始进行检查，然后再遍历上层栏目，得出结果进行数据库更新
                for (SimpleTree<CheckingChannel> tree : siteTrees) {
                    List<SimpleTree.Node<CheckingChannel>> children = tree.getRoot().getChildren();
                    if (children == null) {
                        continue;
                    }
                    for (SimpleTree.Node<CheckingChannel> child : children) {
                        checkChannelTreeUpdate(child);
                    }
                }

                insertIssueAndWarning(siteTrees);
            } catch (Exception e) {
                log.error("check link:{}, siteId:{} info update error!", baseUrl, siteId, e);
            } finally {
                log.info("InfoUpdateCheckScheduler " + siteId + " end...");
            }
        }
    };

    private List<SimpleTree<CheckingChannel>> buildChannelTree() throws RemoteException {

        // 获取栏目的更新设置
        final List<FrequencySetup> frequencySetups = frequencySetupMapper
                .selectPageDataFrequencySetupList(siteId, 0, 100000);

        setupCache = new HashMap<>();
        presetCache = new HashMap<>();
        for (FrequencySetup setup : frequencySetups) {
            setupCache.put(setup.getChnlId(), setup);
            if (presetCache.get(setup.getPresetFeqId()) == null) {
                FrequencyPreset preset = frequencyPresetMapper.selectById(siteId, setup.getPresetFeqId());
                presetCache.put(setup.getPresetFeqId(), preset);
            }
        }

        defaultUpdateFreq = defaultUpdateFreqService
                .getDefaultUpdateFreqBySiteId(siteId);

        // 获取监控站点
        List<SimpleTree<CheckingChannel>> trees = new ArrayList<>();
        SimpleTree<CheckingChannel> tree = buildOneChannelTree(siteId);
        if (tree != null) {
            trees.add(tree);
        }

        return trees;
    }


    /**
     * 一个站点构建一个栏目检查树
     *
     * @param monitorSiteId
     * @return
     * @throws RemoteException
     */
    private SimpleTree<CheckingChannel> buildOneChannelTree(Integer monitorSiteId) throws RemoteException {

        // 查询整个栏目
        final List<Channel> childChannel = siteApiService.getChildChannel(monitorSiteId, 0, "");
        if (childChannel == null || childChannel.isEmpty()) {
            return null;
        }

        SimpleTree<CheckingChannel> tree = new SimpleTree<>(null);
        final SimpleTree.Node<CheckingChannel> parent = tree.getRoot();
        for (Channel chnl : childChannel) {
            try {
                recursiveBuildChannelTree(chnl, parent);
            } catch (Exception e) {
                log.error("", e);
            }
        }

        return tree;
    }

    /**
     * 递归添加子栏目
     * @param curChnl
     * @param parent
     *
     * @throws RemoteException
     */
    private void recursiveBuildChannelTree(
            Channel curChnl,
            SimpleTree.Node<CheckingChannel> parent) throws RemoteException {

        if (curChnl == null) {
            return;
        }

        CheckingChannel newCheckingChannel = new CheckingChannel();
        newCheckingChannel.setChannel(curChnl);

        // 获取检查参数和频率
        FrequencySetup freqSetup = setupCache.get(curChnl.getChannelId());
        if (freqSetup == null) {

            // 如果父节点存在，则保持一致
            if (parent.getData() != null) {
                CheckingChannel parentChecking = parent.getData();
                newCheckingChannel.setShouldIssueCheck(parentChecking.isShouldIssueCheck());
                newCheckingChannel.setShouldSelfCheck(parentChecking.isShouldSelfCheck());
                newCheckingChannel.setBeginDateTime(parentChecking.getBeginDateTime());
                newCheckingChannel.setCheckDay(parentChecking.getCheckDay());
                newCheckingChannel.setWarningDay(parentChecking.getWarningDay());
                newCheckingChannel.setSelfCheckBeginDate(parentChecking.getSelfCheckBeginDate());
                newCheckingChannel.setSelfCheckDay(parentChecking.getSelfCheckDay());
            } else {
                // 设置自查提醒
                if (defaultUpdateFreq != null) {
                    newCheckingChannel.setShouldSelfCheck(true);
                    Date selfCheckBeginDate = DateUtil.nearestPeriodBeginDate(new Date(), BEGIN_CHECK_DAY, defaultUpdateFreq.getValue());
                    newCheckingChannel.setSelfCheckBeginDate(DateUtil.toString(DateUtil.addDay(selfCheckBeginDate, -1 * defaultUpdateFreq.getValue())));
                    newCheckingChannel.setSelfCheckDay(defaultUpdateFreq.getValue());
                }
            }
        } else {
            // 设置
            FrequencyPreset preset = presetCache.get(freqSetup.getPresetFeqId());
            if (preset != null) {
                newCheckingChannel.setShouldIssueCheck(true);
                Date checkBeginDate = DateUtil.nearestPeriodBeginDate(new Date(), BEGIN_CHECK_DAY, preset.getUpdateFreq());
                newCheckingChannel.setBeginDateTime(DateUtil.toString(checkBeginDate));
                newCheckingChannel.setCheckDay(preset.getUpdateFreq());
                newCheckingChannel.setWarningDay(preset.getAlertFreq());
            }
        }

        SimpleTree.Node<CheckingChannel> child = new SimpleTree.Node<>(parent, newCheckingChannel);
        parent.addChild(child);

        // 递归添加子栏目
        if (curChnl.isHasChildren()) {
            // 查询整个栏目
            final List<Channel> childChannel = siteApiService.getChildChannel(0, curChnl.getChannelId(), "");
            if (childChannel == null || childChannel.isEmpty()) {
                return;
            }

            for (Channel chnl : childChannel) {
                try {
                    recursiveBuildChannelTree(chnl, child);
                } catch (Exception e) {
                    log.error("", e);
                }
            }
        }
    }

    /**
     * 检查所有栏目是否更新
     *
     * @param root
     */
    private void checkChannelTreeUpdate(SimpleTree.Node<CheckingChannel> root) {

        if (root == null || root.getData() == null) {
            return;
        }

        CheckingChannel checkingChannel = root.getData();
        if (checkingChannel.getChannel() != null) {

            boolean isUpdated = false;
            if (checkingChannel.isShouldIssueCheck()) {
                isUpdated = isChannelUpdated(checkingChannel.getChannel().getChannelId(), checkingChannel.getBeginDateTime());
                // 检查超时未更新和预警
                if (!isUpdated) {
                    // 本周期未更新，就看一下上个周期是否更新不及时
                    boolean isPrevPeroidUpdated = isChannelUpdated(checkingChannel.getChannel().getChannelId(), checkingChannel.getPrevPeroidBeginDate());
                    Date now = new Date();
                    if (isPrevPeroidUpdated) {
                        // 上一个周期已经更新了，检查是否需要预警
                        Date beginWarningDate = DateUtil.addDay(DateUtil.toDate(checkingChannel.getBeginDateTime()), checkingChannel.getCheckDay()-checkingChannel.getWarningDay());
                        if (now.compareTo(beginWarningDate) >= 0) {
                            checkingChannel.setWarning(true);
                        }
                    } else {
                        // 上一个周期就没有更新，那么就是更新不及时
                        checkingChannel.setIssue(true);
                    }
                } else {
                    // 本周期已经更新了，就不存在更新不及时和预警问题啊
                }
            }

            if (checkingChannel.isShouldSelfCheck()) {
                // 前一个周期已经更新，才对当前周期做自查更新
                isUpdated = isChannelUpdated(checkingChannel.getChannel().getChannelId(), checkingChannel.getSelfCheckBeginDate());
                if (!isUpdated) {
                    // 上一个周期开始都没有更新则这个周期开始，就要提醒
                    checkingChannel.setSelfWarning(true);
                } else {
                    // 已经更新，就不用提醒了
                }
            }
        }

        List<SimpleTree.Node<CheckingChannel>> children = root.getChildren();
        if (children != null && !children.isEmpty()) {
            for (SimpleTree.Node<CheckingChannel> node : children) {
                checkChannelTreeUpdate(node);
            }
        }
    }

    /**
     * 检查一个栏目是否更新
     * @param channelId 栏目ID
     * @return
     */
    private boolean isChannelUpdated(Integer channelId, String dateTime) {
        try {
            List<Integer> ids = documentApiService.getPublishDocIds("", siteId, channelId, dateTime);
            return ids != null && !ids.isEmpty();
        } catch (RemoteException e) {
            log.error("", e);
        }
        // NOTE: 异常情况下，先暂时不判定为未更新，以免发生错误，等下次的检查的时候再判定
        return true;
    }

    /**
     * 获取自我检查更新的起始时间
     * @param day
     * @return
     */
    private Date getSelfCheckBeginDate(int day) {
        Date startCheckDate = DateUtil.toDate(BEGIN_CHECK_DAY);
        Date beginDate = startCheckDate;
        Date now = new Date();
        // 检查上一个周期时间范围内是否更新了
        while (DateUtil.diffDay(now, beginDate) > day * 2) {
            beginDate = DateUtil.addDay(beginDate, day);
        }

        return beginDate;
    }

    /**
     * 获取自我检查更新的起始时间
     * @param day
     * @return
     */
    private Date getWarningCheckBeginDate(int day) {
        Date startCheckDate = DateUtil.toDate(BEGIN_CHECK_DAY);
        Date beginDate = startCheckDate;
        Date now = new Date();
        // 获取当前周期的检查
        while (DateUtil.diffDay(now, beginDate) > day) {
            beginDate = DateUtil.addDay(beginDate, day);
        }

        return beginDate;
    }

    /**
     * 把监测出来的问题，插入到数据库中
     *
     * @param siteTrees
     */
    private void insertIssueAndWarning(List<SimpleTree<CheckingChannel>> siteTrees) {
        if (siteTrees == null || siteTrees.isEmpty()) {
            return;
        }

        for (SimpleTree<CheckingChannel> tree : siteTrees) {
            final List<SimpleTree.Node<CheckingChannel>> children = tree.getRoot().getChildren();
            if (children == null || children.isEmpty()) {
                continue;
            }
            for (SimpleTree.Node<CheckingChannel> node : children) {
                recursiveInsertIssueAndWarning(node);
            }
        }
    }

    /**
     * 递归插入栏目及子栏目监测结果
     *
     * @param root
     */
    private void recursiveInsertIssueAndWarning(SimpleTree.Node<CheckingChannel> root) {
        if (root == null) {
            return;
        }

        insertOneCheckingChannel(root);

        if (root.getChildren() == null || root.getChildren().isEmpty()) {
            return;
        }

        for (SimpleTree.Node<CheckingChannel> child : root.getChildren()) {
            recursiveInsertIssueAndWarning(child);
        }
    }

    /**
     * 插入一个栏目的检测结果
     * @param node
     */
    private void insertOneCheckingChannel(SimpleTree.Node<CheckingChannel> node) {
        if (node == null) {
            return;
        }

        if (node.getData() != null && node.getData().getChannel() != null) {
            CheckingChannel checking = node.getData();
            if (checking.isShouldIssueCheck()
                    && setupCache.get(checking.getChannel().getChannelId()) != null) {

                if (checking.isIssue()) {
                    // 查看子栏目是否更新了，如果子栏目更新，则父栏目也算更新了
                    if (!isChildChannelUpdated(node)) {
                        // 插入一条更新不及时的记录
                        insertToDB(checking.getChannel().getChannelId(),
                                Types.IssueType.INFO_UPDATE_ISSUE.value,
                                Types.InfoUpdateIssueType.UPDATE_NOT_INTIME.value);
                    }
                } else if (checking.isWarning()) {
                    insertToDB(checking.getChannel().getChannelId(),
                            Types.IssueType.INFO_UPDATE_WARNING.value,
                            Types.InfoUpdateWarningType.UPDATE_WARNING.value);
                } else {
                    // 父栏目有更新，无预警，不插入任何记录
                }

            } else if (checking.isShouldSelfCheck() && checking.isSelfWarning()) {
                // 插入一条自查提醒
                insertToDB(checking.getChannel().getChannelId(),
                        Types.IssueType.INFO_UPDATE_WARNING.value,
                        Types.InfoUpdateWarningType.SELF_CHECK_WARNING.value);
            }
        }
    }

    /**
     * 插入到数据库
     *
     * @param channelId
     * @param issueTypeId
     * @param subIssueTypeId
     */
    private void insertToDB(int channelId, int issueTypeId, int subIssueTypeId) {
        InfoUpdateResponse update = new InfoUpdateResponse();
        update.setSiteId(siteId);
        update.setIssueTypeId(subIssueTypeId);
        update.setCheckTime(new Date());
        update.setChnlId(channelId);
        try {
            update.setChnlUrl(siteApiService.getChannelPublishUrl("", 0, channelId));
        } catch (Exception e) {
            log.error("", e);
        }
        infoUpdateMapper.insert(issueTypeId, update);
    }

    /**
     * 递归判定子栏目是否已更新
     * @param parent
     * @return
     */
    private boolean isChildChannelUpdated(SimpleTree.Node<CheckingChannel> parent) {

        if (parent.getChildren() == null || parent.getChildren().isEmpty()) {
            return false;
        }

        for (SimpleTree.Node<CheckingChannel> child : parent.getChildren()) {
            if (child != null && child.getData() != null) {
                // 只判定没有单独设置监控频率的子栏目
                if (child.getData().isShouldIssueCheck()
                        && !child.getData().isIssue()) {
                    return true;
                } else {
                    // child更新不及时
                    boolean isUpdated = isChildChannelUpdated(child);
                    if (isUpdated) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    /**
     * 递归判定子栏目是否有不预警的
     * @param parent
     * @return
     */
    private boolean hasNoWarningChildChannel(SimpleTree.Node<CheckingChannel> parent) {

        if (parent.getChildren() == null || parent.getChildren().isEmpty()) {
            return true;
        }

        for (SimpleTree.Node<CheckingChannel> child : parent.getChildren()) {
            if (child != null && child.getData() != null) {
                // 只判定没有单独设置监控频率的子栏目
                if ( setupCache.get(child.getData().getChannel().getChannelId()) == null
                        && child.getData().isShouldIssueCheck() && !child.getData().isWarning()) {
                    return true;
                } else {
                    return isChildChannelUpdated(child);
                }
            }
        }

        return true;
    }

}
