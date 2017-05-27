package com.trs.gov.kpi.entity.outerapi;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.trs.gov.kpi.constant.Types;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by he.lang on 2017/5/24.
 */
public class ContentCheckResult {

    @Getter @Setter
    private Integer code;

    @Getter @Setter
    private String message;

    @Getter
    private String result;

    private JSONObject resultObj;

    public boolean isOk() {
        return code == 1;
    }

    public void setResult(String result) {
        if (result != null) {
            this.result = result;
            resultObj = JSON.parseObject(result);
        }
    }

    /**
     * 获取某一类问题的检测结果
     *
     * @param issueType
     * @return
     */
    public String getResultOfType(Types.InfoErrorIssueType issueType) {
        if (resultObj == null) {
            return null;
        }

        return resultObj.getString(issueType.name);
    }


}
