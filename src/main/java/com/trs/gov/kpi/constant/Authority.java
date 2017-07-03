package com.trs.gov.kpi.constant;

/**
 * 权限相关
 * Created by ranwei on 2017/7/3.
 */
public class Authority {

    private Authority() {
    }


    //绩效考核
    public static final String PERFORMANCE = "performance";


    //综合实时监测
    public static final String PERFORMANCE_MONITOR = "performance.monitor";
    public static final String PERFORMANCE_MONITOR_SEARCH = "performance.monitor.search";//查询

    //待解决问题
    public static final String PERFORMANCE_ISSUE = "performance.issue";
    public static final String PERFORMANCE_ISSUE_FIND = "performance.issue.find";//查询
    public static final String PERFORMANCE_ISSUE_HANDLE = "performance.issue.handle";//批量处理
    public static final String PERFORMANCE_ISSUE_IGNORE = "performance.issue.ignore";//批量忽略
    public static final String PERFORMANCE_ISSUE_DELETE = "performance.issue.delete";//批量删除

    //待解决预警
    public static final String PERFORMANCE_WARNING = "performance.warning";
    public static final String PERFORMANCE_WARNING_FIND = "performance.warning.find";//查询
    public static final String PERFORMANCE_WARNINGE_HANDLE = "performance.warning.handle";//批量处理
    public static final String PERFORMANCE_WARNING_IGNORE = "performance.warning.ignore";//批量忽略
    public static final String PERFORMANCE_WARNING_DELETE = "performance.warning.delete";//批量删除

    //已解决问题和预警
    public static final String PERFORMANCE_RESOLVED = "performance.resolved";
    public static final String PERFORMANCE_RESOLVED_FINDHANDLED = "performance.resolved.findhandled";//查询已处理
    public static final String PERFORMANCE_RESOLVED_FINDIGNORED = "performance.resolved.findignored";//查询已忽略


    //网站可用性
    public static final String PERFORMANCE_AVAILABILITY = "performance.availability";
    public static final String PERFORMANCE_AVAILABILITY_COUNT = "performance.availability.count";//统计数
    public static final String PERFORMANCE_AVAILABILITY_HISTORYCOUNT = "performance.availability.history";//统计数趋势
    public static final String PERFORMANCE_AVAILABILITY_HOME = "performance.availability.home";//首页可用性查询
    public static final String PERFORMANCE_AVAILABILITY_FIND = "performance.availability.find";//查询
    public static final String PERFORMANCE_AVAILABILITY_HANDLE = "performance.availability.handle";//批量处理
    public static final String PERFORMANCE_AVAILABILITY_IGNORE = "performance.availability.ignore";//批量忽略
    public static final String PERFORMANCE_AVAILABILITY_DELETE = "performance.availability.delete";//批量删除

    //信息更新
    public static final String PERFORMANCE_INFOUPDATE = "performance.infoupdate";
    public static final String PERFORMANCE_INFOUPDATE_COUNT = "performance.infoupdate.count";//统计数
    public static final String PERFORMANCE_INFOUPDATE_HISTORY = "performance.infoupdate.history";//统计数趋势
    public static final String PERFORMANCE_INFOUPDATE_FIND = "performance.infoupdate.find";//查询
    public static final String PERFORMANCE_INFOUPDATE_HANDLE = "performance.infoupdate.handle";//批量处理
    public static final String PERFORMANCE_INFOUPDATE_IGNORE = "performance.infoupdate.ignore";//批量忽略
    public static final String PERFORMANCE_INFOUPDATE_DELETE = "performance.infoupdate.delete";//批量删除

    //服务实用
    public static final String PERFORMANCE_SERVICE = "performance.service";
    public static final String PERFORMANCE_SERVICE_FIND = "performance.service.find";//查询
    public static final String PERFORMANCE_SERVICE_HANDLE = "performance.service.handle";//批量处理
    public static final String PERFORMANCE_SERVICE_IGNORE = "performance.service.ignore";//批量忽略
    public static final String PERFORMANCE_SERVICE_DELETE = "performance.service.delete";//批量删除

    //信息错误
    public static final String PERFORMANCE_INFOERROR = "performance.infoerror";
    public static final String PERFORMANCE_INFOERROR_COUNT = "performance.infoerror.count";//统计数
    public static final String PERFORMANCE_INFOERROR_HISTORY = "performance.infoerror.history";//统计数趋势
    public static final String PERFORMANCE_INFOERROR_FIND = "performance.infoerror.find";//查询
    public static final String PERFORMANCE_INFOERROR_HANDLE = "performance.infoerror.handle";//批量处理
    public static final String PERFORMANCE_INFOERROR_IGNORE = "performance.infoerror.ignore";//批量忽略
    public static final String PERFORMANCE_INFOERROR_DELETE = "performance.infoerror.delete";//批量删除

    //访问优化
    public static final String PERFORMANCE_IMPROVE = "performance.improve";
    public static final String PERFORMANCE_IMPROVE_COUNTSPEED = "performance.improve.countspeed";//响应速度-->统计
    public static final String PERFORMANCE_IMPROVE_FINDSPEED = "performance.improve.findspeed";//响应速度-->查询
    public static final String PERFORMANCE_IMPROVE_HANDLESPEED = "performance.improve.handlespeed";//响应速度-->批量处理
    public static final String PERFORMANCE_IMPROVE_IGNORESPEED = "performance.improve.ignorespeed";//响应速度-->批量忽略
    public static final String PERFORMANCE_IMPROVE_DELETESPEED = "performance.improve.deletespeed";//响应速度-->批量删除
    public static final String PERFORMANCE_IMPROVE_COUNTSSIZE = "performance.improve.countsize";//页面大小-->统计
    public static final String PERFORMANCE_IMPROVE_FINDSIZE = "performance.improve.findsize";//页面大小-->查询
    public static final String PERFORMANCE_IMPROVE_HANDLESIZE = "performance.improve.handlesize";//页面大小-->批量处理
    public static final String PERFORMANCE_IMPROVE_IGNORESIZE = "performance.improve.ignoresize";//页面大小-->批量忽略
    public static final String PERFORMANCE_IMPROVE_DELETESIZE = "performance.improve.deletesize";//页面大小-->批量删除
    public static final String PERFORMANCE_IMPROVE_COUNTSDEPTH = "performance.improve.countdepth";//页面深度-->统计
    public static final String PERFORMANCE_IMPROVE_FINDDEPTH = "performance.improve.finddepth";//页面深度-->查询
    public static final String PERFORMANCE_IMPROVE_HANDLEDEPTH = "performance.improve.handledepth";//页面深度-->批量处理
    public static final String PERFORMANCE_IMPROVE_IGNOREDEPTH = "performance.improve.ignoredepth";//页面深度-->批量忽略
    public static final String PERFORMANCE_IMPROVE_DELETEDEPTH = "performance.improve.deletedepth";//页面深度-->批量删除
    public static final String PERFORMANCE_IMPROVE_COUNTSLENGTH = "performance.improve.countlength";//过长url-->统计
    public static final String PERFORMANCE_IMPROVE_FINDLENGTH = "performance.improve.findlength";//过长url-->查询
    public static final String PERFORMANCE_IMPROVE_HANDLELENGTH = "performance.improve.handlelength";//过长url-->批量处理
    public static final String PERFORMANCE_IMPROVE_IGNORELENGTH = "performance.improve.ignorelength";//过长url-->批量忽略
    public static final String PERFORMANCE_IMPROVE_DELETELENGTH = "performance.improve.deletelength";//过长url-->批量删除

    //用户分析
    public static final String PERFORMANCE_ANALYSIS = "performance.analysis";
    public static final String PERFORMANCE_ANALYSIS_VIEWS = "performance.analysis.views";//访问量-->查询
    public static final String PERFORMANCE_ANALYSIS_STAYTIME = "performance.analysis.staytime";//停留时间-->查询

    //统计分析
    public static final String PERFORMANCE_STATISTICS = "performance.statistics";
    public static final String PERFORMANCE_STATISTICS_ISSUE = "performance.statistics.issue";//问题查询
    public static final String PERFORMANCE_STATISTICS_DOC = "performance.statistics.doc";//稿件查询

    //监测设置
    public static final String PERFORMANCE_MONITORSETUP = "performance.monitorsetup";
    public static final String PERFORMANCE_MONITORSETUP_FINDADMIN = "performance.monitorsetup.findadmin";//查询保障人员
    public static final String PERFORMANCE_MONITORSETUP_UPDATEADMIN = "performance.monitorsetup.updateadmin";//设置保障人员
    public static final String PERFORMANCE_MONITORSETUP_SEARCH = "performance.monitorsetup.search";//查询监测参数
    public static final String PERFORMANCE_MONITORSETUP_SAVE = "performance.monitorsetup.save";//保存监测参数
    public static final String PERFORMANCE_MONITORSETUP_FINDFREQ = "performance.monitorsetup.findfreq";//查询频率

    //指标参数设置
    public static final String PERFORMANCE_INDEXSETUP = "performance.indexsetup";
    public static final String PERFORMANCE_INDEXSETUP_FINDPREFREQ = "performance.indexsetup.findprefreq";//预设频率-->查询
    public static final String PERFORMANCE_INDEXSETUP_ADDPREFREQ = "performance.indexsetup.addprefreq";//预设频率-->增加
    public static final String PERFORMANCE_INDEXSETUP_UPDATEPREFREQ = "performance.indexsetup.updateprefreq";//预设频率-->更新
    public static final String PERFORMANCE_INDEXSETUP_DELETEPREFREQ = "performance.indexsetup.deleteprefreq";//预设频率-->删除
    public static final String PERFORMANCE_INDEXSETUP_FINDDEMANDFREQ = "performance.indexsetup.finddemandfreq";//按需更新频率-->查询
    public static final String PERFORMANCE_INDEXSETUP_UPDATEDEMANDFREQ = "performance.indexsetup.updatedemandfreq";//按需更新频率-->更新
    public static final String PERFORMANCE_INDEXSETUP_FINDCHNL = "performance.indexsetup.findchnl";//栏目监测-->查询栏目
    public static final String PERFORMANCE_INDEXSETUP_FINDMONITORCHNL = "performance.indexsetup.findmonitorchnl";//栏目监测-->查询监测栏目
    public static final String PERFORMANCE_INDEXSETUP_ADDMONITORCHNL = "performance.indexsetup.addmonitorchnl";//栏目监测-->添加监测栏目
    public static final String PERFORMANCE_INDEXSETUP_UPDATEMONITORCHNL = "performance.indexsetup.updatemonitorchnl";//栏目监测-->更新监测栏目的频率
    public static final String PERFORMANCE_INDEXSETUP_DELMONITORCHNL = "performance.indexsetup.delmonitorchnl";//栏目监测-->删除监测栏目
    public static final String PERFORMANCE_INDEXSETUP_ENABLEDMONITORCHNL = "performance.indexsetup.enabledmonitorchnl";//栏目监测-->栏目监测是否启用
    public static final String PERFORMANCE_INDEXSETUP_FINDTYPE = "performance.indexsetup.findtype";//栏目分类-->查询分类
    public static final String PERFORMANCE_INDEXSETUP_FINDCHNLBYTYPE = "performance.indexsetup.findchnlbytype";//栏目分类-->根据分类查栏目
    public static final String PERFORMANCE_INDEXSETUP_ADDCHNLTOTYPE = "performance.indexsetup.addchnltotype";//栏目分类-->栏目分配到所属分类
    public static final String PERFORMANCE_INDEXSETUP_UPDATETYPEOFCHNL = "performance.indexsetup.updatetypeofchnl";//栏目分类-->修改栏目所属分类
    public static final String PERFORMANCE_INDEXSETUP_DELCHNLFROMTYPE = "performance.indexsetup.delchnlfromtype";//栏目分类-->从分类中移除栏目

    //按时间节点导出
    public static final String PERFORMANCE_TIMENODE = "performance.timenode";
    public static final String PERFORMANCE_TIMENODE_SEARCH = "performance.timenode.search";//查询
    public static final String PERFORMANCE_TIMENODE_EXPORT = "performance.timenode.export";//导出

    //按时间区间导出
    public static final String PERFORMANCE_TIMEINTERVAL = "performance.timeinterval";
    public static final String PERFORMANCE_TIMEINTERVAL_SEARCH = "performance.timeinterval.search";//查询
    public static final String PERFORMANCE_TIMEINTERVAL_EXPORT = "performance.timeinterval.export";//导出


}
