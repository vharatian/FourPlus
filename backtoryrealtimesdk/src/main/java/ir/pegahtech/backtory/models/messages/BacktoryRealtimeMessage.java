package ir.pegahtech.backtory.models.messages;

/**
 * Created by Mohammad on 3/28/16 AD.
 */
public class BacktoryRealtimeMessage {

    // Realtime:
    public static String CHAT = ".ChatMessage";
    public static String MASTER = ".MasterMessage";
    public static String DIRECT = ".DirectMessage";
    public static String CHALLENGE = ".ChallengeMessage";
    public static String WEBHOOK_ERROR = ".WebhookErrorMessage";
    public static String JOINED_WEBHOOK = ".JoinedWebhookMessage";
    public static String STARTED_WEBHOOK = ".StartedWebhookMessage";
    public static String CHALLENGE_ENDED = ".ChallengeEndedMessage";
    public static String CHALLENGE_JOINED = ".ChallengeJoinedMessage";
    public static String CHALLENGE_LEAVED = ".ChallengeLeavedMessage";
    public static String CHALLENGE_STARTED = ".ChallengeStartedMessage";
    public static String CHALLENGE_DISCONNECT = ".ChallengeDisconnectedMessage";
    public static String RESPONSE_RECEIVED = ".ResponseReceivedMessage";

    private String _class;

    public String get_class() {
        return _class;
    }

    public void set_class(String _class) {
        this._class = _class;
    }

}
