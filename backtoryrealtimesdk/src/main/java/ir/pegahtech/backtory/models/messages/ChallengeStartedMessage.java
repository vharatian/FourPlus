package ir.pegahtech.backtory.models.messages;

/**
 * Created by Mohammad on 5/4/16 AD.
 */
public class ChallengeStartedMessage extends BacktoryRealtimeMessage {

    private long startedDate;

    public long getStartedDate() {
        return startedDate;
    }

    public void setStartedDate(long startedDate) {
        this.startedDate = startedDate;
    }
}
