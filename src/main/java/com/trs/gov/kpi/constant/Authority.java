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

    //批量操作
    public static final String PERFORMANCE_OPERATE = "performance.operate";
    public static final String PERFORMANCE_OPERATE_HANDLE = "performance.operate.handle";//处理
    public static final String PERFORMANCE_OPERATE_IGNORE = "performance.operate.ignore";//忽略
    public static final String PERFORMANCE_OPERATE_DELETE = "performance.operate.delete";//删除

    //待解决问题
    public static final String PERFORMANCE_ISSUE = "performance.issue";
    public static final String PERFORMANCE_ISSUE_SEARCH = "performance.issue.search";//查询

    //待解决预警
    public static final String PERFORMANCE_WARNING = "performance.warning";
    public static final String PERFORMANCE_WARNING_SEARCH = "performance.warning.search";//查询

    //已解决问题和预警
    public static final String PERFORMANCE_RESOLVED = "performance.resolved";
    public static final String PERFORMANCE_RESOLVED_FINDHANDLED = "performance.resolved.findhandled";//查询已处理
    public static final String PERFORMANCE_RESOLVED_FINDIGNORED = "performance.resolved.findignored";//查询已忽略


    //网站可用性
    public static final String PERFORMANCE_AVAILABILITY = "performance.availability";
    public static final String PERFORMANCE_AVAILABILITY_COUNT = "performance.availability.count";//统计数查询
    public static final String PERFORMANCE_AVAILABILITY_HISTORYCOUNT = "performance.availability.history";//统计数趋势查询
    public static final String PERFORMANCE_AVAILABILITY_HOME = "performance.availability.home";//首页可用性查询
    public static final String PERFORMANCE_AVAILABILITY_SEARCH = "performance.availability.search";//查询

    //信息更新
    public static final String PERFORMANCE_INFOUPDATE = "performance.infoupdate";
    public static final String PERFORMANCE_INFOUPDATE_COUNT = "performance.infoupdate.count";//统计数查询
    public static final String PERFORMANCE_INFOUPDATE_HISTORY = "performance.infoupdate.history";//统计数趋势查询
    public static final String PERFORMANCE_INFOUPDATE_SEARCH = "performance.infoupdate.search";//查询

    //服务实用
    public static final String PERFORMANCE_SERVICE = "performance.service";
    public static final String PERFORMANCE_SERVICE_SEARCH = "performance.service.search";//查询

    //信息错误
    public static final String PERFORMANCE_INFOERROR = "performance.infoerror";
    public static final String PERFORMANCE_INFOERROR_COUNT = "performance.infoerror.count";//统计数查询
    public static final String PERFORMANCE_INFOERROR_HISTORY = "performance.infoerror.history";//统计数趋势查询
    public static final String PERFORMANCE_INFOERROR_SEARCH = "performance.infoerror.search";//查询

    //访问优化
    public static final String PERFORMANCE_IMPROVE = "performance.improve";
    public static final String PERFORMANCE_IMPROVE_COUNT = "performance.improve.count";//统计
    public static final String PERFORMANCE_IMPROVE_SEARCH = "performance.improve.search";//查询
    public static final String PERFORMANCE_IMPROVE_HANDLE = "performance.improve.handle";//批量处理
    public static final String PERFORMANCE_IMPROVE_IGNORE = "performance.improve.ignore";//批量忽略
    public static final String PERFORMANCE_IMPROVE_DELETE = "performance.improve.delete";//批量删除

    //用户分析
    public static final String PERFORMANCE_ANALYSIS = "performance.analysis";
    public static final String PERFORMANCE_ANALYSIS_VIEWS = "performance.analysis.views";//访问量查询
    public static final String PERFORMANCE_ANALYSIS_STAYTIME = "performance.analysis.staytime";//停留时间查询

    //统计分析
    public static final String PERFORMANCE_STATISTICS = "performance.statistics";
    public static final String PERFORMANCE_STATISTICS_ISSUE = "performance.statistics.issue";//问题查询
    public static final String PERFORMANCE_STATISTICS_DOCUMENT = "performance.statistics.document";//稿件查询

    //监测设置
    public static final String PERFORMANCE_MONITORSETUP = "performance.monitorsetup";
    public static final String PERFORMANCE_MONITORSETUP_FINDADMIN = "performance.monitorsetup.findadmin";//查询保障人员
    public static final String PERFORMANCE_MONITORSETUP_UPDATEADMIN = "performance.monitorsetup.updateadmin";//设置保障人员
    public static final String PERFORMANCE_MONITORSETUP_SEARCH = "performance.monitorsetup.search";//查询监测参数
    public static final String PERFORMANCE_MONITORSETUP_SAVE = "performance.monitorsetup.save";//保存监测参数
    public static final String PERFORMANCE_MONITORSETUP_FINDFREQ = "performance.monitorsetup.findfreq";//查询频率

    //指标参数设置
    public static final String PERFORMANCE_INDEXSETUP = "performance.indexsetup";
    public static final String PERFORMANCE_INDEXSETUP_FINDPREFREQ = "performance.indexsetup.findprefreq";//查询预设频率
    public static final String PERFORMANCE_INDEXSETUP_ADDPREFREQ = "performance.indexsetup.addprefreq";//增加预设频率
    public static final String PERFORMANCE_INDEXSETUP_UPDATEPREFREQ = "performance.indexsetup.updateprefreq";//更新预设频率
    public static final String PERFORMANCE_INDEXSETUP_DELETEPREFREQ = "performance.indexsetup.deleteprefreq";//删除预设频率
    public static final String PERFORMANCE_INDEXSETUP_FINDDEMANDFREQ = "performance.indexsetup.finddemandfreq";//查询按需更新频率
    public static final String PERFORMANCE_INDEXSETUP_UPDATEDEMANDFREQ = "performance.indexsetup.updatedemandfreq";//修改按需更新频率
    public static final String PERFORMANCE_INDEXSETUP_FINDCHNL = "performance.indexsetup.findchnl";//查询栏目
    public static final String PERFORMANCE_INDEXSETUP_FINDMONITORCHNL = "performance.indexsetup.findmonitorchnl";//查询监测栏目
    public static final String PERFORMANCE_INDEXSETUP_ADDMONITORCHNL = "performance.indexsetup.addmonitorchnl";//添加监测栏目
    public static final String PERFORMANCE_INDEXSETUP_UPDATEMONITORCHNL = "performance.indexsetup.updatemonitorchnl";//修改监测栏目的频率
    public static final String PERFORMANCE_INDEXSETUP_DELMONITORCHNL = "performance.indexsetup.delmonitorchnl";//删除监测栏目
    public static final String PERFORMANCE_INDEXSETUP_ENABLEDMONITORCHNL = "performance.indexsetup.enabledmonitorchnl";//栏目监测是否启用
    public static final String PERFORMANCE_INDEXSETUP_FINDTYPE = "performance.indexsetup.findtype";//查询分类
    public static final String PERFORMANCE_INDEXSETUP_FINDCHNLBYTYPE = "performance.indexsetup.findchnlbytype";//根据分类查栏目
    public static final String PERFORMANCE_INDEXSETUP_ADDCHNLTOTYPE = "performance.indexsetup.addchnltotype";//栏目分配到所属分类
    public static final String PERFORMANCE_INDEXSETUP_UPDATETYPEOFCHNL = "performance.indexsetup.updatetypeofchnl";//修改栏目所属分类
    public static final String PERFORMANCE_INDEXSETUP_DELCHNLFROMTYPE = "performance.indexsetup.delchnlfromtype";//从分类中移除栏目

    //报表
    public static final String PERFORMANCE_REPORT = "performance.report";
    public static final String PERFORMANCE_REPORT_SEARCH = "performance.report.search";//查询
    public static final String PERFORMANCE_REPORT_EXPORT = "performance.report.export";//导出


}
