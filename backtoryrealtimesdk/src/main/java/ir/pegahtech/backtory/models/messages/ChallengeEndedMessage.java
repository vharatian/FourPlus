package ir.pegahtech.backtory.models.messages;

import java.util.List;

/**
 * Created by Mohammad on 5/5/16 AD.
 */
public class ChallengeEndedMessage extends BacktoryRealtimeMessage {
    private List<String> winners;

    public List<String> getWinners() {
        return winners;
    }

    public void setWinners(List<String> winners) {
        this.winners = winners;
    }
}
