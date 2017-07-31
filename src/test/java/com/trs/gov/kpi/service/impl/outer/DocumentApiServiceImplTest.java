package com.trs.gov.kpi.service.impl.outer;

import com.trs.gov.kpi.entity.exception.RemoteException;
import com.trs.gov.kpi.entity.outerapi.ChnlDoc;
import com.trs.gov.kpi.entity.outerapi.Document;
import com.trs.gov.kpi.service.outer.DocumentApiService;
import org.junit.Test;

import java.text.ParseException;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by he.lang on 2017/7/13.
 */
public class DocumentApiServiceImplTest {
    @Test
    public void findDocumentByUrl_null_String() throws Exception {
        MockDocumentApiService service = new MockDocumentApiService();
        ChnlDoc document = service.findDocumentByUrl("", "");
        assertEquals(null, document);
    }

    @Test
    public void findDocumentByUrl() throws RemoteException {
        MockDocumentApiService service = new MockDocumentApiService();
        ChnlDoc document = service.findDocumentByUrl("", "www.test.com");
        assertEquals(1, document.getChnlId());
        assertEquals("test", document.getDocTitle());
    }

    public class  MockDocumentApiService implements DocumentApiService {

        @Override
        public List<Integer> getPublishDocIds(String userName, int siteId, int channelId, String beginTime) throws RemoteException {
            return null;
        }

        @Override
        public Document getDocument(String userName, int channelId, int docmentId) throws RemoteException {
            return null;
        }

        @Override
        public List<Document> getPublishDocuments(int siteId) throws RemoteException, ParseException {
            return null;
        }

        @Override
        public ChnlDoc findDocumentByUrl(String userName, String url) throws RemoteException {
            if("www.test.com".equals(url)){
                ChnlDoc document = new ChnlDoc();
                document.setChnlId(1);
                document.setDocTitle("test");
                return document;
            }
            return null;
        }
    }

}