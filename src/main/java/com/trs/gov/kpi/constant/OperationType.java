package com.trs.gov.kpi.constant;

/**
 * Created by he.lang on 2017/7/26.
 */
// TODO REVIEW LINWEI 如果只是常量，可以用interface，更为好一些
public class OperationType {
    public static final String QUERY = "查询";

    public static final String ADD = "添加";

    public static final String DELETE = "删除";

    public static final String UPDATE = "修改";

    public static final String REQUEST = "请求";

    public static final String MONITOR = "监测";

    public static final String REMOTE = "接口调用";

    public static final String TASK_SCHEDULE = "任务调度";
	
	private OperationType() {
    }

}
