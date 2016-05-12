package ir.pegahtech.backtory.models.messages;

/**
 * Created by Mohammad on 5/5/16 AD.
 */
public class BacktoryConnectivityMessage {
    // Connectivity:
    public static String PUSH = ".Push";
    public static String EXCEPTION = ".Exception";
    public static String CHAT_MESSAGE = ".ChatMessage";
    public static String MATCH_FOUND_MESSAGE = ".MatchFoundMessage";
    public static String MATCH_UPDATE_MESSAGE = ".MatchUpdateMessage";
    public static String MATCHMAKING_RESPONSE = ".MatchmakingResponse";
    public static String MATCH_NOT_FOUND_MESSAGE = ".MatchNotFoundMessage";

    private String _class;

    public String get_class() {
        return _class;
    }

    public void set_class(String _class) {
        this._class = _class;
    }
}
