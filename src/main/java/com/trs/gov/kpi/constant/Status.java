package com.trs.gov.kpi.constant;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by linwei on 2017/5/25.
 */
public class Status {

    /**
     * 枚举是否删除
     * Created by he.lang on 2017/5/19.
     */
    public enum Delete {

        UN_DELETE(0, "未删除"),
        DELETED(1, "已删除");

        public final int value;

        @Getter
        private final String name;

        Delete(int value, String name) {
            this.value = value;
            this.name = name;
        }
    }

    /**
     * 枚举是否解决
     * Created by he.lang on 2017/5/19.
     */
    public enum Resolve {
        UN_RESOLVED(0, "未解决"),
        RESOLVED(1, "已处理"),
        IGNORED(2, "已忽略");

        public final int value;

        @Getter
        private final String name;

        Resolve(int value, String name) {
            this.value = value;
            this.name = name;
        }
    }

    /**
     * 枚举是否开启，用于栏目更新频率设置
     * Created by he.lang on 2017/6/6.
     */
    public enum Open {
        CLOSE(0, "关闭"),
        OPEN(1, "开启");

        public final int value;

        @Getter
        private final String name;

        Open(int value, String name) {
            this.value = value;
            this.name = name;
        }

    }

    /**
     * 枚举工单状态
     * Created by he.lang on 2017/6/28.
     */
    public enum WorkOrder {
        INVALID(-1, "非法状态"),
        UN_HANDLE(0, "未转工单"),
        HANDLING(1, "已转工单");

        public final int value;

        @Getter
        private final String name;

        WorkOrder(int value, String name) {
            this.value = value;
            this.name = name;
        }

        public static WorkOrder valueOf(int value) {
            if (value < 0) {
                return INVALID;
            } else if (value == 0) {
                return UN_HANDLE;
            } else {
                return HANDLING;
            }
        }
    }

    /**
     * 站点监测状态类型
     */
    public enum MonitorStatusType {

        INVALID(-1, "未知类型"),
        DOING_CHECK(1, "正在检测"),
        CHECK_DONE(2, "检测完成"),
        CHECK_ERROR(3,"检测失败"),
        WAIT_CHECK(4,"等待检测");

        public final int value;

        @Getter
        private final String name;

        MonitorStatusType(int value, String name) {
            this.value = value;
            this.name = name;
        }

        public static MonitorStatusType valueOf(int value) {
            if(value <= 0 ){
                return INVALID;
            }

            MonitorStatusType[] types = MonitorStatusType.values();
            for (MonitorStatusType type : types) {
                if (type.value == value) {
                    return type;
                }
            }
            return null;
        }

        /**
         * 根据任务状态名称获取任务编号（支持模糊查询）
         * @param searchText
         * @return
         */
        public static List<Integer> getStatusByStatusName(String searchText){
            MonitorStatusType[] values = MonitorStatusType.values();
            List<Integer> statrsList = new ArrayList<>();
            for (Status.MonitorStatusType type : values) {
                if (type != INVALID && type.getName().contains(searchText)) {
                    int taskStatus = type.value;
                    statrsList.add(taskStatus);
                }
            }
            return statrsList;
        }
    }

    /**
     * 站点监测类型
     */
    public enum MonitorType {
        AUTO_MONITOR(0, "主动监测"),
        MANUAL_MONITOR(1, "手动监测");

        public final int value;

        @Getter
        private final String name;

        MonitorType(int value, String name) {
            this.value = value;
            this.name = name;
        }

    }


    /**
     * 检测状态
     */
    public enum MonitorState {
        NORMAL(1, "正常"),
        ABNORMAL(2, "异常");

        public final int value;

        @Getter
        private final String name;

        MonitorState(int value, String name) {
            this.value = value;
            this.name = name;
        }

    }
}
