package com.trs.gov.kpi.constant;

/**
 * 权限相关
 * Created by ranwei on 2017/7/3.
 */
public class Authority {

    private Authority() {
    }

    public static final String NO_AUTHORITY = "权限不合法";

    public static final String PLATFORM_ROLE_ID = "0";
    public static final String SITE_ROLE_ID = "103";

    //绩效考核
    public static final String KPIWEB = "kpiweb";


    //综合实时监测
    public static final String KPIWEB_MONITOR = "kpiweb.monitor";
    public static final String KPIWEB_MONITOR_SEARCH = "kpiweb.monitor.search";//查询

    //待解决问题
    public static final String KPIWEB_ISSUE = "kpiweb.issue";
    public static final String KPIWEB_ISSUE_SEARCH = "kpiweb.issue.search";//查询
    public static final String KPIWEB_ISSUE_HANDLE = "kpiweb.issue.handle";//处理
    public static final String KPIWEB_ISSUE_IGNORE = "kpiweb.issue.ignore";//忽略
    public static final String KPIWEB_ISSUE_DELETE = "kpiweb.issue.delete";//删除
    public static final String KPIWEB_ISSUE_UPDATEDEPT = "kpiweb.issue.updatedept";//更新所属部门

    //待解决预警
    public static final String KPIWEB_WARNING = "kpiweb.warning";
    public static final String KPIWEB_WARNING_SEARCH = "kpiweb.warning.search";//查询
    public static final String KPIWEB_WARNING_HANDLE = "kpiweb.warning.handle";//处理
    public static final String KPIWEB_WARNING_IGNORE = "kpiweb.warning.ignore";//忽略
    public static final String KPIWEB_WARNING_DELETE = "kpiweb.warning.delete";//删除
    public static final String KPIWEB_WARNING_UPDATEDEPT = "kpiweb.warning.updatedept";//更新所属部门

    //已解决问题和预警
    public static final String KPIWEB_RESOLVED = "kpiweb.resolved";
    public static final String KPIWEB_RESOLVED_SEARCH = "kpiweb.resolved.search";//查询


    //网站可用性
    public static final String KPIWEB_AVAILABILITY = "kpiweb.availability";
    public static final String KPIWEB_AVAILABILITY_SEARCH = "kpiweb.availability.search";//查询
    public static final String KPIWEB_AVAILABILITY_HANDLE = "kpiweb.availability.handle";//处理
    public static final String KPIWEB_AVAILABILITY_IGNORE = "kpiweb.availability.ignore";//忽略
    public static final String KPIWEB_AVAILABILITY_DELETE = "kpiweb.availability.delete";//删除
    public static final String KPIWEB_AVAILABILITY_UPDATEDEPT = "kpiweb.availability.updatedept";//更新所属部门

    //信息更新
    public static final String KPIWEB_INFOUPDATE = "kpiweb.infoupdate";
    public static final String KPIWEB_INFOUPDATE_SEARCH = "kpiweb.infoupdate.search";//查询
    public static final String KPIWEB_INFOUPDATE_HANDLE = "kpiweb.infoupdate.handle";//处理
    public static final String KPIWEB_INFOUPDATE_IGNORE = "kpiweb.infoupdate.ignore";//忽略
    public static final String KPIWEB_INFOUPDATE_DELETE = "kpiweb.infoupdate.delete";//删除
    public static final String KPIWEB_INFOUPDATE_UPDATEDEPT = "kpiweb.infoupdate.updatedept";//更新所属部门

    //服务实用
    public static final String KPIWEB_SERVICE = "kpiweb.service";
    public static final String KPIWEB_SERVICE_SEARCH = "kpiweb.service.search";//查询
    public static final String KPIWEB_SERVICE_HANDLE = "kpiweb.service.handle";//处理
    public static final String KPIWEB_SERVICE_IGNORE = "kpiweb.service.ignore";//忽略
    public static final String KPIWEB_SERVICE_DELETE = "kpiweb.service.delete";//删除
    public static final String KPIWEB_SERVICE_UPDATEDEPT = "kpiweb.service.updatedept";//更新所属部门

    //信息错误
    public static final String KPIWEB_INFOERROR = "kpiweb.infoerror";
    public static final String KPIWEB_INFOERROR_SEARCH = "kpiweb.infoerror.search";//查询
    public static final String KPIWEB_INFOERROR_HANDLE = "kpiweb.infoerror.handle";//处理
    public static final String KPIWEB_INFOERROR_IGNORE = "kpiweb.infoerror.ignore";//忽略
    public static final String KPIWEB_INFOERROR_DELETE = "kpiweb.infoerror.delete";//删除
    public static final String KPIWEB_INFOERROR_UPDATEDEPT = "kpiweb.infoerror.updatedept";//更新所属部门

    //访问优化
    public static final String KPIWEB_IMPROVE = "kpiweb.improve";
    public static final String KPIWEB_IMPROVE_SEARCH = "kpiweb.improve.search";//查询
    public static final String KPIWEB_IMPROVE_HANDLE = "kpiweb.improve.handle";//批量处理
    public static final String KPIWEB_IMPROVE_IGNORE = "kpiweb.improve.ignore";//批量忽略
    public static final String KPIWEB_IMPROVE_DELETE = "kpiweb.improve.delete";//批量删除

    //用户分析
    public static final String KPIWEB_ANALYSIS = "kpiweb.analysis";
    public static final String KPIWEB_ANALYSIS_VIEWS = "kpiweb.analysis.views";//访问量查询
    public static final String KPIWEB_ANALYSIS_STAYTIME = "kpiweb.analysis.staytime";//停留时间查询

    //统计分析
    public static final String KPIWEB_STATISTICS = "kpiweb.statistics";
    public static final String KPIWEB_STATISTICS_ISSUE = "kpiweb.statistics.issue";//问题查询
    public static final String KPIWEB_STATISTICS_DOCUMENT = "kpiweb.statistics.document";//稿件查询

    //监测设置
    public static final String KPIWEB_MONITORSETUP = "kpiweb.monitorsetup";
    public static final String KPIWEB_MONITORSETUP_SEARCH = "kpiweb.monitorsetup.search";//查询
    public static final String KPIWEB_MONITORSETUP_UPDATEADMIN = "kpiweb.monitorsetup.updateadmin";//设置保障人员
    public static final String KPIWEB_MONITORSETUP_SAVE = "kpiweb.monitorsetup.save";//保存监测参数

    //指标参数设置
    public static final String KPIWEB_INDEXSETUP = "kpiweb.indexsetup";
    public static final String KPIWEB_INDEXSETUP_SEARCH = "kpiweb.indexsetup.search";//查询
    public static final String KPIWEB_INDEXSETUP_ADDPREFREQ = "kpiweb.indexsetup.addprefreq";//增加预设频率
    public static final String KPIWEB_INDEXSETUP_UPDATEPREFREQ = "kpiweb.indexsetup.updateprefreq";//更新预设频率
    public static final String KPIWEB_INDEXSETUP_DELETEPREFREQ = "kpiweb.indexsetup.deleteprefreq";//删除预设频率
    public static final String KPIWEB_INDEXSETUP_UPDATEDEMANDFREQ = "kpiweb.indexsetup.updatedemandfreq";//修改按需更新频率
    public static final String KPIWEB_INDEXSETUP_ADDMONITORCHNL = "kpiweb.indexsetup.addmonitorchnl";//添加监测栏目
    public static final String KPIWEB_INDEXSETUP_UPDATEMONITORCHNL = "kpiweb.indexsetup.updatemonitorchnl";//修改监测栏目的频率
    public static final String KPIWEB_INDEXSETUP_DELMONITORCHNL = "kpiweb.indexsetup.delmonitorchnl";//删除监测栏目
    public static final String KPIWEB_INDEXSETUP_ENABLEDMONITORCHNL = "kpiweb.indexsetup.enabledmonitorchnl";//栏目监测是否启用
    public static final String KPIWEB_INDEXSETUP_ADDCHNLTOTYPE = "kpiweb.indexsetup.addchnltotype";//栏目分配到所属分类
    public static final String KPIWEB_INDEXSETUP_UPDATETYPEOFCHNL = "kpiweb.indexsetup.updatetypeofchnl";//修改栏目所属分类
    public static final String KPIWEB_INDEXSETUP_DELCHNLFROMTYPE = "kpiweb.indexsetup.delchnlfromtype";//从分类中移除栏目
    public static final String KPIWEB_INDEXSETUP_DUTYDEPT = "kpiweb.indexsetup.dutydept";//为栏目分配责任部门
    public static final String KPIWEB_INDEXSETUP_DELDUTYDEPT = "kpiweb.indexsetup.deldutydept";//删除责任部门

    //报表
    public static final String KPIWEB_REPORT = "kpiweb.report";
    public static final String KPIWEB_REPORT_SEARCH = "kpiweb.report.search";//查询
    public static final String KPIWEB_REPORT_EXPORT = "kpiweb.report.export";//导出


}
