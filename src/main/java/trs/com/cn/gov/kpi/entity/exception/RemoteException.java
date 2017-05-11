package trs.com.cn.gov.kpi.entity.exception;

/**
 * Created by wangxuan on 2017/5/10.
 */
public class RemoteException extends Exception {

    public RemoteException() {

        super();
    }

    public RemoteException(String message) {

        super(message);
    }

    public RemoteException(String message, Throwable cause) {

        super(message, cause);
    }

    public RemoteException(Throwable cause) {

        super(cause);
    }

    protected RemoteException(String message, Throwable cause,
                           boolean enableSuppression,
                           boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
