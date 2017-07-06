package com.trs.gov.kpi.constant;

/**
 * Created by linwei on 2017/5/27.
 */
public final class Constants {

    private Constants() {}

    public static final String INVALID_PARAMETER = "参数不合法！";

    public static final int ISSUE_BEGIN_ID = 1;

    public static final int ISSUE_END_ID = 50;

    public static final int WARNING_BEGIN_ID = 51;

    public static final int WARNING_END_ID = 100;

    // 数据库字段名id
    public static final String DB_FIELD_ID = "id";

    // 数据库字段名siteId
    public static final String DB_FIELD_SITE_ID = "siteId";

    //检索字段chnlName
    public static final String CHNL_NAME = "chnlName";

    //用于标识消息传输（接收和发送）的常量
    public static final String INPUT_CHANNEL = "input_channel";
    public static final String OUTPUT_CHANNEL = "output_channel";
    public static final String INPUT = "input";
    public static final String OUTPUT = "output";

    //用于issue表中的deptId（部门编号）为null或者字符串的空值情况
    public static final String DEPT_NULL = "未归属部门";

    //空字符串，用于问题中返回部门
    public static final String EMPTY_STRING = "";

}
