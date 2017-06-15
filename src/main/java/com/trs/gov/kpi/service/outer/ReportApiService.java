package com.trs.gov.kpi.service.outer;

import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.gov.kpi.utils.StringUtil;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by linwei on 2017/6/15.
 */
public interface ReportApiService {
    /**
     * 获取报告
     * @param param
     * @return
     */
    String getReport(ReportApiParam param) throws RemoteException;

    @Data
    class ReportApiParam {

        private String reportName;
        private String beginDate;
        private String endDate;
        private String fields;
        private String granularity;

        private Map<String, String> otherParams;

        private ReportApiParam(){
            otherParams = new HashMap<>();
        }

        public void validate() {
            if (StringUtil.isEmpty(reportName)) {
                throw new IllegalArgumentException("report name is empty!");
            }

            if (StringUtil.isEmpty(fields)) {
                throw new IllegalArgumentException("fields is empty!");
            }
        }
    }

    class ReportApiParamBuilder {

        private ReportApiParam param;

        private ReportApiParamBuilder() {
            this.param = new ReportApiParam();
        }

        public static ReportApiParamBuilder newBuilder() {
            return new ReportApiParamBuilder();
        }

        public ReportApiParamBuilder setReportName(String reportName) {
            this.param.setReportName(reportName);
            return this;
        }

        public ReportApiParamBuilder setBeginDate(String beginDate) {
            this.param.setBeginDate(beginDate);
            return this;
        }

        public ReportApiParamBuilder setEndDate(String endDate) {
            this.param.setEndDate(endDate);
            return this;
        }

        public ReportApiParamBuilder setDimensionFields(String fields) {
            this.param.setFields(fields);
            return this;
        }

        public ReportApiParamBuilder setGranularity(String granularity) {
            this.param.setGranularity(granularity);
            return this;
        }

        public ReportApiParamBuilder addOtherParam(String key, String value) {
            this.param.getOtherParams().put(key, value);
            return this;
        }

        public ReportApiParam build() {
            this.param.validate();
            return this.param;
        }
    }
}
