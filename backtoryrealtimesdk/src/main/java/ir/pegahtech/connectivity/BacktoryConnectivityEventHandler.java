package ir.pegahtech.connectivity;

import java.util.List;

import ir.pegahtech.backtory.models.messages.ChatMessage;
import ir.pegahtech.backtory.models.messages.MatchFoundMessage;
import ir.pegahtech.backtory.models.messages.MatchNotFoundMessage;
import ir.pegahtech.backtory.models.messages.MatchUpdateMessage;
import ir.pegahtech.backtory.models.messages.PushNotifMessage;
import ir.pegahtech.backtory.models.messages.nested.ExceptionResponse;

/**
 * Created by Mohammad on 5/2/16 AD.
 */
public interface BacktoryConnectivityEventHandler {

    void onMatchFound(MatchFoundMessage matchFoundMessage);

    void onOpen();

    void onMessage(String message);

    void onException(List<ExceptionResponse> exceptions, String exceptionsString);

    void onError(String message);

    void onClose();

    void onMatchNotFound(MatchNotFoundMessage matchNotFoundMessage);

    void onMatchUpdate(MatchUpdateMessage matchUpdateMessage);

    void onChatMessage(ChatMessage chatMessage);

    void onPushMessage(PushNotifMessage pushNotifMessage);
}
