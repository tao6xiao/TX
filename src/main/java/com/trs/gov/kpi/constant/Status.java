package com.trs.gov.kpi.constant;

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

        private final String name;

        Open(int value, String name) {
            this.value = value;
            this.name = name;
        }

    }
}
