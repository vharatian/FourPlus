package ir.pegahtech.backtory.models.messages;

import ir.pegahtech.backtory.http.HttpStatus;

/**
 * Created by Mohammad on 5/5/16 AD.
 */
public class WebhookErrorMessage extends BacktoryRealtimeMessage {

    private HttpStatus httpStatus;
    private String message;

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }
}
