package com.trs.gov.kpi.constant;

import com.trs.gov.kpi.utils.StringUtil;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by linwei on 2017/5/25.
 */
public final class Types {

    public interface CheckIssueType {
    }

    /**
     * 问题类型
     */
    public enum IssueType implements CheckIssueType {
        INVALID(-1, "未知问题"),
        LINK_AVAILABLE_ISSUE(1, "可用性问题"),
        INFO_UPDATE_ISSUE(2, "信息更新问题"),
        INFO_ERROR_ISSUE(3, "信息错误"),
        INFO_UPDATE_WARNING(51,"信息更新预警"),
        RESPOND_WARNING(52, "互动回应预警");

        public final int value;

        public final String name;

        private IssueType(int value, String name) {

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

        public static IssueType getIssueTypeByName(String name) {

            for(IssueType issueType: IssueType.values()) {

                if(StringUtils.equals(issueType.name, name)) {

                    return issueType;
                }
            }
            return null;
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

        public final String name;

        private LinkAvailableIssueType(int type, String name) {
            this.value = type;
            this.name = name;
        }

//        public static List<LinkAvailableIssueType> findTypesByName(String name) {
//            List<LinkAvailableIssueType> matchedType = new ArrayList<>();
//            LinkAvailableIssueType[] types = LinkAvailableIssueType.values();
//            for (LinkAvailableIssueType type : types) {
//                if (type.name.indexOf(name) >= 0) {
//                    matchedType.add(type);
//                }
//            }
//            return matchedType;
//        }

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
     * 信息更新的子类型
     */
    public enum InfoUpdateIssueType {
        INVALID(-1, "未知问题"),
        UPDATE_NOT_INTIME(IssueType.INFO_UPDATE_ISSUE.value * 10 + 1,"更新不及时");

        public final int value;

        public final String name;

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
        TYPOS(IssueType.INFO_ERROR_ISSUE.value * 10 + 1,"错别字", "字词"),
        SENSITIVE_WORDS(IssueType.INFO_ERROR_ISSUE.value * 10 + 2,"敏感词", "敏感词");

        public final int value;

        public final String name;

        public final String checkType;

        InfoErrorIssueType(int type, String name, String checkType) {
            this.value = type;
            this.name = name;
            this.checkType = checkType;
        }

        public static InfoErrorIssueType valueOfName(String name){
            for (InfoErrorIssueType type : InfoErrorIssueType.values()) {
                if(name.equals(type.name)){
                    return type;
                }
            }
            return INVALID;
        }

        public static InfoErrorIssueType valueOfCheckType(String checkType){
            for (InfoErrorIssueType type : InfoErrorIssueType.values()) {
                if(checkType.equals(type.checkType)){
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

        public final String name;

        private InfoUpdateWarningType(int type, String name) {
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
        RESPOND_WARNING(IssueType.RESPOND_WARNING.value * 10 + 1,"咨询回应预警"),
        FEEDBACK_WARNING(IssueType.RESPOND_WARNING.value * 10 + 2,"征集反馈预警");

        public final int value;

        public final String name;

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
             default:
                 return "未知问题";
         }
    }

}
