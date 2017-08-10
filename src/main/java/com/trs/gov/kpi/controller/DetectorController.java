package com.trs.gov.kpi.controller;

import com.alibaba.fastjson.JSON;
import com.trs.gov.kpi.constant.Constants;
import com.trs.gov.kpi.constant.OperationType;
import com.trs.gov.kpi.entity.exception.BizException;
import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.gov.kpi.entity.outerapi.ContentCheckResult;
import com.trs.gov.kpi.entity.requestdata.CheckTextRequest;
import com.trs.gov.kpi.service.outer.ContentCheckApiService;
import com.trs.gov.kpi.utils.CollectionUtil;
import com.trs.gov.kpi.utils.LogUtil;
import com.trs.gov.kpi.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

/**
 * Created by linwei on 2017/6/14.
 */
@Slf4j
@RestController
@RequestMapping("/gov/detector")
public class DetectorController {

    private static final String[] ALL_CHECK_TYPES = new String[]{"字词", "语法", "词义", "政治", "敏感词"};

    @Resource
    ContentCheckApiService contentCheckApiService;

    /**
     * CKM校对封装接口， 为采编中心编辑器智能校对提供的接口
     *
     * @return
     */
    @RequestMapping(value = "/check/text", method = RequestMethod.POST)
    @ResponseBody
    public Object checkText(@RequestBody CheckTextRequest request) throws BizException, RemoteException {
        String logDesc = "执行ckm校对";
        return LogUtil.ControlleFunctionWrapper(() -> {
            if (request.getCheckType() == null || request.getCheckType().length == 0) {
                log.error("check type is empty!");
                throw new BizException(Constants.INVALID_PARAMETER);
            }

            if (StringUtil.isEmpty(request.getCheckContent())) {
                log.error("check content is empty!");
                throw new BizException(Constants.INVALID_PARAMETER);
            }

            List<String> checkTypeList = Arrays.asList(ALL_CHECK_TYPES);
            for (String type : request.getCheckType()) {
                if (!checkTypeList.contains(type)) {
                    log.error("invalid check type: " + type);
                    throw new BizException(Constants.INVALID_PARAMETER);
                }
            }

            final ContentCheckResult checkResult = contentCheckApiService.check(request.getCheckContent(), CollectionUtil.join(Arrays.asList(request.getCheckType()), ";"));
            if (!checkResult.isOk()) {
                log.error("check return error: " + checkResult.getMessage() + ", content is " + request);
                throw new RemoteException("文本校对错误：" + checkResult.getMessage());
            }
            return JSON.parseObject(checkResult.getResult());
        }, OperationType.QUERY, logDesc, "");
    }

}
