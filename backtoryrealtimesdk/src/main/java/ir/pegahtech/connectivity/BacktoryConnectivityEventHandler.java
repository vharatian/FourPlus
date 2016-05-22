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
public abstract class BacktoryConnectivityEventHandler {

    public void onMatchFound(MatchFoundMessage matchFoundMessage){}

    public void onOpen(){}

    public void onMessage(String message){}

    public void onException(List<ExceptionResponse> exceptions, String exceptionsString){}

    public void onError(String message){}

    public void onClose(){}

    public void onMatchNotFound(MatchNotFoundMessage matchNotFoundMessage){}

    public void onMatchUpdate(MatchUpdateMessage matchUpdateMessage){}

    public void onChatMessage(ChatMessage chatMessage){}

    public void onPushMessage(PushNotifMessage pushNotifMessage){}

    public void onMatchCancellationResponse(String requestId){}

    public void onMatchResponse(String requestId){}
}
