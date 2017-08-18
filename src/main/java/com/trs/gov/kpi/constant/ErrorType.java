package com.trs.gov.kpi.constant;

/**
 * 错误类型
 * Created by he.lang on 2017/8/3.
 */
public class ErrorType {

    public static final String REQUEST_FAILED = "请求失败";

    public static final String REMOTE_FAILED = "外部接口调用失败";

    public static final String RUN_FAILED = "运行失败";

    public static final String BIZ_EXCEPTION = "业务异常";

    public static final String TASK_SCHEDULE_FAILED = "任务调度失败";

    public static final String DOWNLOAD_FAILED = "下载失败";

    private ErrorType(){}
}
