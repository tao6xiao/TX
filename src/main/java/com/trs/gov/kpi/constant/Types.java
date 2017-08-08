package com.trs.gov.kpi.constant;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by linwei on 2017/5/25.
 */
public final class Types {
    /**
     * 问题类型
     */
    public enum IssueType {
        INVALID(-1, "未知问题"),
        HOMEPAGE_AVAILABLE_ISSUE(0, "首页可用性问题"),
        LINK_AVAILABLE_ISSUE(1, "网站可用性问题"),
        INFO_UPDATE_ISSUE(2, "信息更新问题"),
        INFO_ERROR_ISSUE(3, "信息错误"),
        SERVICE_LINK_AVAILABLE(4, "服务链接"),
        INFO_UPDATE_WARNING(51, "信息更新预警"),
        RESPOND_WARNING(52, "互动回应预警");

        public final int value;

        @Getter
        private final String name;

        IssueType(int value, String name) {

            this.value = value;
            this.name = name;
        }

        public static IssueType valueOf(int value) {
            if (value <= 0) {
                return INVALID;
            }
            IssueType[] types = IssueType.values();
            for (IssueType type : types) {
                if (type.value == value) {
                    return type;
                }
            }
            return INVALID;
        }

    }


    /**
     * 链接可用性的子类型
     */
    public enum LinkAvailableIssueType {
        INVALID(-1, "未知问题"),
        INVALID_LINK(IssueType.LINK_AVAILABLE_ISSUE.value * 10 + 1, "链接失效"),
        INVALID_IMAGE(IssueType.LINK_AVAILABLE_ISSUE.value * 10 + 2, "图片失效"),
        CONNECTION_TIME_OUT(IssueType.LINK_AVAILABLE_ISSUE.value * 10 + 3, "连接超时"),
        INVALID_FILE(IssueType.LINK_AVAILABLE_ISSUE.value * 10 + 4, "附件失效"),
        INVALID_HOME_PAGE(IssueType.LINK_AVAILABLE_ISSUE.value * 10 + 5, "首页失效");

        public final int value;

        @Getter
        private final String name;

        LinkAvailableIssueType(int type, String name) {
            this.value = type;
            this.name = name;
        }

        public static LinkAvailableIssueType valueOf(int value) {
            if (value <= 0) {
                return INVALID;
            }
            LinkAvailableIssueType[] types = LinkAvailableIssueType.values();
            for (LinkAvailableIssueType type : types) {
                if (type.value == value) {
                    return type;
                }
            }
            return INVALID;
        }

        /**
         * 查找名字包含指定字符串的type，支持模糊查找
         *
         * @param name
         * @return
         */
        public static List<Integer> findByName(String name) {
            if (name == null || name.isEmpty()) {
                return new ArrayList<>();
            }
            List<Integer> result = new ArrayList<>();
            LinkAvailableIssueType[] types = LinkAvailableIssueType.values();
            for (LinkAvailableIssueType type : types) {
                if (type != INVALID && type.name.contains(name)) {
                    result.add(type.value);
                }
            }

            return result;
        }
    }

    /**
     * 服务链接可用性的子类型
     */
    public enum ServiceLinkIssueType {
        INVALID(-1, "未知问题"),
        INVALID_LINK(IssueType.SERVICE_LINK_AVAILABLE.value * 10 + 1, "服务链接失效");

        public final int value;

        @Getter
        private final String name;

        ServiceLinkIssueType(int type, String name) {
            this.value = type;
            this.name = name;
        }

        public static ServiceLinkIssueType valueOf(int value) {
            if (value <= 0) {
                return INVALID;
            }
            ServiceLinkIssueType[] types = ServiceLinkIssueType.values();
            for (ServiceLinkIssueType type : types) {
                if (type.value == value) {
                    return type;
                }
            }
            return INVALID;
        }

        /**
         * 查找名字包含指定字符串的type，支持模糊查找
         *
         * @param name
         * @return
         */
        public static List<Integer> findByName(String name) {
            if (name == null || name.isEmpty()) {
                return new ArrayList<>();
            }
            List<Integer> result = new ArrayList<>();
            ServiceLinkIssueType[] types = ServiceLinkIssueType.values();
            for (ServiceLinkIssueType type : types) {
                if (type != INVALID && type.name.contains(name)) {
                    result.add(type.value);
                }
            }

            return result;
        }
    }


    /**
     * 信息更新的子类型
     */
    public enum InfoUpdateIssueType {
        INVALID(-1, "未知问题"),
        UPDATE_NOT_INTIME(IssueType.INFO_UPDATE_ISSUE.value * 10 + 1, "更新不及时");

        public final int value;

        @Getter
        private final String name;

        InfoUpdateIssueType(int type, String name) {
            this.value = type;
            this.name = name;
        }

        public static InfoUpdateIssueType valueOf(int value) {
            if (value <= 0) {
                return INVALID;
            }
            InfoUpdateIssueType[] types = InfoUpdateIssueType.values();
            for (InfoUpdateIssueType type : types) {
                if (type.value == value) {
                    return type;
                }
            }
            return INVALID;
        }

        /**
         * 查找名字包含指定字符串的type，支持模糊查找
         *
         * @param name
         * @return
         */
        public static List<Integer> findByName(String name) {
            if (name == null || name.isEmpty()) {
                return new ArrayList<>();
            }
            List<Integer> result = new ArrayList<>();
            InfoUpdateIssueType[] types = InfoUpdateIssueType.values();
            for (InfoUpdateIssueType type : types) {
                if (type != INVALID && type.name.contains(name)) {
                    result.add(type.value);
                }
            }

            return result;
        }
    }


    /**
     * 信息错误的子类型
     */
    public enum InfoErrorIssueType {
        INVALID(-1, "未知问题", "未知"),
        TYPOS(IssueType.INFO_ERROR_ISSUE.value * 10 + 1, "错别字", "字词"),
        SENSITIVE_WORDS(IssueType.INFO_ERROR_ISSUE.value * 10 + 2, "敏感词", "敏感词");

        public final int value;

        @Getter
        private final String name;

        public final String checkType;

        InfoErrorIssueType(int type, String name, String checkType) {
            this.value = type;
            this.name = name;
            this.checkType = checkType;
        }

        public static InfoErrorIssueType valueOfName(String name) {
            for (InfoErrorIssueType type : InfoErrorIssueType.values()) {
                if (name.equals(type.name)) {
                    return type;
                }
            }
            return INVALID;
        }

        public static InfoErrorIssueType valueOfCheckType(String checkType) {
            for (InfoErrorIssueType type : InfoErrorIssueType.values()) {
                if (checkType.equals(type.checkType)) {
                    return type;
                }
            }
            return INVALID;
        }

        public static InfoErrorIssueType valueOf(int value) {
            if (value <= 0) {
                return INVALID;
            }
            InfoErrorIssueType[] types = InfoErrorIssueType.values();
            for (InfoErrorIssueType type : types) {
                if (type.value == value) {
                    return type;
                }
            }
            return INVALID;
        }

        public static List<String> getAllCheckTypes() {
            InfoErrorIssueType[] types = InfoErrorIssueType.values();
            List<String> checkTypeList = new ArrayList<>();
            for (InfoErrorIssueType type : types) {
                if (type != INVALID) {
                    checkTypeList.add(type.checkType);
                }
            }
            return checkTypeList;
        }

        /**
         * 查找名字包含指定字符串的type，支持模糊查找
         *
         * @param name
         * @return
         */
        public static List<Integer> findByName(String name) {
            if (name == null || name.isEmpty()) {
                return new ArrayList<>();
            }
            List<Integer> result = new ArrayList<>();
            InfoErrorIssueType[] types = InfoErrorIssueType.values();
            for (InfoErrorIssueType type : types) {
                if (type != INVALID && type.name.contains(name)) {
                    result.add(type.value);
                }
            }

            return result;
        }
    }

    /**
     * 信息预警子类型类型
     */
    public enum InfoUpdateWarningType {
        INVALID(-1, "未知问题"),
        UPDATE_WARNING(IssueType.INFO_UPDATE_WARNING.value * 10 + 1, "栏目更新预警"),
        SELF_CHECK_WARNING(IssueType.INFO_UPDATE_WARNING.value * 10 + 2, "信息自查预警");

        public final int value;

        @Getter
        private final String name;

        InfoUpdateWarningType(int type, String name) {
            this.value = type;
            this.name = name;
        }

        public static InfoUpdateWarningType valueOf(int value) {
            if (value <= 0) {
                return INVALID;
            }
            InfoUpdateWarningType[] types = InfoUpdateWarningType.values();
            for (InfoUpdateWarningType type : types) {
                if (type.value == value) {
                    return type;
                }
            }
            return INVALID;
        }

        /**
         * 查找名字包含指定字符串的type，支持模糊查找
         *
         * @param name
         * @return
         */
        public static List<Integer> findByName(String name) {
            if (name == null || name.isEmpty()) {
                return new ArrayList<>();
            }
            List<Integer> result = new ArrayList<>();
            InfoUpdateWarningType[] types = InfoUpdateWarningType.values();
            for (InfoUpdateWarningType type : types) {
                if (type != INVALID && type.name.contains(name)) {
                    result.add(type.value);
                }
            }

            return result;
        }
    }

    /**
     * 互动回应预警子类型
     */
    public enum RespondWarningType {
        INVALID(-1, "未知问题"),
        RESPOND_WARNING(IssueType.RESPOND_WARNING.value * 10 + 1, "咨询回应预警"),
        FEEDBACK_WARNING(IssueType.RESPOND_WARNING.value * 10 + 2, "征集反馈预警");

        public final int value;

        @Getter
        private final String name;

        RespondWarningType(int type, String name) {
            this.value = type;
            this.name = name;
        }

        public static RespondWarningType valueOf(int value) {
            if (value <= 0) {
                return INVALID;
            }
            RespondWarningType[] types = RespondWarningType.values();
            for (RespondWarningType type : types) {
                if (type.value == value) {
                    return type;
                }
            }
            return INVALID;
        }

        /**
         * 查找名字包含指定字符串的type，支持模糊查找
         *
         * @param name
         * @return
         */
        public static List<Integer> findByName(String name) {
            if (name == null || name.isEmpty()) {
                return new ArrayList<>();
            }
            List<Integer> result = new ArrayList<>();
            RespondWarningType[] types = RespondWarningType.values();
            for (RespondWarningType type : types) {
                if (type != INVALID && type.name.contains(name)) {
                    result.add(type.value);
                }
            }

            return result;
        }
    }

    public static String getSubTypeName(IssueType issueType, int subType) {
        switch (issueType) {
            case LINK_AVAILABLE_ISSUE:
                return LinkAvailableIssueType.valueOf(subType).name;
            case INFO_UPDATE_ISSUE:
                return InfoUpdateIssueType.valueOf(subType).name;
            case INFO_ERROR_ISSUE:
                return InfoErrorIssueType.valueOf(subType).name;
            case INFO_UPDATE_WARNING:
                return InfoUpdateWarningType.valueOf(subType).name;
            case RESPOND_WARNING:
                return RespondWarningType.valueOf(subType).name;
            case SERVICE_LINK_AVAILABLE:
                return ServiceLinkIssueType.valueOf(subType).name;
            default:
                return "未知问题";
        }
    }

    /**
     * 网页分析类型
     */
    public enum AnalysisType {
        INVALID(-1, "未知类型"),
        REPLY_SPEED(1, "响应速度"),
        OVERSIZE_PAGE(2, "过大页面"),
        OVER_DEEP_PAGE(3, "过深页面"),
        REPEAT_CODE(4, "冗余代码"),
        TOO_LONG_URL(5, "过长URL页面");

        public final int value;

        @Getter
        private final String name;

        AnalysisType(int value, String name) {

            this.value = value;
            this.name = name;
        }

        public static AnalysisType valueOf(int value) {
            if (value <= 0) {
                return INVALID;
            }
            AnalysisType[] types = AnalysisType.values();
            for (AnalysisType type : types) {
                if (type.value == value) {
                    return type;
                }
            }
            return INVALID;
        }

    }

    /**
     * 日志监测_任务类型
     */
    public enum MonitorRecordNameType {
        INVALID(-1, "未知类型"),
        TASK_CHECK_HOME_PAGE(1, "首页可用性监测任务"),
        TASK_CHECK_LINK(2, "全站失效链接监测任务"),
        TASK_CHECK_CONTENT(3, "信息错误监测任务"),
        TASK_CHECK_INFO_UPDATE(4, "栏目更新监测任务"),
        TASK_CALCULATE_PERFORMANCE(5, "绩效指数计算后台任务"),
        TASK_TIMENODE_REPORT_GENERATE(6, "按时间节点生成报表的后台任务"),
        TASK_TIMEINTERVAL_REPORT_GENERATE(7, "按时间区间生成报表的后台任务"),
        TASK_SERVICE_LINK(8, "服务连接监测任务");

        public final int value;

        @Getter
        private final String name;

        MonitorRecordNameType(int value, String name) {

            this.value = value;
            this.name = name;
        }

        public static MonitorRecordNameType valueOf(int value) {
            if (value <= 0) {
                return INVALID;
            }
            MonitorRecordNameType[] types = MonitorRecordNameType.values();
            for (MonitorRecordNameType type : types) {
                if (type.value == value) {
                    return type;
                }
            }
            return INVALID;
        }

    }

}
