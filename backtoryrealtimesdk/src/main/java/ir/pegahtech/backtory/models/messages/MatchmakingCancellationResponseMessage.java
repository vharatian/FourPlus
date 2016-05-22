package ir.pegahtech.backtory.models.messages;

/**
 * Created by Mohammad on 3/28/16 AD.
 */
public class MatchmakingCancellationResponseMessage extends BacktoryConnectivityMessage {

    private String requestId;

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

}
