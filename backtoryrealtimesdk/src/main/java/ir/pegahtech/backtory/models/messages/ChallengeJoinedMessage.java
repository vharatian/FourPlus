package ir.pegahtech.backtory.models.messages;

import java.util.List;

/**
 * Created by Mohammad on 5/4/16 AD.
 */
public class ChallengeJoinedMessage extends BacktoryRealtimeMessage {
    private String userId;
    private String username;
    private List<String> connectedUserIds;

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

    public List<String> getConnectedUserIds() {
        return connectedUserIds;
    }

    public void setConnectedUserIds(List<String> connectedUserIds) {
        this.connectedUserIds = connectedUserIds;
    }
}
