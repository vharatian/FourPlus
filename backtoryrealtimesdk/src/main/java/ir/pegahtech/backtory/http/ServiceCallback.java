package ir.pegahtech.backtory.http;

public interface ServiceCallback<T> {
    public static final int RESULT_CODE_NETWORK_FAILURE = 1;
    public static final int RESULT_CODE_GENERAL_FAILURE = 2;
    public static final int RESULT_CODE_NOT_FOUND = 3;
    public static final int RESULT_CODE_EXPECTATION_FAILED = 4;
    public static final int RESULT_CODE_BAD_REQUEST = 5;
    public static final int RESULT_CODE_REPETITIVE = 6;

    void success(T result);

    void fail(int resultCode);
}
