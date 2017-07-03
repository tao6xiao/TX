package com.trs.gov.kpi.constant;

/**
 * Created by ranwei on 2017/7/3.
 */
public class Authority {

    private Authority() {
    }

    public static class OprTypeKey {

        //站点
        public static final String SITE = "site";
        public static final String SITE_FIND = "site.find";//查询站点
        public static final String SITE_SWITCH = "site.switch";//切换站点


        //指标分析
        public static final String INDEX = "index";

        public static final String INDEX_MONITOR = "index.monitor";//综合实时监测
        public static final String INDEX_MONITOR_FIND = "index.monitor.find";//查询综合监测

        public static final String INDEX_ISSUE = "index.issue";//待解决问题
        public static final String INDEX_ISSUE_FIND = "index.issue.find";//查询
        public static final String INDEX_ISSUE_EXPORT = "index.issue.export";//导出
        public static final String INDEX_ISSUE_HANDLE = "index.issue.handle";//批量处理
        public static final String INDEX_ISSUE_IGNORE = "index.issue.ignore";//批量忽略
        public static final String INDEX_ISSUE_DELETE = "index.issue.delete";//批量删除

        public static final String INDEX_WARNING = "index.warning";//待解决预警
        public static final String INDEX_WARNING_FIND = "index.warning.find";//查询
        public static final String INDEX_WARNING_EXPORT = "index.warning.export";//导出
        public static final String INDEX_WARNINGE_HANDLE = "index.warning.handle";//批量处理
        public static final String INDEX_WARNING_IGNORE = "index.warning.ignore";//批量忽略
        public static final String INDEX_WARNING_DELETE = "index.warning.delete";//批量删除

        public static final String INDEX_RESOLVED = "index.resolved";//已解决问题和预警
        public static final String INDEX_RESOLVED_FINDHANDLED = "index.resolved.findhandled";//查询已处理
        public static final String INDEX_RESOLVED_FINDIGNORED = "index.resolved.findignored";//查询已忽略
        public static final String INDEX_RESOLVED_EXPORTHANDLED = "index.resolved.exporthandled";//导出已处理
        public static final String INDEX_RESOLVED_EXPORTIGNORED = "index.resolved.exportignored";//导出已忽略

        public static final String INDEX_AVAILABILITY = "index.availability";//网站可用性
        public static final String INDEX_AVAILABILITY_COUNT = "index.availability.count";//统计数
        public static final String INDEX_AVAILABILITY_HISTORYCOUNT = "index.availability.history";//统计数趋势
        public static final String INDEX_AVAILABILITY_HOME = "index.availability.home";//首页可用性查询
        public static final String INDEX_AVAILABILITY_FIND = "index.availability.find";//查询
        public static final String INDEX_AVAILABILITY_EXPORT = "index.availability.export";//导出
        public static final String INDEX_AVAILABILITY_HANDLE = "index.availability.handle";//批量处理
        public static final String INDEX_AVAILABILITY_IGNORE = "index.availability.ignore";//批量忽略
        public static final String INDEX_AVAILABILITY_DELETE = "index.availability.delete";//批量删除

        public static final String INDEX_INFOUPDATE = "index.infoupdate";//信息更新
        public static final String INDEX_INFOUPDATE_COUNT = "index.infoupdate.count";//统计数
        public static final String INDEX_INFOUPDATE_HISTORY = "index.infoupdate.history";//统计数趋势
        public static final String INDEX_INFOUPDATE_FIND = "index.infoupdate.find";//查询
        public static final String INDEX_INFOUPDATE_EXPORT = "index.infoupdate.export";//导出
        public static final String INDEX_INFOUPDATE_HANDLE = "index.infoupdate.handle";//批量处理
        public static final String INDEX_INFOUPDATE_IGNORE = "index.infoupdate.ignore";//批量忽略
        public static final String INDEX_INFOUPDATE_DELETE = "index.infoupdate.delete";//批量删除

        public static final String INDEX_SERVICE = "index.service";//服务实用
        public static final String INDEX_SERVICE_FIND = "index.service.find";//查询
        public static final String INDEX_SERVICE_EXPORT = "index.service.export";//导出
        public static final String INDEX_SERVICE_HANDLE = "index.service.handle";//批量处理
        public static final String INDEX_SERVICE_IGNORE = "index.service.ignore";//批量忽略
        public static final String INDEX_SERVICE_DELETE = "index.service.delete";//批量删除

        public static final String INDEX_INFOERROR = "index.infoerror";//信息错误
        public static final String INDEX_INFOERROR_COUNT = "index.infoerror.count";//统计数
        public static final String INDEX_INFOERROR_HISTORY = "index.infoerror.history";//统计数趋势
        public static final String INDEX_INFOERROR_FIND = "index.infoerror.find";//查询
        public static final String INDEX_INFOERROR_EXPORT = "index.infoerror.export";//导出
        public static final String INDEX_INFOERROR_HANDLE = "index.infoerror.handle";//批量处理
        public static final String INDEX_INFOERROR_IGNORE = "index.infoerror.ignore";//批量忽略
        public static final String INDEX_INFOERROR_DELETE = "index.infoerror.delete";//批量删除


        //访问分析
        public static final String ACCESS = "access";

        public static final String ACCESS_IMPROVE = "access.improve";//访问优化
        public static final String ACCESS_IMPROVE_COUNTSPEED = "access.improve.countspeed";//响应速度-->统计
        public static final String ACCESS_IMPROVE_FINDSPEED = "access.improve.findspeed";//响应速度-->查询
        public static final String ACCESS_IMPROVE_EXPORTSPEED = "access.improve.exportspeed";//响应速度-->导出
        public static final String ACCESS_IMPROVE_HANDLESPEED = "access.improve.handlespeed";//响应速度-->批量处理
        public static final String ACCESS_IMPROVE_IGNORESPEED = "access.improve.ignorespeed";//响应速度-->批量忽略
        public static final String ACCESS_IMPROVE_DELETESPEED = "access.improve.deletespeed";//响应速度-->批量删除
        public static final String ACCESS_IMPROVE_COUNTSSIZE = "access.improve.countsize";//页面大小-->统计
        public static final String ACCESS_IMPROVE_FINDSIZE = "access.improve.findsize";//页面大小-->查询
        public static final String ACCESS_IMPROVE_EXPORTSIZE = "access.improve.exportsize";//页面大小-->导出
        public static final String ACCESS_IMPROVE_HANDLESIZE = "access.improve.handlesize";//页面大小-->批量处理
        public static final String ACCESS_IMPROVE_IGNORESIZE = "access.improve.ignoresize";//页面大小-->批量忽略
        public static final String ACCESS_IMPROVE_DELETESIZE = "access.improve.deletesize";//页面大小-->批量删除
        public static final String ACCESS_IMPROVE_COUNTSDEPTH = "access.improve.countdepth";//页面深度-->统计
        public static final String ACCESS_IMPROVE_FINDDEPTH = "access.improve.finddepth";//页面深度-->查询
        public static final String ACCESS_IMPROVE_EXPORTDEPTH = "access.improve.exportdepth";//页面深度-->导出
        public static final String ACCESS_IMPROVE_HANDLEDEPTH = "access.improve.handledepth";//页面深度-->批量处理
        public static final String ACCESS_IMPROVE_IGNOREDEPTH = "access.improve.ignoredepth";//页面深度-->批量忽略
        public static final String ACCESS_IMPROVE_DELETEDEPTH = "access.improve.deletedepth";//页面深度-->批量删除
        public static final String ACCESS_IMPROVE_COUNTSLENGTH = "access.improve.countlength";//过长url-->统计
        public static final String ACCESS_IMPROVE_FINDLENGTH = "access.improve.findlength";//过长url-->查询
        public static final String ACCESS_IMPROVE_EXPORTLENGTH = "access.improve.exportlength";//过长url-->导出
        public static final String ACCESS_IMPROVE_HANDLELENGTH = "access.improve.handlelength";//过长url-->批量处理
        public static final String ACCESS_IMPROVE_IGNORELENGTH = "access.improve.ignorelength";//过长url-->批量忽略
        public static final String ACCESS_IMPROVE_DELETELENGTH = "access.improve.deletelength";//过长url-->批量删除

        public static final String ACCESS_ANALYSIS = "access.analysis";//用户分析
        public static final String ACCESS_ANALYSIS_VIEWS = "access.analysis.views";//访问量-->查询
        public static final String ACCESS_ANALYSIS_VIEWSHISTORY = "access.analysis.viewshistory";//访问量-->趋势查询
        public static final String ACCESS_ANALYSIS_TIME = "access.analysis.time";//停留时间-->查询
        public static final String ACCESS_ANALYSIS_TIMEHISTORY = "access.analysis.timehistory";//停留时间-->趋势查询


        //统计分析
        public static final String STATISTICS = "statistics";

        public static final String STATISTICS_ISSUE = "statistics.issue";//问题统计
        public static final String STATISTICS_ISSUE_SEARCH = "statistics.issue.search";//查询

        public static final String STATISTICS_DOC = "statistics.doc";//稿件统计
        public static final String STATISTICS_DOC_SEARCH = "statistics.doc.search";//查询


        //设置
        public static final String SETUP = "setup";

        public static final String SETUP_MONITOR = "setup.monitor";//监测设置
        public static final String SETUP_MONITOR_FINDADMIN = "setup.monitor.findadmin";//查询保障人员
        public static final String SETUP_MONITOR_UPDATEADMIN = "setup.monitor.updateadmin";//设置保障人员
        public static final String SETUP_MONITOR_SEARCH = "setup.monitor.search";//查询监测参数
        public static final String SETUP_MONITOR_SAVE = "setup.monitor.save";//保存监测参数
        public static final String SETUP_MONITOR_FINDFREQ = "setup.monitor.findfreq";//查询频率

        public static final String SETUP_INDEX = "setup.index";//指标参数设置
        public static final String SETUP_INDEX_FINDPREFREQ = "setup.index.findprefreq";//预设频率-->查询
        public static final String SETUP_INDEX_ADDPREFREQ = "setup.index.addprefreq";//预设频率-->增加
        public static final String SETUP_INDEX_UPDATEPREFREQ = "setup.index.updateprefreq";//预设频率-->更新
        public static final String SETUP_INDEX_DELETEPREFREQ = "setup.index.deleteprefreq";//预设频率-->删除
        public static final String SETUP_INDEX_FINDDEMANDFREQ = "setup.index.finddemandfreq";//按需更新频率-->查询
        public static final String SETUP_INDEX_UPDATEDEMANDFREQ = "setup.index.updatedemandfreq";//按需更新频率-->更新
        public static final String SETUP_INDEX_FINDCHNL = "setup.index.findchnl";//栏目监测-->查询栏目
        public static final String SETUP_INDEX_FINDMONITORCHNL = "setup.index.findmonitorchnl";//栏目监测-->查询监测栏目
        public static final String SETUP_INDEX_ADDMONITORCHNL = "setup.index.addmonitorchnl";//栏目监测-->添加监测栏目
        public static final String SETUP_INDEX_UPDATEMONITORCHNL = "setup.index.updatemonitorchnl";//栏目监测-->更新监测栏目的频率
        public static final String SETUP_INDEX_DELMONITORCHNL = "setup.index.delmonitorchnl";//栏目监测-->删除监测栏目
        public static final String SETUP_INDEX_ENABLEDMONITORCHNL = "setup.index.enabledmonitorchnl";//栏目监测-->栏目监测是否启用
        public static final String SETUP_INDEX_FINDTYPE = "setup.index.findtype";//栏目分类-->查询分类
        public static final String SETUP_INDEX_FINDCHNLBYTYPE = "setup.index.findchnlbytype";//栏目分类-->根据分类查栏目
        public static final String SETUP_INDEX_ADDCHNLTOTYPE = "setup.index.addchnltotype";//栏目分类-->栏目分配到所属分类
        public static final String SETUP_INDEX_UPDATETYPEOFCHNL = "setup.index.updatetypeofchnl";//栏目分类-->修改栏目所属分类
        public static final String SETUP_INDEX_DELCHNLFROMTYPE = "setup.index.delchnlfromtype";//栏目分类-->从分类中移除栏目


        //导出
        public static final String EXPORT = "export";

        public static final String EXPORT_TIMENODE = "export.timenode";//按时间节点
        public static final String EXPORT_TIMENODE_SEARCH = "export.timenode.search";//查询
        public static final String EXPORT_TIMENODE_EXPORT = "export.timenode.export";//导出

        public static final String EXPORT_TIMEINTERVAL = "export.timeinterval";//按时间区间
        public static final String EXPORT_TIMEINTERVAL_SEARCH = "export.timeinterval.search";//查询
        public static final String EXPORT_TIMEINTERVAL_EXPORT = "export.timeinterval.export";//导出



        private OprTypeKey() {
        }
    }



}
