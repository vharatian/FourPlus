package ir.pegahtech.backtory.models.messages;

/**
 * Created by Mohammad on 5/5/16 AD.
 */
public class PushNotifMessage extends BacktoryConnectivityMessage {
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
