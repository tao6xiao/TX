package com.trs.gov.kpi.service;

import com.trs.gov.kpi.entity.exception.RemoteException;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * Created by wangxuan on 2017/5/15.
 */
public interface InfoDetectionService {

    String DETECTION_URL = "http://jiaodui.trs.cn/rs/detect";

    String DETECTION_KEY_TYPE = "type";

    String DETECTION_KEY_TEXT = "text";

    List<String> DETECTION_TYPE = Arrays.asList("字词", "政治");

    Set<String> detectText(String text) throws RemoteException;
}
