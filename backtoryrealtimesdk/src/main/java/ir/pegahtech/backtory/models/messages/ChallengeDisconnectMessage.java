package ir.pegahtech.backtory.models.messages;

/**
 * Created by Mohammad on 5/5/16 AD.
 */
public class ChallengeDisconnectMessage extends BacktoryRealtimeMessage {
    private String userId;
    private String username;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
