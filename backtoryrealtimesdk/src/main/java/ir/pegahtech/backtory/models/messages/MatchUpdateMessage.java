package ir.pegahtech.backtory.models.messages;

import java.util.ArrayList;
import java.util.List;

import ir.pegahtech.backtory.models.messages.nested.MatchUpdateParticipant;

public class MatchUpdateMessage extends BacktoryConnectivityMessage {
    private String requestId;
    private List<MatchUpdateParticipant> participants;

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public List<MatchUpdateParticipant> getParticipants() {
        if (participants == null)
            participants = new ArrayList();

        return participants;
    }

    public void setParticipants(List<MatchUpdateParticipant> participants) {
        this.participants = participants;
    }

}
