package ir.pegahtech.backtory.models.messages;

/**
 * Created by Mohammad on 5/4/16 AD.
 */
public class StartedWebhookMessage extends BacktoryRealtimeMessage {

    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
