package com.trs.gov.kpi.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.gov.kpi.service.InfoDetectionService;
import com.trs.gov.kpi.utils.HttpUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by wangxuan on 2017/5/15.
 */
@Service
@Slf4j
public class InfoDetectionServiceImpl implements InfoDetectionService {

    @Override
    public Set<String> detectText(String text) throws RemoteException {

        Map<String, String> detectionParams = new HashMap<>();
        detectionParams.put(DETECTION_KEY_TYPE, StringUtils.join(DETECTION_TYPE, ";"));
        detectionParams.put(DETECTION_KEY_TEXT, text);

        try {

            String response = HttpUtils.postHttpInfo(DETECTION_URL, detectionParams);
            JSONObject detection = JSON.parseObject(response);
            Assert.isTrue(detection.getInteger("code") == 1, String.format("远程调用失败，返回为:%s", JSON.toJSONString(response)));

            JSONObject result = detection.getJSONObject("result");
            return result.keySet();
        } catch (Exception e) {

            log.error("detection text error!", e);
            throw new RemoteException();
        }
    }
}
