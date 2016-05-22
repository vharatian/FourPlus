package ir.pegahtech.backtory.models.messages;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ir.pegahtech.backtory.models.messages.nested.MatchFoundParticipant;

/**
 * Created by Mohammad on 3/28/16 AD.
 */
public class MatchFoundMessage extends BacktoryConnectivityMessage {
    private String realtimeChallengeId;
    private String address;
    private int port;
    private String matchmakingName;
    private String requestId;
    private List<MatchFoundParticipant> participants;
    private String extraMessage;
    private String ownerUserId;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getMatchmakingName() {
        return matchmakingName;
    }

    public void setMatchmakingName(String matchmakingName) {
        this.matchmakingName = matchmakingName;
    }

    public List<MatchFoundParticipant> getParticipants() {
        if (participants == null)
            participants = new ArrayList();

        return participants;
    }

    public void setParticipants(List<MatchFoundParticipant> participants) {
        this.participants = participants;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getRealtimeChallengeId() {
        return realtimeChallengeId;
    }

    public void setRealtimeChallengeId(String realtimeChallengeId) {
        this.realtimeChallengeId = realtimeChallengeId;
    }

    public String getExtraMessage() {
        return extraMessage;
    }

    public void setExtraMessage(String extraMessage) {
        this.extraMessage = extraMessage;
    }

    public String getOwnerUserId() {
        return ownerUserId;
    }
}
