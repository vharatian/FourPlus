package ir.pegahtech.backtory.http;

public class NetworkFailureException extends Exception {

    private int resultCode;

    /**
     *
     */
    private static final long serialVersionUID = 3913163106305989043L;

    public NetworkFailureException() {
        super();
        resultCode = -1;
    }

    public NetworkFailureException(String msg) {
        super(msg);
        resultCode = -1;
    }

    public NetworkFailureException(Throwable t) {
        super(t);
        resultCode = -1;
    }

    public NetworkFailureException(String msg, int responseCode) {
        super(msg);
        this.resultCode = HttpConnectionUtility.responseCodeToResultCodeConverter(responseCode);
    }

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }
}
