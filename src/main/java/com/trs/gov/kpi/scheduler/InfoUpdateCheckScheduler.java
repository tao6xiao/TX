package com.trs.gov.kpi.scheduler;

import com.trs.gov.kpi.constant.*;
import com.trs.gov.kpi.dao.CommonMapper;
import com.trs.gov.kpi.dao.FrequencyPresetMapper;
import com.trs.gov.kpi.dao.FrequencySetupMapper;
import com.trs.gov.kpi.dao.IssueMapper;
import com.trs.gov.kpi.entity.*;
import com.trs.gov.kpi.entity.check.CheckingChannel;
import com.trs.gov.kpi.entity.dao.DBUpdater;
import com.trs.gov.kpi.entity.dao.QueryFilter;
import com.trs.gov.kpi.entity.dao.Table;
import com.trs.gov.kpi.entity.exception.BizException;
import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.gov.kpi.entity.outerapi.Channel;
import com.trs.gov.kpi.service.DefaultUpdateFreqService;
import com.trs.gov.kpi.service.outer.DocumentApiService;
import com.trs.gov.kpi.service.outer.SiteApiService;
import com.trs.gov.kpi.service.outer.SiteChannelServiceHelper;
import com.trs.gov.kpi.utils.DBUtil;
import com.trs.gov.kpi.utils.DateUtil;
import com.trs.gov.kpi.utils.LogUtil;
import com.trs.gov.kpi.utils.SpringContextUtil;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.Serializable;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.*;

/**
 * Created by linwei on 2017/5/23.
 */

@Slf4j
@Component
@Scope("prototype")
public class InfoUpdateCheckScheduler implements SchedulerTask, Serializable {

    // 开始检查的第一个周期起始点
    private static final String BEGIN_CHECK_DAY = "2017-05-01 00:00:00";

    @Resource
    private transient SiteChannelServiceHelper siteChannelServiceHelper;

    @Resource
    private transient SiteApiService siteApiService;

    @Resource
    private transient DocumentApiService documentApiService;

    @Resource
    private transient FrequencySetupMapper frequencySetupMapper;

    @Resource
    private transient FrequencyPresetMapper frequencyPresetMapper;

    @Resource
    private transient DefaultUpdateFreqService defaultUpdateFreqService;

    @Resource
    private transient IssueMapper issueMapper;

    @Resource
    private transient CommonMapper commonMapper;

    @Getter
    @Setter
    private Integer siteId;

    @Getter
    @Setter
    private String baseUrl;

    @Setter
    @Getter
    private Boolean isTimeNode;

    // 检测时缓存频率设置
    private transient Map<Integer, FrequencySetup> setupCache;

    // 检测时缓存频率预设
    private transient Map<Integer, FrequencyPreset> presetCache;

    // 缓存自查更新频率
    private transient DefaultUpdateFreq defaultUpdateFreq;

    //信息(栏目)更新数量计数
    @Getter
    private int monitorResult = 0;

    //站点监测状态（0：自动监测；1：手动监测）
    @Setter
    @Getter
    private int monitorType;

    @Getter
    private EnumCheckJobType checkJobType = EnumCheckJobType.CHECK_INFO_UPDATE;

    @Override
    public void run() throws RemoteException, BizException {
        siteChannelServiceHelper = SpringContextUtil.getBean(SiteChannelServiceHelper.class);
        siteApiService = SpringContextUtil.getBean(SiteApiService.class);
        documentApiService = SpringContextUtil.getBean(DocumentApiService.class);
        frequencySetupMapper = SpringContextUtil.getBean(FrequencySetupMapper.class);
        frequencyPresetMapper = SpringContextUtil.getBean(FrequencyPresetMapper.class);
        defaultUpdateFreqService = SpringContextUtil.getBean(DefaultUpdateFreqService.class);
        issueMapper = SpringContextUtil.getBean(IssueMapper.class);
        commonMapper = SpringContextUtil.getBean(CommonMapper.class);

        List<SimpleTree<CheckingChannel>> siteTrees = buildChannelTree();

        // 优先从最下面的子栏目开始进行检查，然后再遍历上层栏目，得出结果进行数据库更新
        for (SimpleTree<CheckingChannel> tree : siteTrees) {
            List<SimpleTree.Node<CheckingChannel>> children = tree.getRoot().getChildren();
            if (children == null) {
                continue;
            }
            for (SimpleTree.Node<CheckingChannel> child : children) {
                try {
                    checkChannelTreeUpdate(child);
                } catch (ParseException e) {
                    String errorInfo = "任务[" + getName() + "]，检查站点siteId[" + siteId + "]，栏目channel[" + child + "]下的子栏目是否更新";
                    log.error(errorInfo, e);
                    LogUtil.addErrorLog(OperationType.REQUEST, ErrorType.REQUEST_FAILED, errorInfo, e);
                }
            }
        }
        insertIssueAndWarning(siteTrees);
    }

    @Override
    public String getName() {
        return SchedulerType.INFO_UPDATE_CHECK_SCHEDULER.toString();
    }

    private List<SimpleTree<CheckingChannel>> buildChannelTree() throws RemoteException {

        // 获取栏目的更新设置
        final List<FrequencySetup> frequencySetups = frequencySetupMapper
                .selectPageDataFrequencySetupList(siteId, 0, 100000);

        setupCache = new HashMap<>();
        presetCache = new HashMap<>();
        for (FrequencySetup setup : frequencySetups) {

            // 关闭状态不检测
            if (setup.getIsOpen() == null || setup.getIsOpen().intValue() == Status.Open.CLOSE.value) {
                continue;
            }

            setupCache.put(setup.getChnlId(), setup);
            if (presetCache.get(setup.getPresetFeqId()) == null) {
                FrequencyPreset preset = frequencyPresetMapper.selectById(siteId, setup.getPresetFeqId());
                presetCache.put(setup.getPresetFeqId(), preset);
            }
        }

        defaultUpdateFreq = defaultUpdateFreqService.getDefaultUpdateFreqBySiteId(siteId);

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
                LogUtil.addErrorLog(OperationType.REQUEST, ErrorType.REQUEST_FAILED, "构建栏目检树失败，siteId[" + siteId + "]", e);
            }
        }

        return tree;
    }

    /**
     * 递归添加子栏目
     *
     * @param curChnl
     * @param parent
     * @throws RemoteException
     */
    private void recursiveBuildChannelTree(
            Channel curChnl,
            SimpleTree.Node<CheckingChannel> parent) throws RemoteException, ParseException {

        if (curChnl == null) {
            return;
        }

        CheckingChannel newCheckingChannel = new CheckingChannel();
        newCheckingChannel.setChannel(curChnl);

        // 获取检查参数和频率
        FrequencySetup freqSetup = setupCache.get(curChnl.getChannelId());
        if (freqSetup == null) {
            setCheckingParamsOfDefaultChannel(newCheckingChannel, parent, defaultUpdateFreq);
        } else {
            setCheckingParamsOfCheckChannel(newCheckingChannel, freqSetup);
        }

        SimpleTree.Node<CheckingChannel> child = new SimpleTree.Node<>(parent, newCheckingChannel);
        parent.addChild(child);

        // 递归添加子栏目
        if (curChnl.isHasChildren()) {
            recursiveBuildChilds(curChnl, child);
        }
    }

    /**
     * 递归构建子栏目检查树
     *
     * @param curChnl
     * @param parent
     * @throws RemoteException
     */
    private void recursiveBuildChilds(Channel curChnl, SimpleTree.Node<CheckingChannel> parent) throws RemoteException {
        // 查询子栏目
        final List<Channel> childChannel = siteApiService.getChildChannel(0, curChnl.getChannelId(), "");
        if (childChannel == null || childChannel.isEmpty()) {
            return;
        }

        for (Channel channel : childChannel) {
            try {
                recursiveBuildChannelTree(channel, parent);
            } catch (Exception e) {
                log.error("", e);
                LogUtil.addErrorLog(OperationType.REQUEST, ErrorType.REMOTE_FAILED, "递归构建channelId[" + curChnl.getChannelId() + "]子栏目检查树，siteId[" + siteId + "]", e);
            }
        }
    }

    /**
     * 为设置了检测频率的栏目设置检测参数
     *
     * @param newCheckingChannel
     * @param freqSetup
     * @throws ParseException
     */
    private void setCheckingParamsOfCheckChannel(CheckingChannel newCheckingChannel, FrequencySetup freqSetup) throws ParseException {
        // 设置
        FrequencyPreset preset = presetCache.get(freqSetup.getPresetFeqId());
        if (preset != null) {
            newCheckingChannel.setShouldIssueCheck(true);
            Date checkBeginDate = DateUtil.nearestPeriodBeginDate(new Date(), BEGIN_CHECK_DAY, preset.getUpdateFreq());
            newCheckingChannel.setBeginDateTime(DateUtil.toString(checkBeginDate));
            newCheckingChannel.setCheckDay(preset.getUpdateFreq());
            newCheckingChannel.setWarningDay(preset.getAlertFreq());
        } else {
            log.error("freq setup data is invalid! freqId[" + freqSetup.getPresetFeqId() + "] doesn't have preset");
        }
    }

    /**
     * 为没有设置预警的栏目创建检测的相关参数
     *
     * @param newCheckingChannel
     * @param parent
     * @param defaultUpdateFreq
     */
    private void setCheckingParamsOfDefaultChannel(CheckingChannel newCheckingChannel, SimpleTree.Node<CheckingChannel> parent, DefaultUpdateFreq defaultUpdateFreq) throws ParseException {
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
    }

    /**
     * 检查所有栏目是否更新
     *
     * @param root
     */
    private void checkChannelTreeUpdate(SimpleTree.Node<CheckingChannel> root) throws ParseException {

        if (root == null || root.getData() == null) {
            return;
        }

        judgeIssueOrWarning(root.getData());

        List<SimpleTree.Node<CheckingChannel>> children = root.getChildren();
        if (children != null && !children.isEmpty()) {
            for (SimpleTree.Node<CheckingChannel> node : children) {
                checkChannelTreeUpdate(node);
            }
        }
    }


    /**
     * 判定是否为问题还是预警
     *
     * @param checkingChannel
     */
    private void judgeIssueOrWarning(CheckingChannel checkingChannel) throws ParseException {

        if (checkingChannel == null || checkingChannel.getChannel() == null) {
            return;
        }

        if (checkingChannel.isShouldIssueCheck()) {
            judgeIssueCheckResult(checkingChannel);
        }

        if (checkingChannel.isShouldSelfCheck()) {
            judgeSelfCheckResult(checkingChannel);
        }
    }

    /**
     * 判定自查提醒检查结果
     *
     * @param checkingChannel
     */
    private void judgeSelfCheckResult(CheckingChannel checkingChannel) {
        // 前一个周期已经更新，才对当前周期做自查更新
        boolean isUpdated = isChannelUpdated(checkingChannel.getChannel().getChannelId(), checkingChannel.getSelfCheckBeginDate());
        if (!isUpdated) {
            // 上一个周期开始都没有更新则这个周期开始，就要提醒
            checkingChannel.setSelfWarning(true);
        } else {
            // 已经更新，就不用提醒了
        }
    }

    /**
     * 判定错误监测的结果
     *
     * @param checkingChannel
     * @throws ParseException
     */
    private void judgeIssueCheckResult(CheckingChannel checkingChannel) throws ParseException {
        boolean isUpdated = isChannelUpdated(checkingChannel.getChannel().getChannelId(), checkingChannel.getBeginDateTime());
        // 检查超时未更新和预警
        if (!isUpdated) {
            // 本周期未更新，就看一下上个周期是否更新不及时
            boolean isPrevPeroidUpdated = isChannelUpdated(checkingChannel.getChannel().getChannelId(), checkingChannel.getPrevPeroidBeginDate());
            Date now = new Date();
            if (isPrevPeroidUpdated) {
                // 上一个周期已经更新了，检查是否需要预警
                Date beginWarningDate = DateUtil.addDay(DateUtil.toDate(checkingChannel.getBeginDateTime()), checkingChannel.getCheckDay() - checkingChannel.getWarningDay());
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

    /**
     * 检查一个栏目是否更新
     *
     * @param channelId 栏目ID
     * @return
     */
    private boolean isChannelUpdated(Integer channelId, String dateTime) {
        try {
            List<Integer> ids = documentApiService.getPublishDocIds("", siteId, channelId, dateTime);
            return ids != null && !ids.isEmpty();
        } catch (RemoteException e) {
            log.error("", e);
            LogUtil.addErrorLog(OperationType.REMOTE, ErrorType.REMOTE_FAILED, "检查一个栏目channelId[" + channelId + "]是否更新，siteId[" + siteId + "]", e);
        }
        // NOTE: 异常情况下，先暂时不判定为未更新，以免发生错误，等下次的检查的时候再判定
        return true;
    }

    /**
     * 把监测出来的问题，插入到数据库中
     *
     * @param siteTrees
     */
    private void insertIssueAndWarning(List<SimpleTree<CheckingChannel>> siteTrees) throws RemoteException {
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
    private void recursiveInsertIssueAndWarning(SimpleTree.Node<CheckingChannel> root) throws RemoteException {
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
     *
     * @param node
     */
    private void insertOneCheckingChannel(SimpleTree.Node<CheckingChannel> node) throws RemoteException {
        if (node == null || node.getData() == null || node.getData().getChannel() == null) {
            return;
        }

        CheckingChannel checking = node.getData();
        if (checking.isShouldIssueCheck() && hasSetup(checking.getChannel())) {
            insertUpdateIssue(checking, node);
        } else if (checking.isShouldSelfCheck() && checking.isSelfWarning()) {
            // 插入一条自查提醒
            QueryFilter filter = buildQueryFilter(checking.getChannel().getChannelId(),
                    Types.IssueType.INFO_UPDATE_WARNING.value,
                    Types.InfoUpdateWarningType.SELF_CHECK_WARNING.value,
                    checking.getSelfCheckBeginDate());
            if (isExist(filter)) {
                updateToDB(filter, checking.getChannel().getChannelId());
            } else {
                insertToDB(checking.getChannel().getChannelId(),
                        Types.IssueType.INFO_UPDATE_WARNING.value,
                        Types.InfoUpdateWarningType.SELF_CHECK_WARNING.value);
            }
        }
    }

    /**
     * 判定栏目是否设置了监测频率
     *
     * @param channel
     * @return
     */
    private boolean hasSetup(Channel channel) {
        return setupCache.get(channel.getChannelId()) != null;
    }

    /**
     * 插入更新问题
     *
     * @param checking
     * @param node
     */
    private void insertUpdateIssue(CheckingChannel checking, SimpleTree.Node<CheckingChannel> node) throws RemoteException {
        if (checking.isIssue()) {
            // 查看子栏目是否更新了，如果子栏目更新，则父栏目也算更新了
            if (!isChildChannelUpdated(node)) {
                // 插入一条更新不及时的记录
                QueryFilter filter = buildQueryFilter(checking.getChannel().getChannelId(),
                        Types.IssueType.INFO_UPDATE_ISSUE.value,
                        Types.InfoUpdateIssueType.UPDATE_NOT_INTIME.value,
                        checking.getBeginDateTime());
                if (isExist(filter)) {
                    updateToDB(filter, checking.getChannel().getChannelId());
                } else {
                    insertToDB(checking.getChannel().getChannelId(),
                            Types.IssueType.INFO_UPDATE_ISSUE.value,
                            Types.InfoUpdateIssueType.UPDATE_NOT_INTIME.value);
                }
            }
        } else if (checking.isWarning()) {
            QueryFilter filter = buildQueryFilter(checking.getChannel().getChannelId(),
                    Types.IssueType.INFO_UPDATE_WARNING.value,
                    Types.InfoUpdateWarningType.UPDATE_WARNING.value,
                    checking.getBeginDateTime());
            if (isExist(filter)) {
                updateToDB(filter, checking.getChannel().getChannelId());
            } else {
                insertToDB(checking.getChannel().getChannelId(),
                        Types.IssueType.INFO_UPDATE_WARNING.value,
                        Types.InfoUpdateWarningType.UPDATE_WARNING.value);
            }
        } else {
            // 父栏目有更新，无预警，不插入任何记录
        }
        insertEmptyColumn(checking);
    }

    /**
     * 插入空栏目
     */
    private void insertEmptyColumn(CheckingChannel checking) throws RemoteException {
        List<Integer> chnlIdList = siteChannelServiceHelper.getEmptyChannel(siteId);
        for (Integer chnlId : chnlIdList) {
            QueryFilter filter = buildQueryFilter(chnlId,
                    Types.IssueType.EMPTY_CHANNEL.value,
                    Types.EmptyChannelType.EMPTY_COLUMN.value,
                    checking.getBeginDateTime());
            if (isExist(filter)) {
                //若栏目记录已存在，则更新CHECK_TIME
                DBUpdater updater = new DBUpdater(Table.ISSUE.getTableName());
                updater.addField(IssueTableField.CHECK_TIME, new Date());
                commonMapper.update(updater, filter);
            } else {
                InfoUpdate update = new InfoUpdate();
                update.setSiteId(siteId);
                update.setChnlUrl("");
                update.setTypeId(Types.IssueType.EMPTY_CHANNEL.value);
                update.setSubTypeId(Types.EmptyChannelType.EMPTY_COLUMN.value);
                Date curDate = new Date();
                update.setIssueTime(curDate);
                update.setCheckTime(curDate);
                update.setChnlId(chnlId);
                issueMapper.insert(DBUtil.toRow(update));
            }
        }
        updateResolvedEmptyColumn(chnlIdList);
    }

    /**
     * 更新已解决空栏目的状态
     */
    void updateResolvedEmptyColumn(List<Integer> chnlIdList) {
        QueryFilter filter = buildQueryFilter(
                Types.IssueType.EMPTY_CHANNEL.value,
                Types.EmptyChannelType.EMPTY_COLUMN.value,
                Status.Resolve.UN_RESOLVED.value);
        List<Issue> issues = issueMapper.select(filter);
        List<Integer> chnlIdListInDB = new ArrayList<>();
        //获取数据库中空栏目记录的栏目ID集合
        for (Issue issue : issues) {
            try {
                chnlIdListInDB.add(Integer.valueOf(issue.getCustomer2()));
            } catch (Exception e) {
                log.error("", e.getMessage());
            }

        }
        //从数据库空栏目ID集合中去掉仍为空栏目的集合，余下为已处理的空栏目。
        chnlIdListInDB.removeAll(chnlIdList);

        resolveOldEmptyChnlIssue(chnlIdListInDB);
    }

    /**
     * 把已经不是空栏目的问题，变成resolved
     *
     * @param chnlIdListInDB
     */
    private void resolveOldEmptyChnlIssue(List<Integer> chnlIdListInDB) {
        QueryFilter filter;
        if (!chnlIdListInDB.isEmpty()) {
            List<String> resolvedChnlIds = new ArrayList<>();
            for (Integer resolvedChnlId : chnlIdListInDB) {
                resolvedChnlIds.add(String.valueOf(resolvedChnlId));
            }
            filter = buildQueryFilter(resolvedChnlIds,
                    Types.IssueType.EMPTY_CHANNEL.value,
                    Types.EmptyChannelType.EMPTY_COLUMN.value);
            DBUpdater updater = new DBUpdater(Table.ISSUE.getTableName());
            updater.addField(IssueTableField.IS_RESOLVED, IssueIndicator.SOLVED.value);
            commonMapper.update(updater, filter);
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
        InfoUpdate update = new InfoUpdate();
        update.setSiteId(siteId);
        update.setTypeId(issueTypeId);
        update.setSubTypeId(subIssueTypeId);
        Date curDate = new Date();
        update.setIssueTime(curDate);
        update.setCheckTime(curDate);
        update.setChnlId(channelId);

        try {
            final Integer deptId = siteChannelServiceHelper.findRelatedDept(channelId, "");
            update.setDeptId(deptId);
        } catch (RemoteException e) {
            String errorInfo = MessageFormat.format("获取栏目所属部门失败！[chnnelId={0}, siteId={1}]", channelId, siteId);
            log.error(errorInfo, e);
            LogUtil.addErrorLog(OperationType.REQUEST, ErrorType.REMOTE_FAILED, errorInfo, e);
        }

        try {
            update.setChnlUrl(siteApiService.getChannelPublishUrl("", 0, channelId));
        } catch (Exception e) {
            String errorInfo = MessageFormat.format("获取栏目发布URL失败！[chnnelId={0}, siteId={1}]", channelId, siteId);
            log.error(errorInfo, e);
            LogUtil.addErrorLog(OperationType.REQUEST, ErrorType.REMOTE_FAILED, errorInfo, e);
        }
        issueMapper.insert(DBUtil.toRow(update));
        monitorResult++;
    }

    /**
     * 判定更新不及时记录是否存在
     *
     * @return
     */
    private boolean isExist(@NonNull QueryFilter filter) {
        final List<Issue> issues = issueMapper.select(filter);
        return !issues.isEmpty();
    }

    /**
     * 构造查询过滤器
     *
     * @param channelId
     * @param issueTypeId
     * @param subIssueTypeId
     * @param beginDateTime
     * @return
     */
    private QueryFilter buildQueryFilter(int channelId, int issueTypeId, int subIssueTypeId, String beginDateTime) {
        QueryFilter filter = new QueryFilter(Table.ISSUE);
        filter.addCond(IssueTableField.SITE_ID, siteId);
        filter.addCond(IssueTableField.CUSTOMER2, String.valueOf(channelId));
        filter.addCond(IssueTableField.TYPE_ID, issueTypeId);
        filter.addCond(IssueTableField.SUBTYPE_ID, subIssueTypeId);
        filter.addCond(IssueTableField.CHECK_TIME, beginDateTime).setRangeBegin(true);
        return filter;
    }

    /**
     * 构造查询过滤器
     *
     * @param chnlIdList
     * @param issueTypeId
     * @param subIssueTypeId
     * @return
     */
    private QueryFilter buildQueryFilter(List<String> chnlIdList, int issueTypeId, int subIssueTypeId) {
        QueryFilter filter = new QueryFilter(Table.ISSUE);
        filter.addCond(IssueTableField.SITE_ID, siteId);
        filter.addCond(IssueTableField.CUSTOMER2, chnlIdList);
        filter.addCond(IssueTableField.TYPE_ID, issueTypeId);
        filter.addCond(IssueTableField.SUBTYPE_ID, subIssueTypeId);
        return filter;
    }

    /**
     * 构造查询过滤器
     *
     * @param issueTypeId
     * @param issueTypeId
     * @param subIssueTypeId
     * @return
     */
    private QueryFilter buildQueryFilter(int issueTypeId, int subIssueTypeId, int isResolved) {
        QueryFilter filter = new QueryFilter(Table.ISSUE);
        filter.addCond(IssueTableField.SITE_ID, siteId);
        filter.addCond(IssueTableField.TYPE_ID, issueTypeId);
        filter.addCond(IssueTableField.SUBTYPE_ID, subIssueTypeId);
        filter.addCond(IssueTableField.IS_RESOLVED, isResolved);
        return filter;
    }

    /**
     * 更新到数据库
     *
     * @param filter    查询条件
     * @param channelId 栏目id
     */
    private void updateToDB(QueryFilter filter, int channelId) {
        String chnlUrl = null;
        try {
            chnlUrl = siteApiService.getChannelPublishUrl("", 0, channelId);
        } catch (RemoteException e) {
            String errorInfo = MessageFormat.format("任务调度[{0}]，[siteId={1}, channelId={2}]", getName(), siteId, channelId);
            log.error(errorInfo, e);
            LogUtil.addErrorLog(OperationType.REMOTE, ErrorType.REMOTE_FAILED, errorInfo, e);
        }
        DBUpdater updater = new DBUpdater(Table.ISSUE.getTableName());
        if (chnlUrl != null) {
            updater.addField(IssueTableField.DETAIL, chnlUrl);
        }
        updater.addField(IssueTableField.CHECK_TIME, new Date());
        commonMapper.update(updater, filter);
        monitorResult++;
    }

    /**
     * 递归判定子栏目是否已更新
     *
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

}
