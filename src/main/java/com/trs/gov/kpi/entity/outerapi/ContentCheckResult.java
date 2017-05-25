package com.trs.gov.kpi.entity.outerapi;

import com.trs.gov.kpi.constant.Types;
import lombok.Data;

/**
 * Created by he.lang on 2017/5/24.
 */
@Data
public class ContentCheckResult {
    private Types.InfoErrorIssueType type;
    private String errorInfo;
}
