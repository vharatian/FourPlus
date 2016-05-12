package ir.pegahtech.backtory.models.messages.nested;

import java.util.List;

import ir.pegahtech.backtory.models.messages.BacktoryConnectivityMessage;

public class ExceptionListResponse extends BacktoryConnectivityMessage {
    private List<ExceptionResponse> exceptions;

    public List<ExceptionResponse> getExceptions() {
        return exceptions;
    }

    public void setExceptions(List<ExceptionResponse> exceptions) {
        this.exceptions = exceptions;
    }

    public String exceptionsToString() {
        if (exceptions == null)
            return null;

        String message = "{";
        for (ExceptionResponse response : exceptions) {
            message += response.getKey() + ":" + response.getValue() + ",";
        }
        message = message.substring(0, message.length()-1);
        message += "}";
        return message;
    }
}
