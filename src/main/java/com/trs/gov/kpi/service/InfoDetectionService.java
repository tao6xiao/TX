package com.trs.gov.kpi.service;

import com.trs.gov.kpi.constant.DocumentErrorType;
import com.trs.gov.kpi.entity.exception.RemoteException;
import lombok.Data;

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

    List<String> DETECTION_TYPE = Arrays.asList(
            DocumentErrorType.WORD.getKeyWord(),
            DocumentErrorType.SENSITIVE.getKeyWord());

    Set<String> detectText(String text) throws RemoteException;

    List<String> getDocumentIdsBySiteId(Integer siteId);

    Document getDocumentById(String documentId);

    @Data
    class Document {

        String title;

        String text;

        String link;
    }
}
