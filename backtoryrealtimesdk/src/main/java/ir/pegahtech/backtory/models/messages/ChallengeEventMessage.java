package ir.pegahtech.backtory.models.messages;

/**
 * Created by Mohammad on 5/4/16 AD.
 */
public class ChallengeEventMessage extends BacktoryRealtimeMessage {
    private String message;
    private String userId;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
