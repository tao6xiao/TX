package com.trs.gov.kpi.constant;

/**
 * 权限相关
 * Created by ranwei on 2017/7/3.
 */
public class Authority {

    private Authority() {
    }
    public static final String NO_AUTHORITY = "权限不合法";

    //绩效考核
    public static final String KPIWEB = "kpiweb";


    //综合实时监测
    public static final String KPIWEB_MONITOR = "kpiweb.monitor";
    public static final String KPIWEB_MONITOR_SEARCH = "kpiweb.monitor.search";//查询

    //批量操作
    public static final String KPIWEB_OPERATE = "kpiweb.operate";
    public static final String KPIWEB_OPERATE_HANDLE = "kpiweb.operate.handle";//处理
    public static final String KPIWEB_OPERATE_IGNORE = "kpiweb.operate.ignore";//忽略
    public static final String KPIWEB_OPERATE_DELETE = "kpiweb.operate.delete";//删除
    public static final String KPIWEB_OPERATE_UPDATEDEPT = "kpiweb.operate.updatedept";//更新所属部门

    //待解决问题
    public static final String KPIWEB_ISSUE = "kpiweb.issue";
    public static final String KPIWEB_ISSUE_SEARCH = "kpiweb.issue.search";//查询

    //待解决预警
    public static final String KPIWEB_WARNING = "kpiweb.warning";
    public static final String KPIWEB_WARNING_SEARCH = "kpiweb.warning.search";//查询

    //已解决问题和预警
    public static final String KPIWEB_RESOLVED = "kpiweb.resolved";
    public static final String KPIWEB_RESOLVED_SEARCH = "kpiweb.resolved.search";//查询


    //网站可用性
    public static final String KPIWEB_AVAILABILITY = "kpiweb.availability";
    public static final String KPIWEB_AVAILABILITY_SEARCH = "kpiweb.availability.search";//查询

    //信息更新
    public static final String KPIWEB_INFOUPDATE = "kpiweb.infoupdate";
    public static final String KPIWEB_INFOUPDATE_SEARCH = "kpiweb.infoupdate.search";//查询

    //服务实用
    public static final String KPIWEB_SERVICE = "kpiweb.service";
    public static final String KPIWEB_SERVICE_SEARCH = "kpiweb.service.search";//查询

    //信息错误
    public static final String KPIWEB_INFOERROR = "kpiweb.infoerror";
    public static final String KPIWEB_INFOERROR_SEARCH = "kpiweb.infoerror.search";//查询

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

    //报表
    public static final String KPIWEB_REPORT = "kpiweb.report";
    public static final String KPIWEB_REPORT_SEARCH = "kpiweb.report.search";//查询
    public static final String KPIWEB_REPORT_EXPORT = "kpiweb.report.export";//导出


}
